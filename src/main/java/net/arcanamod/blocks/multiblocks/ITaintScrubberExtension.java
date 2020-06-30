package net.arcanamod.blocks.multiblocks;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITaintScrubberExtension {

	/**
	 * It checks if the extension is in right place
	 * @param world World
	 * @param pos Position of extension
	 * @return isValidConnection
	 */
	boolean isValidConnection(World world, BlockPos pos);

	/**
	 * It is performed if this block is found by TaintScrubber.
	 * @param world World
	 * @param pos Position of extension
	 */
	void sendUpdate(World world, BlockPos pos);

	/**
	 * Runs extension action.
	 * @param world World
	 * @param pos Position of extension
	 */
	void run(World world, BlockPos pos, CompoundNBT compound);

	CompoundNBT getShareableData(CompoundNBT compound);
}
