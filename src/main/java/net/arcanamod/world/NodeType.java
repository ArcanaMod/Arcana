package net.arcanamod.world;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.*;
import net.arcanamod.client.render.particles.ArcanaParticles;
import net.arcanamod.client.render.particles.NodeParticleData;
import net.arcanamod.items.settings.GogglePriority;
import net.arcanamod.systems.vis.VisUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

import static net.arcanamod.Arcana.arcLoc;
import static net.arcanamod.aspects.AspectUtils.primalAspects;

// Although IDEA complains about class loading deadlock, this only occurs under specific conditions.
// Handles type-specific things, such as behaviour and vis generation rates.
@SuppressWarnings("StaticInitializerReferencesSubClass")
public abstract class NodeType{
	
	// A diet registry, used for serialization and deserialization.
	public static final BiMap<ResourceLocation, NodeType> TYPES = HashBiMap.create(6);
	public static final Set<NodeType> SPECIAL_TYPES = new HashSet<>(5);
	
	public static final NodeType
			NORMAL = new NormalNodeType(),
			BRIGHT = new BrightNodeType(),
			PALE = new PaleNodeType(),
			ELDRITCH = new EldritchNodeType(),
			HUNGRY = new HungryNodeType(),
			PURE = new PureNodeType(),
			TAINTED = new TaintedNodeType();
	
	public static final NodeType DEFAULT = NORMAL;
	
	public static void init(){
		TYPES.put(arcLoc("normal"), NORMAL);
		TYPES.put(arcLoc("bright"), BRIGHT);
		TYPES.put(arcLoc("pale"), PALE);
		TYPES.put(arcLoc("eldritch"), ELDRITCH);
		TYPES.put(arcLoc("hungry"), HUNGRY);
		TYPES.put(arcLoc("pure"), PURE);
		TYPES.put(arcLoc("tainted"), TAINTED);
		
		SPECIAL_TYPES.add(BRIGHT);
		SPECIAL_TYPES.add(PALE);
		SPECIAL_TYPES.add(ELDRITCH);
		SPECIAL_TYPES.add(HUNGRY);
		SPECIAL_TYPES.add(PURE);
	}
	
	public void tick(IWorld world, AuraView nodes, Node node){
		// Display the node
		if(world.isRemote()){
			GogglePriority priority = GogglePriority.getClientGogglePriority();
			if(priority == GogglePriority.SHOW_NODE || priority == GogglePriority.SHOW_ASPECTS)
				world.addParticle(new NodeParticleData(node.nodeUniqueId(), node.type().texture(world, nodes, node)), node.getX(), node.getY(), node.getZ(), 0, 0, 0);
		}
		
		// Regenerate aspects over time
		// Time: default is 20 * 60, bright has 20 * 45, pale has 20 * 75
		// Cap: default is 23, bright has 47, pale has 11
		// Power: default is 5, bright has 7, pale has 4 (all +/- 1)
		// These are random numbers I pulled out of the void, please adjust as is reasonable
		
		if(node.timeUntilRecharge <= 0){
			node.timeUntilRecharge = rechargeTime(world, nodes, node);
			// are we below our cap? if so, charge
			if(node.getAspects().getHolders().stream().mapToDouble(IAspectHolder::getCurrentVis).sum() < rechargeCap(world, nodes, node)){
				int toCharge = rechargePower(world, nodes, node) + world.getRandom().nextInt(3) - 1;
				// pick a random aspect and give 1-3 aspect to it until we're done
				while(toCharge > 0){
					IAspectHolder holder = node.getAspects().getHolders().get(world.getRandom().nextInt(node.getAspects().getHolders().size()));
					int giving = Math.min(world.getRandom().nextInt(3) + 1, toCharge);
					holder.insert(new AspectStack(holder.getContainedAspect(), giving), false);
					toCharge -= giving;
				}
			}
		}else
			node.timeUntilRecharge--;
	}
	
	public abstract ResourceLocation texture(IWorld world, AuraView nodes, Node node);
	public abstract Collection<ResourceLocation> textures();
	
	public int rechargeTime(IWorld world, AuraView nodes, Node node){
		return 20 * 60;
	}
	
	public int rechargeCap(IWorld world, AuraView nodes, Node node){
		return 23;
	}
	
	public int rechargePower(IWorld world, AuraView nodes, Node node){
		return 5;
	}
	
	public boolean blocksTaint(IWorld world, AuraView nodes, Node node, BlockPos pos){
		return false;
	}
	
	public String toString(){
		return TYPES.containsValue(this) ? TYPES.inverse().get(this).toString() : super.toString();
	}
	
