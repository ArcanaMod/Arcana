package net.arcanamod.systems.taint;

import com.google.common.collect.Lists;
import net.arcanamod.Arcana;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.VisShareable;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.DeadBlock;
import net.arcanamod.blocks.DeadPlantBlock;
import net.arcanamod.blocks.TaintedBlock;
import net.arcanamod.blocks.tainted.TaintedFallingBlock;
import net.arcanamod.blocks.tainted.TaintedPlantBlock;
import net.arcanamod.blocks.tainted.*;
import net.arcanamod.blocks.tiles.JarTileEntity;
import net.arcanamod.entities.tainted.*;
import net.arcanamod.fluids.ArcanaFluids;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.systems.taint.*;
import net.arcanamod.world.ServerAuraView;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.block.trees.OakTree;
import net.minecraft.block.trees.Tree;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static net.arcanamod.blocks.DelegatingBlock.switchBlock;
import static net.minecraft.entity.EntityClassification.MONSTER;

public class Taint{
	
	public static final BooleanProperty UNTAINTED = BooleanProperty.create("untainted"); // false by default
	private static final Map<Block, Block> taintMap = new HashMap<>();
	private static final Map<Block, Block> deadMap = new HashMap<>();

	public static void init(){
		addDeadUnstableBlock(
				Blocks.OAK_LEAVES,Blocks.BIRCH_LEAVES,Blocks.SPRUCE_LEAVES,Blocks.JUNGLE_LEAVES,Blocks.DARK_OAK_LEAVES,Blocks.ACACIA_LEAVES,
				ArcanaBlocks.DAIR_LEAVES.get(),ArcanaBlocks.EUCALYPTUS_LEAVES.get(),ArcanaBlocks.GREATWOOD_LEAVES.get(),ArcanaBlocks.HAWTHORN_LEAVES.get(),ArcanaBlocks.SILVERWOOD_LEAVES.get()
		);
	}
	
	@SuppressWarnings("deprecation")
	public static Block taintedOf(Block parent, Block... blocks){
		Block tainted;
		if (parent instanceof FallingBlock)
			tainted = new TaintedFallingBlock(parent);
		else if (parent instanceof SaplingBlock)
			tainted = new TaintedSaplingBlock(parent);
		else if (parent instanceof IPlantable || parent instanceof IShearable || parent instanceof IGrowable)
			tainted = new TaintedPlantBlock(parent);
		else if (parent instanceof StairsBlock)
			tainted = new TaintedStairsBlock(parent);
		else if (parent instanceof SlabBlock)
			tainted = new TaintedSlabBlock(parent);
		else
			tainted = new TaintedBlock(parent);

		//Add children to TaintMapping, NOT parent (see TaintedBlock)!
		for (Block block : blocks) {
			Taint.addTaintMapping(block, tainted);
		}

		return tainted;
	}

	public static void addDeadUnstableBlock(Block... blocks) {
		deadMap.putAll(Lists.newArrayList(blocks).stream().collect(Collectors.toMap(block -> block,block -> Blocks.AIR)));
	}

	@SuppressWarnings("deprecation")
	public static Block deadOf(Block parent, Block... blocks){
		Block dead;
		if (parent instanceof IPlantable || parent instanceof IShearable || parent instanceof IGrowable)
			dead = new DeadPlantBlock(parent);
		else
			dead = new DeadBlock(parent);

		//Add children to DeadMapping, NOT parent (see DeadBlock)!
		for (Block block : blocks) {
			Taint.addDeadMapping(block, dead);
		}

		return dead;
	}
	
	public static Block getPureOfBlock(Block block){
		return taintMap.entrySet().stream()
				.filter(entry -> entry.getValue() == block)
				.map(Map.Entry::getKey)
				.findAny().orElse(null);
	}

	public static Block getLivingOfBlock(Block block){
		return deadMap.entrySet().stream()
				.filter(entry -> entry.getValue() == block)
				.map(Map.Entry::getKey)
				.findAny().orElse(null);
	}

