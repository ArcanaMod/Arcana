package net.arcanamod.world;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.arcanamod.aspects.Aspect;
import net.minecraft.dispenser.IPosition;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

// implements position for BlockPos constructor convenience
public class Node implements IPosition{
	
	Reference2IntMap<Aspect> aspects;
	NodeType type;
	double x, y, z;
	
	public Node(Reference2IntMap<Aspect> aspects, NodeType type){
		this.aspects = aspects;
		this.type = type;
	}
	
	// might as well pick the fast version
	public Reference2IntMap<Aspect> aspects(){
		return aspects;
	}
	
	public NodeType type(){
		return type;
	}
	
	public CompoundNBT serializeNBT(){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("type", NodeType.TYPES.inverse().get(type()).toString());
		CompoundNBT aspectsNBT = new CompoundNBT();
		aspects().forEach((aspect, integer) -> aspectsNBT.putInt(aspect.name(), integer));
		nbt.put("aspects", aspectsNBT);
		return nbt;
	}
	
	public static Node fromNBT(CompoundNBT nbt){
		Reference2IntMap<Aspect> aspects = new Reference2IntOpenHashMap<>();
		CompoundNBT aspectsNBT = nbt.getCompound("aspects");
		for(String s : aspectsNBT.keySet())
			aspects.put(Aspect.valueOf(s), aspectsNBT.getInt(s));
		NodeType type = NodeType.TYPES.get(new ResourceLocation(nbt.getString("type")));
		return new Node(aspects, type);
	}
	
	/**
	 * Implementations still need to override hashCode. This version hashes the node's
	 * position, type, and aspects.
	 *
	 * @return A default hashcode implementation for nodes.
	 */
	public int hashCode(){
		return Objects.hash(getX(), getY(), getZ(), type(), aspects());
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getZ(){
		return z;
	}
}