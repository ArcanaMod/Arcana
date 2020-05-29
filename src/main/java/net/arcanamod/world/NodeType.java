package net.arcanamod.world;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.arcanamod.Arcana;
import net.minecraft.util.ResourceLocation;

import static net.arcanamod.Arcana.arcLoc;

// Although IDEA complains about class loading deadlock, this only occurs under specific conditions.
@SuppressWarnings("StaticInitializerReferencesSubClass")
public abstract class NodeType{
	
	// A diet registry, used for serialization and deserialization.
	public static final BiMap<ResourceLocation, NodeType> TYPES = HashBiMap.create(6);
	
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
		
	}
	
	public static class NormalNodeType extends NodeType{}
	public static class BrightNodeType extends NodeType{}
	public static class PaleNodeType extends NodeType{}
	public static class EldritchNodeType extends NodeType{}
	public static class HungryNodeType extends NodeType{}
	public static class TaintedNodeType extends NodeType{}
}