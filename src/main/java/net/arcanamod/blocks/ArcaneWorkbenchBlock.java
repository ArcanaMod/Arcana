package net.arcanamod.blocks;

import net.arcanamod.blocks.tiles.ArcaneWorkbenchTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class ArcaneWorkbenchBlock extends Block {
	public ArcaneWorkbenchBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ArcaneWorkbenchTileEntity();
	}
}