	public static Block getDeadOfBlock(Block block){
		return deadMap.getOrDefault(block,block);
	}
	
	public static Block getTaintedOfBlock(Block block){
		return taintMap.get(block);
	}

	public static void addTaintMapping(Block original, Block tainted){
		taintMap.put(original, tainted);
	}

	public static void addDeadMapping(Block original, Block dead){
		deadMap.put(original, dead);
	}
	
	public static void tickTaintedBlock(BlockState state, ServerWorld world, BlockPos pos, Random random){
		if(state.getBlock() == ArcanaFluids.TAINT_FLUID_BLOCK.get() || !state.get(UNTAINTED)){
			// and if flux level is greater than 5,
			ServerAuraView auraView = new ServerAuraView(world);
			int at = auraView.getTaintAt(pos);
			if(at > ArcanaConfig.TAINT_SPREAD_MIN_FLUX.get()){
				// pick a block within a 4x6x4 area
				// If this block is air, stop. If this block doesn't have a tainted form, re-roll.
				// Do this up to 5 times.
				Block tainted = null;
				BlockPos taintingPos = pos;
				int iter = 0;
				while(tainted == null && iter < ArcanaConfig.TAINT_SPREAD_TRIES.get()){
					int xzSpread = ArcanaConfig.TAINT_SPREAD_XZ.get();
					int ySpread = ArcanaConfig.TAINT_SPREAD_Y.get();
					taintingPos = pos.north(random.nextInt(xzSpread * 2 + 1) - xzSpread).west(random.nextInt(xzSpread * 2 + 1) - xzSpread).up(random.nextInt(ySpread * 2 + 1) - ySpread);
					tainted = world.getBlockState(taintingPos).getBlock();
					if(tainted.isAir(world.getBlockState(taintingPos), world, taintingPos)){
						tainted = null;
						break;
					}
					tainted = Taint.getTaintedOfBlock(tainted);
					iter++;
				}
				// Replace it with its tainted form if found.
				if(tainted != null){
					BlockState taintedState = switchBlock(world.getBlockState(taintingPos), tainted).with(UNTAINTED, false);
					world.setBlockState(taintingPos, taintedState);
					// Reduce flux level
					auraView.addTaintAt(pos, -ArcanaConfig.TAINT_SPREAD_FLUX_COST.get());
					// Schedule a tick
					world.getPendingBlockTicks().scheduleTick(pos, state.getBlock(), taintTickWait(at));
				}
			}
		}
	}
	
	public static int taintTickWait(int taintLevel){
		// more taint level -> less tick wait
		int base = (int)((1d / taintLevel) * 200);
		return base > 0 ? base : 1;
	}

	public static void tickTaintInContainer(Object sender) {
		if (sender instanceof JarTileEntity){
			JarTileEntity jar = (JarTileEntity)sender;
			if (!((VisShareable)jar).isSecure()) {
				if (jar.getWorld().rand.nextInt(20) == 2)
					jar.vis.drain(0, 1, false);
				if (jar.getWorld().isRemote) return;
				ServerAuraView auraView = new ServerAuraView((ServerWorld) jar.getWorld());
				if (jar.getWorld().rand.nextInt(20) == 2)
					auraView.addTaintAt(jar.getPos(), 1);
			}
		}
	}
	
	public static boolean isAreaInTaintBiome(BlockPos pos, IBlockReader world){
		// check if they're in a taint biome
		// 7x13x7 cube, centred on the entity
		// at least 20 tainted blocks
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		int counter = 0;
			for(int x = -3; x < 7; x++)
				for(int y = -6; y < 13; y++)
					for(int z = -3; z < 7; z++){
						mutable.setPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
						try{
							if(isTainted(world.getBlockState(mutable).getBlock()))
								counter++;
						}catch(ArrayIndexOutOfBoundsException ignored){
							// ChunkRenderCache throws this when you try to check somewhere "out-of-bounds".
						}
						if(counter >= 20)
							return true;
					}
		return false;
	}
	
