package net.arcanamod.blocks;

import net.arcanamod.blocks.tiles.WardenedBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class WardenedBlock extends Block {
	public WardenedBlock(Block.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new WardenedBlockTileEntity();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
}
