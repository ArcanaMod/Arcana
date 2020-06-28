package net.arcanamod.blocks.tainted;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.Taint;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class TaintedGrassPathBlock extends Block implements GroupedBlock {
	public static final BooleanProperty UNTAINTED = Taint.UNTAINTED;

	public TaintedGrassPathBlock(Block parent){
		super(Properties.from(parent));
		Taint.addTaintMapping(parent, this);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		super.fillStateContainer(builder);
		builder.add(UNTAINTED);
	}

	public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld world, BlockPos pos1, BlockPos pos2, Hand hand){
		return super.getStateForPlacement(state, facing, state2, world, pos1, pos2, hand).with(UNTAINTED, true);
	}

	@Nullable
	@Override
	public ItemGroup getGroup(){
		return Arcana.TAINT;
	}

	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	public boolean isTransparent(BlockState state) {
		return true;
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState placement = super.getStateForPlacement(context);
		return placement != null ? !this.getDefaultState().isValidPosition(context.getWorld(), context.getPos()) ? Block.nudgeEntitiesWithNewState(this.getDefaultState(), Blocks.DIRT.getDefaultState(), context.getWorld(), context.getPos()).with(UNTAINTED, true) : super.getStateForPlacement(context).with(UNTAINTED, true) : null;
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
	 * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	 * returns its solidified counterpart.
	 * Note that this method should ideally consider only the specific face passed in.
	 */
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing == Direction.UP && !stateIn.isValidPosition(worldIn, currentPos)) {
			//worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
			((World)worldIn).setBlockState(currentPos, nudgeEntitiesWithNewState(stateIn, ArcanaBlocks.TAINTED_SOIL.get().getDefaultState().with(Taint.UNTAINTED,false), (World) worldIn, currentPos));
		}

		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		Taint.tickTaintedBlock(state, worldIn, pos, rand);
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockState blockstate = worldIn.getBlockState(pos.up());
		return !blockstate.getMaterial().isSolid() || blockstate.getBlock() instanceof FenceGateBlock;
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean isViewBlocking(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return true;
	}
}
