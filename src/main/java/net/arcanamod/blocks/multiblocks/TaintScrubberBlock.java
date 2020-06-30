package net.arcanamod.blocks.multiblocks;

import net.arcanamod.blocks.Taint;
import net.arcanamod.blocks.tiles.TaintScrubberTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static net.arcanamod.blocks.DelegatingBlock.switchBlock;

public class TaintScrubberBlock extends Block implements ITaintScrubberExtension {
	public static final BooleanProperty SUPPORTED = BooleanProperty.create("supported"); // false by default

	public TaintScrubberBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TaintScrubberTileEntity();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	public BlockState getStateForPlacement(BlockItemUseContext context){
		return super.getStateForPlacement(context).with(SUPPORTED,false);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		super.fillStateContainer(builder);
		builder.add(SUPPORTED);
	}

	/**
	 * It checks if the extension is in right place
	 *
	 * @param world World
	 * @param pos   Position of extension
	 * @return isValidConnection
	 */
	@Override
	public boolean isValidConnection(World world, BlockPos pos) {
		return true;
	}

	/**
	 * It is performed if this block is found by TaintScrubber.
	 *
	 * @param world World
	 * @param pos   Position of extension
	 */
	@Override
	public void sendUpdate(World world, BlockPos pos) {

	}

	/**
	 * Runs extension action.
	 *
	 * @param world World
	 * @param pos   Position of extension
	 */
	@Override
	public void run(World world, BlockPos pos, CompoundNBT compound) {
		for (int i = 0; i < compound.getInt("speed")+1; i++) {
			// pick a block within a 4x6x4 area
			// If this block is air, stop. If this block doesn't have a tainted form, re-roll.
			// Do this up to 5 times.
			Block tainted = null; //TODO: rename tainted to pure
			BlockPos taintingPos = pos;
			int iter = 0;
			while(tainted == null && iter < 5){
				int r = compound.getInt("range");
				taintingPos = pos.north(RANDOM.nextInt(r+1) - (r/2)).west(RANDOM.nextInt(r+1) - (r/2)).up(RANDOM.nextInt(r+1) - (r/2));
				tainted = world.getBlockState(taintingPos).getBlock();
				if(tainted.isAir(world.getBlockState(taintingPos), world, taintingPos)){
					tainted = null;
					break;
				}
				tainted = Taint.getPureOfBlock(tainted);
				iter++;
			}
			// Replace it with its tainted form if found.
			if(tainted != null){
				BlockState taintedState = switchBlock(world.getBlockState(taintingPos), tainted);
				world.setBlockState(taintingPos, taintedState);
			}
		}
	}

	@Override
	public CompoundNBT getShareableData(CompoundNBT compound) {
		return compound;
	}
}
