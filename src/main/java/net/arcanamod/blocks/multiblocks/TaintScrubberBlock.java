package net.arcanamod.blocks.multiblocks;

import net.minecraft.block.Block;

public class TaintScrubberBlock extends Block {
	private Level level;

	public TaintScrubberBlock(Properties properties, Level level) {
		super(properties);
		this.level = level;
	}

	public enum Level {
		MK1,
		MK2
	}
}
