package net.arcanamod.blocks.multiblocks.taint_scrubber;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITaintScrubberExtension{
	
	/**
	 * Returns true if an extension is in a valid position.
	 *
	 * @param world
	 * 		World
	 * @param pos
	 * 		Position of extension
	 * @return If an extension is in a correct position.
	 */
	boolean isValidConnection(World world, BlockPos pos);
	
	/**
	 * Called by a taint scrubber that this extension is connected to.
	 *
	 * @param world
	 * 		World
	 * @param pos
	 * 		Position of extension
	 */
	void sendUpdate(World world, BlockPos pos);
	
	/**
	 * Runs extension action.
	 *
	 * @param world
	 * 		World
	 * @param pos
	 * 		Position of extension
	 */
	void run(World world, BlockPos pos, CompoundNBT compound);
	
	CompoundNBT getShareableData(CompoundNBT compound);
}