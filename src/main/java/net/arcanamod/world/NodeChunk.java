package net.arcanamod.world;

import net.arcanamod.world.impl.NodeChunkCapability;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

/**
 * Stores nodes that are inside of this chunk. May also store taint or aura if we make them chunk stuff.
 */
// Will be stored as a capability and do the actual work of storing nodes.
public interface NodeChunk{
	
	/**
	 * Adds a node to this chunk.
	 *
	 * @param node
	 * 		The node to be added.
	 */
	void addNode(Node node);
	
	/**
	 * Adds a collection of nodes to this chunk. By default, iterates through the iterable and adds each node.
	 *
	 * @param nodes
	 * 		The nodes to be added.
	 */
	default void addNodes(Iterable<Node> nodes){
		for(Node node : nodes)
			addNode(node);
	}
	
	/**
	 * Sets this chunk's node set. Modifications to the set passed in are reflected in world.
	 *
	 * @param nodes
	 * 		The set of nodes to set.
	 */
	void setNodes(Set<Node> nodes);
	
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
	
	CompoundNBT serializeNBT();
	
	void deserializeNBT(@Nonnull CompoundNBT data);
	
	@SuppressWarnings("ConstantConditions")
	@Nullable
	static NodeChunk getFrom(@Nonnull ICapabilityProvider holder){
		return holder.getCapability(NodeChunkCapability.NODE_CHUNK_CAPABILITY, null).orElse(null);
	}
}