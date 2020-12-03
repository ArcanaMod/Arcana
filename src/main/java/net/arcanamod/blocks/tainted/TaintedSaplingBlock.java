package net.arcanamod.blocks.tainted;

import net.arcanamod.blocks.Taint;
import net.minecraft.block.Block;
import net.minecraft.block.SaplingBlock;

public class TaintedSaplingBlock extends SaplingBlock {
	public TaintedSaplingBlock(Block parent) {
		super(Taint.taintedTreeOf((SaplingBlock) parent), Block.Properties.from(parent));
		Taint.addTaintMapping(parent, this);
	}
}
