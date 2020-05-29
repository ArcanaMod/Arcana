package net.arcanamod.world.impl;

import net.arcanamod.world.Node;
import net.arcanamod.world.NodeChunk;
import net.arcanamod.world.NodeType;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Set;

public class NodeChunkImpl implements NodeChunk{
	
	public Set<Node> getNodes(){
		return null;
	}
	
	public Set<Node> getNodesWithinAABB(AxisAlignedBB bounds){
		return null;
	}
	
	public Set<Node> getNodesOfType(NodeType type){
		return null;
	}
	
	public Set<Node> getNodesOfTypeWithinAABB(NodeType type, AxisAlignedBB bounds){
		return null;
	}
}
