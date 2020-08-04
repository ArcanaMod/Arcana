package net.arcanamod.blocks;

import net.arcanamod.blocks.tiles.AspectCrystallizerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class AspectCrystallizerBlock extends Block{
	
	public AspectCrystallizerBlock(Properties properties){
		super(properties);
	}
	
	public boolean hasTileEntity(BlockState state){
		return true;
	}
	
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return new AspectCrystallizerTileEntity();
	}
}