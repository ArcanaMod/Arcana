package net.arcanamod.blocks.tainted;

import net.arcanamod.systems.taint.Taint;
import net.minecraft.block.Block;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.trees.Tree;

public class TaintedSaplingBlock extends SaplingBlock {
	
	public TaintedSaplingBlock(Block parent){
		super(/*Taint.taintedTreeOf((SaplingBlock) parent)*/null, Block.Properties.from(parent));
		Taint.addTaintMapping(parent, this);
	}
	
	public TaintedSaplingBlock(Block parent, Tree tree, Properties properties){
		super(tree, properties);
		Taint.addTaintMapping(parent, this);
	}
}
