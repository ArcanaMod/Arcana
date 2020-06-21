package net.arcanamod.world.impl;

import net.arcanamod.world.Node;
import net.arcanamod.world.AuraChunk;
import net.arcanamod.world.NodeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

public class AuraChunkImpl implements AuraChunk{
	
	Collection<Node> nodes = new ArrayList<>();
	int taint;
	
	public void addNode(Node node){
		nodes.add(node);
	}
	
	public void removeNode(Node node){
		nodes.remove(node);
	}
	
	public void setNodes(Collection<Node> nodes){
		this.nodes = nodes;
	}
	
	public Collection<Node> getNodes(){
		return new ArrayList<>(nodes);
	}
	
	public Collection<Node> getNodesWithinAABB(AxisAlignedBB bounds){
		Collection<Node> set = new ArrayList<>();
		for(Node node : getNodes())
			if(bounds.contains(node.getX(), node.getY(), node.getZ()))
				set.add(node);
		return set;
	}
	
	public Collection<Node> getNodesOfType(NodeType type){
		Collection<Node> set = new ArrayList<>();
		for(Node node : getNodes())
			if(node.type().equals(type))
				set.add(node);
		return set;
	}
	
	public Collection<Node> getNodesOfTypeWithinAABB(NodeType type, AxisAlignedBB bounds){
		Collection<Node> set = new ArrayList<>();
		for(Node node : getNodes())
			if(node.type().equals(type))
				if(bounds.contains(node.getX(), node.getY(), node.getZ()))
					set.add(node);
		return set;
	}
	
	public int getTaintLevel(){
		return taint;
	}
	
	public void addTaint(int amount){
		taint += amount;
		taint = Math.max(taint, 0);
	}
	
	public void setTaint(int newTaint){
		taint = Math.max(newTaint, 0);
	}
	
	public CompoundNBT serializeNBT(){
		// Just make a list of CompoundNBTs from each node.
		CompoundNBT compound = new CompoundNBT();
		ListNBT data = new ListNBT();
		for(Node node : nodes)
			data.add(node.serializeNBT());
		compound.put("nodes", data);
		compound.putInt("taint", taint);
		return compound;
	}
	
	public void deserializeNBT(@Nonnull CompoundNBT data){
		// Go through the list and deserialize each entry
		ListNBT list = data.getList("nodes", Constants.NBT.TAG_COMPOUND);
		Collection<Node> nodeSet = new ArrayList<>(list.size());
		for(INBT nodeNBT : list)
			if(nodeNBT instanceof CompoundNBT)
				nodeSet.add(Node.fromNBT((CompoundNBT)nodeNBT));
		nodes = nodeSet;
		taint = data.getInt("taint");
	}
}
