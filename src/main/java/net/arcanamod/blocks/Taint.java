package net.arcanamod.blocks;

import net.minecraft.block.Block;

import java.util.HashMap;
import java.util.Map;

public class Taint{
	
	private static final Map<Block, Block> taintMap = new HashMap<>();
	
	public static Block getTaintedOfBlock(Block block){
		return taintMap.get(block);
	}
	
	public static void addTaintMapping(Block original, Block tainted){
		taintMap.put(original, tainted);
	}
}