package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlembicBlock extends Block{
	
	protected static final VoxelShape SHAPE = VoxelShapes.or(
			makeCuboidShape(1, 1, 1, 15, 15, 15),
			makeCuboidShape(0, 2, 0, 16, 4, 16),
			makeCuboidShape(0, 12, 0, 16, 14, 16),
			makeCuboidShape(4, 0, 4, 12, 2, 12),
			makeCuboidShape(4, 14, 4, 12, 16, 12)
	).simplify();
	
	public AlembicBlock(Properties properties){
		super(properties);
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return SHAPE;
	}
}