package net.arcanamod.blocks.multiblocks;

import net.arcanamod.blocks.Taint;
import net.arcanamod.blocks.tiles.TaintScrubberTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.arguments.ParticleArgument;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
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
		int rh = compound.getInt("h_range");
		int rv = compound.getInt("v_range");
		for (int i = 0; i < compound.getInt("speed")+(rv/32)+1; i++) {
			// pick a block within a rh x rh x rv area
			// If this block is air, stop. If this block doesn't have a tainted form, re-roll.
			// Do this up to 5 times.
			Block dead = null;
			BlockPos taintingPos = pos;
			int iter = 0;
			while(dead == null && iter < 5){
				taintingPos = pos.north(RANDOM.nextInt(rh+1) - (rh/2)).west(RANDOM.nextInt(rh+1) - (rh/2)).up(RANDOM.nextInt(rv+1) - (rv/2));
				dead = world.getBlockState(taintingPos).getBlock();
				if(dead.isAir(world.getBlockState(taintingPos), world, taintingPos)){
					dead = null;
					break;
				}
				dead = Taint.getDeadOfBlock(Taint.getPureOfBlock(dead));
				if (compound.getBoolean("silk_touch"))
					dead = Taint.getPureOfBlock(dead);
				iter++;
			}
			// Replace it with its dead form if found.
			if(dead != null){
				BlockState deadState = switchBlock(world.getBlockState(taintingPos), dead);
				world.setBlockState(taintingPos, deadState);
				if(dead.isAir(world.getBlockState(taintingPos), world, taintingPos)){
					int rnd = RANDOM.nextInt(9)+4;
					for (int j = 0; j < rnd; j++) {
						world.addParticle(
								new BlockParticleData(ParticleTypes.FALLING_DUST, Blocks.BLACK_CONCRETE_POWDER.getDefaultState()),
								taintingPos.getX()+0.5f+((RANDOM.nextInt(9)-4)/10f),taintingPos.getY()+0.5f+((RANDOM.nextInt(9)-4)/10f),taintingPos.getZ()+0.5f+((RANDOM.nextInt(9)-4)/10f),
								0.1f,0.1f,0.1f
						); // Ash Particle if block is destroyed
					}
				}
				world.notifyBlockUpdate(taintingPos,deadState,deadState,3); //AntiGhostBlock
			}
		}
	}

	@Override
	public CompoundNBT getShareableData(CompoundNBT compound) {
		return compound;
	}
}
