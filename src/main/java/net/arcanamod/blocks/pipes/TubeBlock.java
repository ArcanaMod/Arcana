package net.arcanamod.blocks.pipes;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.handlers.AspectHandlerCapability;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TubeBlock extends SixWayBlock{
	
	public TubeBlock(Properties properties){
		super(.1875f, properties);
		setDefaultState(this.stateContainer.getBaseState()
				.with(NORTH, Boolean.FALSE)
				.with(EAST, Boolean.FALSE)
				.with(SOUTH, Boolean.FALSE)
				.with(WEST, Boolean.FALSE)
				.with(UP, Boolean.FALSE)
				.with(DOWN, Boolean.FALSE));
	}
	
	private boolean isVisHolder(IBlockReader world, BlockPos pos){
		Block block = world.getBlockState(pos).getBlock();
		TileEntity tile = world.getTileEntity(pos);
		return (tile != null && tile.getCapability(AspectHandlerCapability.ASPECT_HANDLER).isPresent()) || block instanceof TubeBlock;
	}
	
	// Blockstate stuff
	
	public BlockState getStateForPlacement(BlockItemUseContext context){
		return this.makeConnections(context.getWorld(), context.getPos());
	}
	
	public BlockState makeConnections(IBlockReader world, BlockPos pos){
		return this.getDefaultState()
				.with(DOWN, isVisHolder(world, pos.down()))
				.with(UP, isVisHolder(world, pos.up()))
				.with(NORTH, isVisHolder(world, pos.north()))
				.with(EAST, isVisHolder(world, pos.east()))
				.with(SOUTH, isVisHolder(world, pos.south()))
				.with(WEST, isVisHolder(world, pos.west()));
	}
	
	/**
	 * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
	 * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	 * returns its solidified counterpart.
	 * Note that this method should ideally consider only the specific face passed in.
	 */
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos){
		boolean flag = isVisHolder(world, facingPos);
		return state.with(FACING_TO_PROPERTY_MAP.get(facing), flag);
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}
	
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type){
		return false;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state){
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return new TubeTileEntity();
	}
}