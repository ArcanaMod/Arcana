package net.arcanamod.blocks.multiblocks.taint_scrubber;

import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class BoosterTaintScrubberExtensionBlock extends Block implements ITaintScrubberExtension{
	
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	
	public BoosterTaintScrubberExtensionBlock(Block.Properties properties){
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
	}
	
	@Override
	public boolean isValidConnection(World world, BlockPos pos){
		if(world.getBlockState(pos.north()).getBlock().equals(ArcanaBlocks.TAINT_SCRUBBER_MK2.get()))
			return true;
		if(world.getBlockState(pos.south()).getBlock().equals(ArcanaBlocks.TAINT_SCRUBBER_MK2.get()))
			return true;
		if(world.getBlockState(pos.west()).getBlock().equals(ArcanaBlocks.TAINT_SCRUBBER_MK2.get()))
			return true;
		return world.getBlockState(pos.east()).getBlock().equals(ArcanaBlocks.TAINT_SCRUBBER_MK2.get());
	}
	
	/**
	 * It is performed if this block is found by TaintScrubber.
	 *
	 * @param world
	 * 		World
	 * @param pos
	 * 		Position of extension
	 */
	@Override
	public void sendUpdate(World world, BlockPos pos){
	
	}
	
	@Override
	public void run(World world, BlockPos pos, CompoundNBT compound){}
	
	@Override
	public CompoundNBT getShareableData(CompoundNBT compound){
		compound.putInt("speed", compound.getInt("speed") + 1);
		return compound;
	}
	
	public BlockState getStateForPlacement(BlockItemUseContext context){
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
	}
	
	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	@Nonnull
	public BlockState rotate(BlockState state, Rotation rot){
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}
	
	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	@Nonnull
	public BlockState mirror(BlockState state, Mirror mirrorIn){
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
	
	protected void fillStateContainer(@Nonnull StateContainer.Builder<Block, BlockState> builder){
		super.fillStateContainer(builder);
		builder.add(FACING);
	}
}