	public IAspectHandler genBattery(BlockPos location, IWorld world, Random random){
		AspectBattery battery = new AspectBattery(6, -1);
		// 2-4 random primal aspects
		int primalCount = 2 + random.nextInt(3);
		for(int i = 0; i < primalCount; i++){
			// the same primal can be added multiple times
			Aspect aspect = primalAspects[random.nextInt(primalAspects.length)];
			// 10-27
			int amount = 10 + random.nextInt(18);
			IAspectHolder holder = battery.findAspectInHolders(aspect);
			if(holder != null)
				holder.insert(new AspectStack(aspect, amount), false);
			else{
				AspectCell cell = new AspectCell();
				cell.setCapacity(-1);
				cell.insert(new AspectStack(aspect, amount), false);
				cell.setWhitelist(aspect);
				battery.createCell(cell);
			}
		}
		// if the node is underwater, add 5-10 aqua
		if(world.getFluidState(location).isTagged(FluidTags.WATER)){
			Aspect aspect = Aspects.WATER;
			int amount = 5 + random.nextInt(6);
			IAspectHolder holder = battery.findAspectInHolders(aspect);
			if(holder != null)
				holder.insert(new AspectStack(aspect, amount), false);
			else{
				AspectCell cell = new AspectCell();
				cell.insert(new AspectStack(aspect, amount), false);
				cell.setCapacity(-1);
				cell.setWhitelist(aspect);
				battery.createCell(cell);
			}
		}
		return battery;
	}
	
	public static class NormalNodeType extends NodeType{
		
		public ResourceLocation texture(IWorld world, AuraView nodes, Node node){
			return arcLoc("nodes/normal_node");
		}
		
		public Collection<ResourceLocation> textures(){
			return Collections.singleton(arcLoc("nodes/normal_node"));
		}
	}
	
	public static class BrightNodeType extends NodeType{
		
		public ResourceLocation texture(IWorld world, AuraView nodes, Node node){
			return arcLoc("nodes/bright_node");
		}
		
		public Collection<ResourceLocation> textures(){
			return Collections.singleton(arcLoc("nodes/brightest_node"));
		}
		
		// Add 50% to all aspects
		public IAspectHandler genBattery(BlockPos location, IWorld world, Random random){
			IAspectHandler handler = super.genBattery(location, world, random);
			for(IAspectHolder holder : handler.getHolders())
				holder.insert(new AspectStack(holder.getContainedAspect(), holder.getCurrentVis() / 2), false);
			return handler;
		}
		
		public int rechargeTime(IWorld world, AuraView nodes, Node node){
			return 20 * 45;
		}
		
		public int rechargeCap(IWorld world, AuraView nodes, Node node){
			return 47;
		}
		
		public int rechargePower(IWorld world, AuraView nodes, Node node){
			return 7;
		}
	}
	
	public static class PaleNodeType extends NodeType{
		
		public ResourceLocation texture(IWorld world, AuraView nodes, Node node){
			return arcLoc("nodes/fading_node");
		}
		
		public Collection<ResourceLocation> textures(){
			return Collections.singleton(arcLoc("nodes/fading_node"));
		}
		
		// Remove 30% from all aspects
		public IAspectHandler genBattery(BlockPos location, IWorld world, Random random){
			IAspectHandler handler = super.genBattery(location, world, random);
			for(IAspectHolder holder : handler.getHolders())
				holder.drain(new AspectStack(holder.getContainedAspect(), (int)(holder.getCurrentVis() * 0.3)), false);
			return handler;
		}
		
		public int rechargeTime(IWorld world, AuraView nodes, Node node){
			return 20 * 75;
		}
		
		public int rechargeCap(IWorld world, AuraView nodes, Node node){
			return 11;
		}
		
		public int rechargePower(IWorld world, AuraView nodes, Node node){
			return 4;
		}
	}
	
	public static class EldritchNodeType extends NodeType{
		
		public ResourceLocation texture(IWorld world, AuraView nodes, Node node){
			return arcLoc("nodes/eldritch_node");
		}
		
		public Collection<ResourceLocation> textures(){
			return Collections.singleton(arcLoc("nodes/eldritch_node"));
		}
	}
	
	@SuppressWarnings("deprecation")
	public static class HungryNodeType extends NodeType{
		
		public ResourceLocation texture(IWorld world, AuraView nodes, Node node){
			return arcLoc("nodes/hungry_node");
		}
		
		public Collection<ResourceLocation> textures(){
			return Collections.singleton(arcLoc("nodes/hungry_node"));
		}
		
