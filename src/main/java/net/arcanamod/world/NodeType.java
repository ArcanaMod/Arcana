package net.arcanamod.world;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.arcanamod.aspects.*;
import net.arcanamod.client.render.ArcanaParticles;
import net.arcanamod.client.render.NodeParticleData;
import net.arcanamod.util.GogglePriority;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.*;

import static net.arcanamod.Arcana.arcLoc;
import static net.arcanamod.aspects.Aspects.primalAspects;

// Although IDEA complains about class loading deadlock, this only occurs under specific conditions.
// Handles type-specific things, such as behaviour and vis generation rates.
@SuppressWarnings("StaticInitializerReferencesSubClass")
public abstract class NodeType{
	
	// A diet registry, used for serialization and deserialization.
	public static final BiMap<ResourceLocation, NodeType> TYPES = HashBiMap.create(6);
	public static final Set<NodeType> SPECIAL_TYPES = new HashSet<>(4);
	
	public static final NodeType
			NORMAL = new NormalNodeType(),
			BRIGHT = new BrightNodeType(),
			PALE = new PaleNodeType(),
			ELDRITCH = new EldritchNodeType(),
			HUNGRY = new HungryNodeType(),
			TAINTED = new TaintedNodeType();
	
	public static final NodeType DEFAULT = NORMAL;
	
	public static void init(){
		TYPES.put(arcLoc("normal"), NORMAL);
		TYPES.put(arcLoc("bright"), BRIGHT);
		TYPES.put(arcLoc("pale"), PALE);
		TYPES.put(arcLoc("eldritch"), ELDRITCH);
		TYPES.put(arcLoc("hungry"), HUNGRY);
		TYPES.put(arcLoc("tainted"), TAINTED);
		
		SPECIAL_TYPES.add(BRIGHT);
		SPECIAL_TYPES.add(PALE);
		SPECIAL_TYPES.add(ELDRITCH);
		SPECIAL_TYPES.add(HUNGRY);
	}
	
	public void tick(IWorld world, AuraView nodes, Node node){
		if(world.isRemote()){
			GogglePriority priority = GogglePriority.getGogglePriority();
			if(priority == GogglePriority.SHOW_NODE || priority == GogglePriority.SHOW_ASPECTS)
				world.addParticle(new NodeParticleData(node.nodeUniqueId(), node.type().texture(world, nodes, node), ArcanaParticles.NODE_PARTICLE.get()), node.getX(), node.getY(), node.getZ(), 0, 0, 0);
		}
	}
	
	public abstract ResourceLocation texture(IWorld world, AuraView nodes, Node node);
	public abstract Collection<ResourceLocation> textures();
	
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
			if(battery.findAspectInHolders(aspect) != null)
				battery.findAspectInHolders(aspect).insert(new AspectStack(aspect, amount), false);
			else{
				AspectCell cell = new AspectCell();
				cell.setCapacity(-1);
				cell.insert(new AspectStack(aspect, amount), false);
				battery.createCell(cell);
			}
		}
		// if the node is underwater, add 5-10 aqua
		if(world.getFluidState(location).isTagged(FluidTags.WATER)){
			Aspect aspect = Aspect.WATER;
			int amount = 5 + random.nextInt(6);
			if(battery.findAspectInHolders(aspect) != null)
				battery.findAspectInHolders(aspect).insert(new AspectStack(aspect, amount), false);
			else{
				AspectCell cell = new AspectCell();
				cell.insert(new AspectStack(aspect, amount), false);
				cell.setCapacity(-1);
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
	}
	
	public static class EldritchNodeType extends NodeType{
		
		public ResourceLocation texture(IWorld world, AuraView nodes, Node node){
			return arcLoc("nodes/eldritch_node");
		}
		
		public Collection<ResourceLocation> textures(){
			return Collections.singleton(arcLoc("nodes/eldritch_node"));
		}
	}
	
	public static class HungryNodeType extends NodeType{
		
		public ResourceLocation texture(IWorld world, AuraView nodes, Node node){
			return arcLoc("nodes/hungry_node");
		}
		
		public Collection<ResourceLocation> textures(){
			return Collections.singleton(arcLoc("nodes/hungry_node"));
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
			cell.insert(new AspectStack(Aspect.TAINT, 5 + random.nextInt(11)), false);
			handler.createCell(cell);
			return handler;
		}
	}
}