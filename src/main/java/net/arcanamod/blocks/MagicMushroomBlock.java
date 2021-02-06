package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MagicMushroomBlock extends BushBlock{
	
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2, 0, 2, 14, 13, 14);
	
	public MagicMushroomBlock(Properties properties){
		super(properties);
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return SHAPE;
	}
}