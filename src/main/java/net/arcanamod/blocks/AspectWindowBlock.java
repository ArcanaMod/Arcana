package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.tiles.AspectWindowTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectWindowBlock extends AspectTubeBlock{
	
	protected AspectWindowBlock(Properties properties){
		super(properties);
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return new AspectWindowTileEntity();
	}
	
	public boolean hasComparatorInputOverride(BlockState state){
		return true;
	}
	
	public int getComparatorInputOverride(BlockState block, World world, BlockPos pos){
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof AspectWindowTileEntity){
			AspectWindowTileEntity window = (AspectWindowTileEntity)te;
			int elapsed = (int)(te.getWorld().getGameTime() - window.getLastTransferTime());
			return elapsed > 12 ? 0 : 15;
		}
		return 0;
	}
}