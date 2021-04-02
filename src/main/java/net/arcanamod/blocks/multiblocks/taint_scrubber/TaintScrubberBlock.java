package net.arcanamod.blocks.multiblocks.taint_scrubber;

import net.arcanamod.aspects.AspectBattery;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.aspects.VisShareable;
import net.arcanamod.systems.taint.Taint;
import net.arcanamod.blocks.tiles.TaintScrubberTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.arcanamod.blocks.DelegatingBlock.switchBlock;

public class TaintScrubberBlock extends Block implements ITaintScrubberExtension{
	
	public static final BooleanProperty SUPPORTED = BooleanProperty.create("supported"); // false by default
	
	public TaintScrubberBlock(Properties properties){
		super(properties);
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return new TaintScrubberTileEntity();
	}
	
	@Override
	public boolean hasTileEntity(BlockState state){
		return true;
	}
	
	public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context){
		BlockState state = super.getStateForPlacement(context);
		return state != null ? state.with(SUPPORTED, false) : null;
	}
	
	protected void fillStateContainer(@Nonnull StateContainer.Builder<Block, BlockState> builder){
		super.fillStateContainer(builder);
		builder.add(SUPPORTED);
	}
	
	@Override
	public boolean isValidConnection(World world, BlockPos pos){
		return true;
	}
	
	@Override
	public void sendUpdate(World world, BlockPos pos){}
	
	@Override
	public void run(World world, BlockPos pos, CompoundNBT compound){
		// TODO: don't use NBT for this, just have methods for getting range and speed from extensions directly
		// pick the highest range, and add speeds.
		int rh = compound.getInt("h_range");
		int rv = compound.getInt("v_range");
		for(int i = 0; i < compound.getInt("speed") + (rv / 32) + 1; i++){
			// Pick a block within a rh x rh x rv area.
			// If this block is air, stop. If this block doesn't have a tainted form, re-roll.
			// Do this up to 8 times.
			Block dead = null;
			BlockPos taintingPos = pos;
			int iter = 0;
			while(dead == null && iter < 8){
				// TODO: don't try to pick blocks below or above the height limit, for the Bore's sake.
				// TODO: separate up/down ranges would also be useful, also for the bore, so its not terrible on the surface.
				taintingPos = pos.north(RANDOM.nextInt(rh + 1) - (rh / 2)).west(RANDOM.nextInt(rh + 1) - (rh / 2)).up(RANDOM.nextInt(rv + 1) - (rv / 2));
				dead = world.getBlockState(taintingPos).getBlock();
				if(dead.isAir(world.getBlockState(taintingPos), world, taintingPos)){
					dead = null;
					break;
				}
				// Drain open/unsealed jars
				// TODO: replace with essentia input.
				if(dead.hasTileEntity(world.getBlockState(taintingPos))){
					TileEntity te = world.getTileEntity(taintingPos);
					if(te instanceof VisShareable){
						VisShareable shareable = ((VisShareable)te);
						if(shareable.isVisShareable() && !shareable.isSecure()){
							AspectBattery vis = (AspectBattery)IAspectHandler.getFrom(te);
							if(vis != null){
								if(vis.getHoldersAmount() != 0)
									vis.drain(RANDOM.nextInt(vis.getHoldersAmount()), 8, false);
							}
						}
						break;
					}
				}
				dead = Taint.getDeadOfBlock(Taint.getPureOfBlock(dead));
				// todo: what the heck?
				if(compound.getBoolean("silk_touch"))
					dead = Taint.getPureOfBlock(dead);
				iter++;
			}
			// Replace it with its dead form if found.
			if(dead != null && !world.isRemote()){
				BlockState deadState = switchBlock(world.getBlockState(taintingPos), dead);
				world.setBlockState(taintingPos, deadState);
				if(dead.isAir(world.getBlockState(taintingPos), world, taintingPos)){
					int rnd = RANDOM.nextInt(9) + 4;
					for(int j = 0; j < rnd; j++){
						world.addParticle(
								new BlockParticleData(ParticleTypes.FALLING_DUST, Blocks.BLACK_CONCRETE_POWDER.getDefaultState()),
								taintingPos.getX() + 0.5f + ((RANDOM.nextInt(9) - 4) / 10f), taintingPos.getY() + 0.5f + ((RANDOM.nextInt(9) - 4) / 10f), taintingPos.getZ() + 0.5f + ((RANDOM.nextInt(9) - 4) / 10f),
								0.1f, 0.1f, 0.1f
						); // Ash Particle if block is destroyed
					}
				}
			}
		}
	}
	
	@Override
	public CompoundNBT getShareableData(CompoundNBT compound){
		return compound;
	}
}