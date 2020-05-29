package net.arcanamod.world.impl;

import net.arcanamod.world.Node;
import net.arcanamod.world.NodeChunk;
import net.arcanamod.world.NodeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class NodeChunkImpl implements NodeChunk{
	
	Set<Node> nodes = new HashSet<>();
	
	public Set<Node> getNodes(){
		return new HashSet<>(nodes);
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
	
	public CompoundNBT serializeNBT(){
		// Just make a list of CompoundNBTs from each node.
		CompoundNBT compound = new CompoundNBT();
		ListNBT data = new ListNBT();
		for(Node node : nodes)
			data.add(node.serializeNBT());
		compound.put("nodes", data);
		return compound;
	}
	
	public void deserializeNBT(@Nonnull CompoundNBT data){
		// Go through the list and deserialize each entry
		ListNBT list = data.getList("nodes", Constants.NBT.TAG_COMPOUND);
		Set<Node> nodeSet = new HashSet<>(list.size());
		for(INBT nodeNBT : list)
			if(nodeNBT instanceof CompoundNBT)
				nodeSet.add(Node.fromNBT((CompoundNBT)nodeNBT));
		nodes = nodeSet;
	}
}
