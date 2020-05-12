package net.arcanamod.blocks.bases;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class HorizontalWaterloggableBlock extends WaterloggableBlock{
	
	public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
	
	public HorizontalWaterloggableBlock(Properties properties){
		super(properties);
		setDefaultState(stateContainer.getBaseState().with(WATERLOGGED, Boolean.FALSE).with(HORIZONTAL_FACING, Direction.NORTH));
	}
	
	@Nonnull
	public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context){
		return super.getStateForPlacement(context).with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing());
	}
	
	protected void fillStateContainer(@Nonnull StateContainer.Builder<Block, BlockState> builder){
		super.fillStateContainer(builder);
		builder.add(HORIZONTAL_FACING);
	}
	
	@Nonnull
	public BlockState rotate(@Nonnull BlockState state, @Nonnull Rotation rot) {
		return state.with(HORIZONTAL_FACING, rot.rotate(state.get(HORIZONTAL_FACING)));
	}
	
	@Nonnull
	public BlockState mirror(@Nonnull BlockState state, @Nonnull Mirror mirrorIn){
		return state.rotate(mirrorIn.toRotation(state.get(HORIZONTAL_FACING)));
	}
}
