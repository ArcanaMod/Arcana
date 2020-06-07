package net.arcanamod.world;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.arcanamod.aspects.Aspect;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static net.arcanamod.Arcana.arcLoc;

// Although IDEA complains about class loading deadlock, this only occurs under specific conditions.
// Handles type-specific things, such as behaviour and vis generation rates.
@SuppressWarnings("StaticInitializerReferencesSubClass")
public abstract class NodeType{
	
	// A diet registry, used for serialization and deserialization.
	public static final BiMap<ResourceLocation, NodeType> TYPES = HashBiMap.create(6);
	public static final Set<NodeType> GENERATED_TYPES = new HashSet<>(5);
	
	public static final NodeType
			NORMAL = new NormalNodeType(),
			BRIGHT = new BrightNodeType(),
			PALE = new PaleNodeType(),
			ELDRITCH = new EldritchNodeType(),
			HUNGRY = new HungryNodeType(),
			TAINTED = new TaintedNodeType();
	
	public static void init(){
		TYPES.put(arcLoc("normal"), NORMAL);
		TYPES.put(arcLoc("bright"), BRIGHT);
		TYPES.put(arcLoc("pale"), PALE);
		TYPES.put(arcLoc("eldritch"), ELDRITCH);
		TYPES.put(arcLoc("hungry"), HUNGRY);
		TYPES.put(arcLoc("tainted"), TAINTED);
		
		GENERATED_TYPES.add(NORMAL);
		GENERATED_TYPES.add(BRIGHT);
		GENERATED_TYPES.add(PALE);
		GENERATED_TYPES.add(ELDRITCH);
		GENERATED_TYPES.add(HUNGRY);
	}
	
	public abstract void tick(IWorld world, INodeView nodes, Node node);
	
	/**
	 * The aspects that a new node of this type will have.
	 */
	// default impl should handle normal nodes; maybe bright and pale ones.
	public Reference2IntMap<Aspect> genNodeAspects(BlockPos location, IWorld world, Random random){
		return new Reference2IntOpenHashMap<>();
	}
	
	public static class NormalNodeType extends NodeType{
		public void tick(IWorld world, INodeView nodes, Node node){
			if(world.isRemote()){
				Random random = world.getRandom();
				world.addParticle(ParticleTypes.CLOUD, node.getX(), node.getY(), node.getZ(), random.nextGaussian() / 6, random.nextGaussian() / 6, random.nextGaussian() / 6);
			}
		}
	}
	
	public static class BrightNodeType extends NodeType{
		public void tick(IWorld world, INodeView nodes, Node node){
			if(world.isRemote()){
				Random random = world.getRandom();
				world.addParticle(ParticleTypes.ENCHANTED_HIT, node.getX(), node.getY(), node.getZ(), random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
			}
		}
	}
	
	public static class PaleNodeType extends NodeType{
		public void tick(IWorld world, INodeView nodes, Node node){
			if(world.isRemote()){
				Random random = world.getRandom();
				world.addParticle(ParticleTypes.SMOKE, node.getX(), node.getY(), node.getZ(), random.nextGaussian() / 6, random.nextGaussian() / 6, random.nextGaussian() / 6);
			}
		}
	}
	
	public static class EldritchNodeType extends NodeType{
		public void tick(IWorld world, INodeView nodes, Node node){
			if(world.isRemote()){
				Random random = world.getRandom();
				world.addParticle(ParticleTypes.END_ROD, node.getX(), node.getY(), node.getZ(), random.nextGaussian() / 6, random.nextGaussian() / 6, random.nextGaussian() / 6);
			}
		}
	}
	
	public static class HungryNodeType extends NodeType{
		public void tick(IWorld world, INodeView nodes, Node node){
			if(world.isRemote()){
				Random random = world.getRandom();
				world.addParticle(ParticleTypes.PORTAL, node.getX(), node.getY(), node.getZ(), random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
				world.addParticle(ParticleTypes.PORTAL, node.getX(), node.getY(), node.getZ(), random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
			}
		}
	}
	
	public static class TaintedNodeType extends NodeType{
		public void tick(IWorld world, INodeView nodes, Node node){
			if(world.isRemote()){
				Random random = world.getRandom();
				world.addParticle(ParticleTypes.BUBBLE_POP, node.getX(), node.getY(), node.getZ(), random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
			}
		}
	}
}