		public void tick(IWorld world, AuraView nodes, Node node){
			super.tick(world, nodes, node);
			// check all blocks in range
			int range = (int)(0.7f * MathHelper.sqrt(node.aspects.getHolders().stream().mapToDouble(IAspectHolder::getCurrentVis).sum())) + 1;
			BlockPos nodePos = new BlockPos(node);
			BlockPos.Mutable cursor = new BlockPos.Mutable();
			for(int x = -range; x < range; x++)
				for(int y = -range; y < range; y++)
					for(int z = -range; z < range; z++){
						cursor.setPos(nodePos).move(x, y, z);
						// if they have air on at least one side
						BlockState state = world.getBlockState(cursor);
						if(!state.isAir() && !(state.getBlock() instanceof IFluidBlock || state.getBlock() instanceof FlowingFluidBlock) && (cursor.distanceSq(node, true) < range * range) && (cursor.distanceSq(node, true) < 2 || (emptySpace(world.getBlockState(cursor.up())) || emptySpace(world.getBlockState(cursor.down())) || emptySpace(world.getBlockState(cursor.north())) || emptySpace(world.getBlockState(cursor.south())) || emptySpace(world.getBlockState(cursor.east())) || emptySpace(world.getBlockState(cursor.west()))))){
							// have particles moving from the block to the node
							float xR = world.getRandom().nextFloat(), yR = world.getRandom().nextFloat(), zR = world.getRandom().nextFloat();
							if(world.getRandom().nextBoolean())
								world.addParticle(new BlockParticleData(ArcanaParticles.HUNGRY_NODE_BLOCK_PARTICLE.get(), state).setPos(cursor), cursor.getX() + xR, cursor.getY() + yR, cursor.getZ() + zR, -(x - node.getX() % 1 + xR - 1) / 20f, -(y - node.getY() % 1 + yR) / 20f, -(z - node.getZ() % 1 + zR) / 20f);
							// TODO: minimum time to break (instead of pure random)
							if(!world.isRemote()){
								float hardness = state.getBlockHardness(world, cursor);
								if(hardness != -1 && world.getRandom().nextInt((int)(hardness * 300) + 1) == 0){
									// note down that the block has been broken
									CompoundNBT blocks = node.getData().getCompound("blocks");
									node.getData().put("blocks", blocks);
									String key = Objects.requireNonNull(state.getBlock().getRegistryName()).toString();
									blocks.putInt(key, blocks.getInt(key) + 1);
									// gain a fraction of that block's aspects
									if(ArcanaConfig.HUNGRY_NODE_ASPECT_CARRY_FRACTION.get() > 0){
										if(world instanceof ServerWorld)
											for(ItemStack drop : Block.getDrops(state, (ServerWorld)world, cursor, world.getTileEntity(cursor)))
												for(AspectStack stack : ItemAspectRegistry.get(drop)){
													int toInsert = (int)Math.max(1, stack.getAmount() * ArcanaConfig.HUNGRY_NODE_ASPECT_CARRY_FRACTION.get());
													VisUtils.moveAspects(stack.getAspect(), toInsert, node.aspects, -1);
												}
									}
									// send changes to client
									if(nodes instanceof ServerAuraView)
										((ServerAuraView)nodes).sendChunkToClients(node);
									// destroy the block
									world.destroyBlock(cursor, false);
								}
							}
						}
					}
			// make disc particles
			// disc radius = 1/3 * pull radius
			CompoundNBT blocks = node.getData().getCompound("blocks");
			if(blocks.keySet().size() > 0){
				float discRad = (float)(range * (1 / 3f) + world.getRandom().nextGaussian() / 5f);
				float xPos = (float)(node.x);
				float zPos = (float)(node.z - discRad);
				// TODO: weighted selection
				BlockState state = Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blocks.keySet().toArray(new String[0])[world.getRandom().nextInt(blocks.keySet().size())]))).getDefaultState();
				world.addParticle(new BlockParticleData(ArcanaParticles.HUNGRY_NODE_DISC_PARTICLE.get(), state).setPos(nodePos), xPos, node.y, zPos, discRad / 6f, 0, discRad / 6f);
			}
		}
		
		private static boolean emptySpace(BlockState state){
			return state.isAir() || state.getBlock() instanceof IFluidBlock || state.getBlock() instanceof FlowingFluidBlock;
		}
	}

	public static class PureNodeType extends NodeType{

		public ResourceLocation texture(IWorld world, AuraView nodes, Node node){
			return arcLoc("nodes/pure_node");
		}

		public Collection<ResourceLocation> textures(){
			return Collections.singleton(arcLoc("nodes/pure_node"));
		}
		
		public boolean blocksTaint(IWorld world, AuraView nodes, Node node, BlockPos pos){
			return true;
		}
	}
	
	public static class TaintedNodeType extends NodeType{
		
		public ResourceLocation texture(IWorld world, AuraView nodes, Node node){
			return arcLoc("nodes/tainted_node");
		}
		
		public Collection<ResourceLocation> textures(){
			return Collections.singleton(arcLoc("nodes/tainted_node"));
		}
		
		public IAspectHandler genBattery(BlockPos location, IWorld world, Random random){
			AspectBattery handler = (AspectBattery)super.genBattery(location, world, random);
			// Add 5-15 taint
			// This is only accessible using /arcana-nodes
			AspectCell cell = new AspectCell();
			Aspect aspect = Aspects.TAINT;
			cell.insert(new AspectStack(aspect, 5 + random.nextInt(11)), false);
			cell.setWhitelist(aspect);
			handler.createCell(cell);
			return handler;
		}
	}
}