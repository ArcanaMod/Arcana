package net.arcanamod.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Stores nodes that are inside of this chunk. May also store taint or aura if we make them chunk stuff.
 */
// Will be stored as a capability and do the actual work of storing nodes.
public interface NodeChunk{
	
	/**
	 * Returns a set containing all nodes in this chunk. Changes to this set are *not* reflected in-world.
	 *
	 * @return A set containing all nodes in this chunk.
	 */
	Set<Node> getNodes();
	
	/**
	 * Returns a set containing all nodes in this chunk within the specified bounds.
	 * Changes to this set are *not* reflected in-world.
	 *
	 * @return A set containing all nodes in this chunk within bounds.
	 */
	Set<Node> getNodesWithinAABB(AxisAlignedBB bounds);
	
	/**
	 * Returns a set containing all nodes in this chunk with the specified node type.
	 * Changes to this set are *not* reflected in-world.
	 *
	 * @return A set containing all nodes in this chunk of the specified type.
	 */
	Set<Node> getNodesOfType(NodeType type);
	
	/**
	 * Returns a set containing all nodes in this chunk within the specified bounds, with the specified node type.
	 * Changes to this set are *not* reflected in-world.
	 *
	 * @return A set containing all nodes in this chunk within bounds of the specified type.
	 */
	Set<Node> getNodesOfTypeWithinAABB(NodeType type, AxisAlignedBB bounds);
	
	/**
	 * Returns a set containing all nodes in this chunk, except for the specified node.
	 * Changes to this set are *not* reflected in-world.
	 *
	 * @return A set containing all nodes in this chunk, excluding a specified node.
	 */
	default Set<Node> getNodesExcluding(Node excluded){
		Set<Node> all = getNodes();
		all.remove(excluded);
		return all;
	}
	
	/**
	 * Returns a set containing all nodes in this chunk within the specified bounds,
	 * except for the specified node. Changes to this set are *not* reflected in-world.
	 *
	 * @return A set containing all nodes in this chunk within bounds, excluding a specified node.
	 */
	default Set<Node> getNodesWithinAABBExcluding(AxisAlignedBB bounds, Node excluded){
		Set<Node> all = getNodesWithinAABB(bounds);
		all.remove(excluded);
		return all;
	}
	
	/**
	 * Returns a set containing all nodes in this chunk within the specified bounds, with the specified node
	 * type, except for the specified node. Changes to this set are *not* reflected in-world.
	 *
	 * @return A set containing all nodes in this chunk within bounds of the specified type, excluding a specified node.
	 */
	default Set<Node> getNodesOfTypeExcluding(NodeType type, Node excluded){
		Set<Node> all = getNodesOfType(type);
		all.remove(excluded);
		return all;
	}
	
	/**
	 * Returns a set containing all nodes in this chunk within the specified bounds, with the specified node type,
	 * except for the specified node. Changes to this set are *not* reflected in-world.
	 *
	 * @return A set containing all nodes in this chunk within bounds of the specified type, excluding a specified node.
	 */
	default Set<Node> getNodesOfTypeWithinAABBExcluding(NodeType type, AxisAlignedBB bounds, Node excluded){
		Set<Node> all = getNodesOfTypeWithinAABB(type, bounds);
		all.remove(excluded);
		return all;
	}
	
	default CompoundNBT serializeNBT(){
		return new CompoundNBT();
	}
	
	default void deserializeNBT(@Nonnull CompoundNBT data){
	}
}