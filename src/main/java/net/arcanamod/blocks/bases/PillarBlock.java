package net.arcanamod.blocks.bases;

import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class PillarBlock extends Block {
	public static final BooleanProperty UP = BooleanProperty.create("up");
	public static final BooleanProperty DOWN = BooleanProperty.create("down");

	public PillarBlock(Properties properties) {
		super(properties);
		setDefaultState(stateContainer.getBaseState().with(UP, Boolean.FALSE).with(DOWN, Boolean.FALSE));
	}

	@Nonnull
	public BlockState getStateForPlacement(BlockItemUseContext context){
		BlockPos blockpos = context.getPos();
		World world = context.getWorld();
		return getDefaultState().with(UP, world.getBlockState(blockpos.up()).getBlock() instanceof PillarBlock).with(DOWN, world.getBlockState(blockpos.down()).getBlock() instanceof PillarBlock);
	}

	@Override
	public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return state.getBlock().equals(ArcanaBlocks.PRIDESTONE_PILLAR_COAL.get());
	}

	@Override
	public void updateNeighbors(BlockState stateIn, IWorld worldIn, BlockPos pos, int flags) {
		UpdatePillars(((World)worldIn),pos);
		super.updateNeighbors(stateIn, worldIn, pos, flags);
	}

	public void UpdatePillars(World worldIn, BlockPos pos)
	{
		if (worldIn.getBlockState(pos.down()).getBlock() instanceof PillarBlock)
			worldIn.setBlockState(pos.down(), worldIn.getBlockState(pos.down()).with(UP, worldIn.getBlockState(pos).getBlock() instanceof PillarBlock).with(DOWN, worldIn.getBlockState(pos.down().down()).getBlock() instanceof PillarBlock));
		if (worldIn.getBlockState(pos.up()).getBlock() instanceof PillarBlock)
			worldIn.setBlockState(pos.up(), worldIn.getBlockState(pos.up()).with(UP, worldIn.getBlockState(pos.up().up()).getBlock() instanceof PillarBlock).with(DOWN, worldIn.getBlockState(pos).getBlock() instanceof PillarBlock));
	}

	@Override
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
		UpdatePillars(worldIn,pos);
		super.onExplosionDestroy(worldIn, pos, explosionIn);
	}

	@Override
	public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
		UpdatePillars(((World)worldIn),pos);
		super.onPlayerDestroy(worldIn, pos, state);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(UP);
		builder.add(DOWN);
		super.fillStateContainer(builder);
	}
}