	@SuppressWarnings({"rawtypes"})
	private static final Map<EntityType, EntityType> entityTaintMap = new HashMap<>();

	@SuppressWarnings({"rawtypes"})
	public static EntityType taintedEntityOf(EntityType entity){
		if(entity.getRegistryName() == null)
			return null;
		EntityType<? extends Entity> tainted;

		String id = new ResourceLocation(Arcana.MODID, "tainted_" + entity.getRegistryName().getPath()).toString();
		float w = entity.getSize().width, h = entity.getSize().height;

		EntityType.IFactory<?> factoryIn =
				entity == EntityType.BAT
						? TaintedBatEntity::new
						: entity == EntityType.EVOKER
						|| entity == EntityType.ILLUSIONER
						|| entity == EntityType.VINDICATOR
						|| entity == EntityType.PILLAGER
						? TaintedIllagerEntity::new
						: entity == EntityType.CREEPER
						? TaintedCreeperEntity::new
						: entity == EntityType.SQUID
						? TaintedSquidEntity::new
						: entity == EntityType.GHAST
						? TaintedGhastEntity::new
						: entity == EntityType.CAVE_SPIDER
						? TaintedCaveSpiderEntity::new
						: entity == EntityType.SKELETON
						? TaintedSkeletonEntity::new
						: entity == EntityType.SLIME
						? TaintedSlimeEntity::new
						: entity == EntityType.BEE
						? TaintedBeeEntity::new
						: entity == EntityType.PANDA
						? TaintedPandaEntity::new
						: entity == EntityType.SNOW_GOLEM
						? TaintedSnowGolemEntity::new
						: entity == EntityType.RABBIT
						? TaintedRabbitEntity::new
						: entity == EntityType.POLAR_BEAR
						? TaintedPolarBearEntity::new
						: entity == EntityType.DONKEY
						? TaintedDonkeyEntity::new
						: entity == EntityType.CAT
						? TaintedCatEntity::new
						: entity == EntityType.LLAMA
						|| entity == EntityType.TRADER_LLAMA
						? TaintedLlamaEntity::new
						: entity == EntityType.PUFFERFISH
						? TaintedPufferfishEntity::new
						: entity == EntityType.PARROT
						? TaintedParrotEntity::new
						: entity == EntityType.HORSE
						? TaintedHorseEntity::new
						: (type, world) -> new TaintedEntity(type, world, entity);
		tainted = EntityType.Builder.create(factoryIn, MONSTER).size(w, h).build(id);

		entityTaintMap.put(entity, tainted);
		return tainted;
	}

	@SuppressWarnings({"rawtypes"})
	public static EntityType getTaintedOfEntity(EntityType entity) {
		return entityTaintMap.get(entity);
	}

	public static boolean isTainted(EntityType<?> entity) {
		return entityTaintMap.containsValue(entity);
	}
	
	public static boolean isTainted(Block block) {
		return taintMap.containsValue(block);
	}

	@SuppressWarnings("rawtypes")
	public static Collection<EntityType> getTaintedEntities(){
		return entityTaintMap.values();
	}

	public static Tree taintedTreeOf(SaplingBlock block) {
		if (block == Blocks.OAK_SAPLING)
			return new TaintedOakTree();
		if (block == Blocks.BIRCH_SAPLING)
			return new TaintedBirchTree();
		if (block == Blocks.SPRUCE_SAPLING)
			return new TaintedSpruceTree();
		if (block == Blocks.JUNGLE_SAPLING)
			return new TaintedJungleTree();
		if (block == Blocks.ACACIA_SAPLING)
			return new TaintedAcaciaTree();
		if (block == Blocks.DARK_OAK_SAPLING)
			return new TaintedDarkOakTree();
		return new TaintedOakTree();
	}
}