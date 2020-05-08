package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.bases.WaterloggableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockAspectBookshelf extends WaterloggableBlock{
	
	public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final IntegerProperty LEVEL_0_8 = BlockStateProperties.LEVEL_0_8;
	
	public VoxelShape SHAPE_NORTH = Block.makeCuboidShape(0, 0, 7, 16, 16, 16);
	public VoxelShape SHAPE_SOUTH = Block.makeCuboidShape(0, 0, 0, 16, 16, 9);
	public VoxelShape SHAPE_EAST = Block.makeCuboidShape(0, 0, 0, 9, 16, 16);
	public VoxelShape SHAPE_WEST = Block.makeCuboidShape(7, 0, 0, 16, 16, 16);
	
	public BlockAspectBookshelf(Properties properties){
		super(properties);
		setDefaultState(stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(WATERLOGGED, Boolean.FALSE).with(LEVEL_0_8, 0));
	}
	
	public BlockState getStateForPlacement(BlockItemUseContext context){
		return super.getStateForPlacement(context).with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(LEVEL_0_8,0);
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		builder.add(HORIZONTAL_FACING, WATERLOGGED, LEVEL_0_8);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		if(state.get(HORIZONTAL_FACING) == Direction.NORTH)
			return SHAPE_NORTH;
		if(state.get(HORIZONTAL_FACING) == Direction.SOUTH)
			return SHAPE_SOUTH;
		if(state.get(HORIZONTAL_FACING) == Direction.EAST)
			return SHAPE_EAST;
		else
			return SHAPE_WEST;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context){
		return getShape(state, world, pos, context);
	}
	
	public BlockState rotate(BlockState state, Rotation rot){
		return state.with(HORIZONTAL_FACING, rot.rotate(state.get(HORIZONTAL_FACING)));
	}
	
	public BlockState mirror(BlockState state, Mirror mirrorIn){
		return state.rotate(mirrorIn.toRotation(state.get(HORIZONTAL_FACING)));
	}
}
