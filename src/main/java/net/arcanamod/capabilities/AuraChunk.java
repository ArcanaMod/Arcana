package net.arcanamod.capabilities;

import net.arcanamod.world.Node;
import net.arcanamod.world.NodeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Allows access to this chunk's nodes and taint level.
 */
public interface AuraChunk{
	
	/**
	 * Adds a node to this chunk.
	 *
	 * @param node
	 * 		The node to be added.
	 */
	void addNode(Node node);
	
	/**
	 * Removes a node from this chunk. Has no effect if the node is not in this chunk.
	 *
	 * @param node
	 * 		The node to be removed.
	 */
	void removeNode(Node node);
	
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
	void setNodes(Collection<Node> nodes);
	
	/**
	 * Returns a set containing all nodes in this chunk. Changes to this set are *not* reflected in-world.
	 *
	 * @return A set containing all nodes in this chunk.
	 */
	Collection<Node> getNodes();
	
	/**
	 * Returns a set containing all nodes in this chunk within the specified bounds.
	 * Changes to this set are *not* reflected in-world.
	 *
	 * @return A set containing all nodes in this chunk within bounds.
	 */
	Collection<Node> getNodesWithinAABB(AxisAlignedBB bounds);
	
	/**
	 * Returns a set containing all nodes in this chunk with the specified node type.
	 * Changes to this set are *not* reflected in-world.
	 *
	 * @return A set containing all nodes in this chunk of the specified type.
	 */
	Collection<Node> getNodesOfType(NodeType type);
	
	/**
	 * Returns a set containing all nodes in this chunk within the specified bounds, with the specified node type.
	 * Changes to this set are *not* reflected in-world.
	 *
	 * @return A set containing all nodes in this chunk within bounds of the specified type.
	 */
	Collection<Node> getNodesOfTypeWithinAABB(NodeType type, AxisAlignedBB bounds);
	
	float getFluxLevel();
	
	void addFlux(float amount);
	
	void setFlux(float newFlux);
	
	/**
	 * Returns a set containing all nodes in this chunk, except for the specified node.
	 * Changes to this set are *not* reflected in-world.
	 *
	 * @return A set containing all nodes in this chunk, excluding a specified node.
	 */
	default Collection<Node> getNodesExcluding(Node excluded){
		Collection<Node> all = getNodes();
		all.remove(excluded);
		return all;
	}
	
	/**
	 * Returns a set containing all nodes in this chunk within the specified bounds,
	 * except for the specified node. Changes to this set are *not* reflected in-world.
	 *
	 * @return A set containing all nodes in this chunk within bounds, excluding a specified node.
	 */
	default Collection<Node> getNodesWithinAABBExcluding(AxisAlignedBB bounds, Node excluded){
		Collection<Node> all = getNodesWithinAABB(bounds);
		all.remove(excluded);
		return all;
	}
	
	/**
	 * Returns a set containing all nodes in this chunk within the specified bounds, with the specified node
	 * type, except for the specified node. Changes to this set are *not* reflected in-world.
	 *
	 * @return A set containing all nodes in this chunk within bounds of the specified type, excluding a specified node.
	 */
	default Collection<Node> getNodesOfTypeExcluding(NodeType type, Node excluded){
		Collection<Node> all = getNodesOfType(type);
		all.remove(excluded);
		return all;
	}
	
	/**
	 * Returns a set containing all nodes in this chunk within the specified bounds, with the specified node type,
	 * except for the specified node. Changes to this set are *not* reflected in-world.
	 *
	 * @return A set containing all nodes in this chunk within bounds of the specified type, excluding a specified node.
	 */
	default Collection<Node> getNodesOfTypeWithinAABBExcluding(NodeType type, AxisAlignedBB bounds, Node excluded){
		Collection<Node> all = getNodesOfTypeWithinAABB(type, bounds);
		all.remove(excluded);
		return all;
	}
	
	CompoundNBT serializeNBT();
	
	void deserializeNBT(@Nonnull CompoundNBT data);
	
	@SuppressWarnings("ConstantConditions")
	@Nullable
	static AuraChunk getFrom(@Nonnull ICapabilityProvider holder){
		return holder.getCapability(AuraChunkCapability.NODE_CHUNK_CAPABILITY, null).orElse(null);
	}
}