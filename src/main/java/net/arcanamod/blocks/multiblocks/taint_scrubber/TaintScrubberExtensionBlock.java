package net.arcanamod.blocks.multiblocks.taint_scrubber;

import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.world.ServerAuraView;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TaintScrubberExtensionBlock extends Block implements ITaintScrubberExtension{
	private Type type;
	
	public TaintScrubberExtensionBlock(Block.Properties properties, Type type){
		super(properties);
		this.type = type;
	}
	
	@Override
	public boolean isValidConnection(World world, BlockPos pos){
		// Check that this is the right position.
		if(world.getBlockState(pos).getBlock() != this)
			return false;
		
		// Scrubber Base is placed below a regular Scrubber.
		if(this.type.equals(Type.SCRUBBER_MK2))
			if(world.getBlockState(pos.up(1)).getBlock().equals(ArcanaBlocks.TAINT_SCRUBBER_MK1.get()))
				return true;
		// Bore is placed below a regular Scrubber or Scrubber Base.
		if(this.type.equals(Type.BORE))
			if(world.getBlockState(pos.up(2)).getBlock().equals(ArcanaBlocks.TAINT_SCRUBBER_MK1.get()) && !world.getBlockState(pos.up(1)).getBlock().equals(Blocks.AIR)){
				return true;
			}else if(world.getBlockState(pos.up(1)).getBlock().equals(ArcanaBlocks.TAINT_SCRUBBER_MK1.get()))
				return true;
		// Sucker is placed above the regular Scrubber.
		if(this.type.equals(Type.SUCKER))
			return world.getBlockState(pos.down()).getBlock().equals(ArcanaBlocks.TAINT_SCRUBBER_MK1.get());
		return false;
	}
	
	@Override
	public void sendUpdate(World world, BlockPos pos){
		if(type == Type.SUCKER)
			if(world.getBlockState(pos.down()).getBlock().equals(ArcanaBlocks.TAINT_SCRUBBER_MK1.get()))
				world.setBlockState(pos.down(), world.getBlockState(pos.down()).with(TaintScrubberBlock.SUPPORTED, isValidConnection(world, pos)));
	}
	
	/**
	 * Runs extension action.
	 *
	 * @param world
	 * 		World
	 * @param pos
	 * 		Position of TaintScrubber
	 */
	@Override
	public void run(World world, BlockPos pos, CompoundNBT compound){
		if(this.type.equals(Type.SUCKER))
			if(!world.isRemote && world.getGameTime() % 2 == 0){
				ServerAuraView aura = new ServerAuraView((ServerWorld)world);
				//aura.addTaintAt(pos, -Math.abs(compound.getInt("speed") + 1));
				aura.addFluxAt(pos, -1);
			}
	}
	
	@Override
	public CompoundNBT getShareableData(CompoundNBT compound){
		if(this.type.equals(Type.SCRUBBER_MK2)){
			if(compound.getInt("h_range") < 16)
				compound.putInt("h_range", 16);
			if(compound.getInt("v_range") < 16)
				compound.putInt("v_range", 16);
		}
		if(this.type.equals(Type.BORE))
			compound.putInt("v_range", 256);
		return compound;
	}
	
	/**
	 * For addon dev's:
	 * Arcana Pre-Defined Extensions. You can add own extension implementing ITaintScrubberExtension.
	 */
	public enum Type{
		SCRUBBER_MK2,
		SUCKER,
		BORE
	}
}
