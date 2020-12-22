package net.arcanamod.world;

import net.arcanamod.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public final class WorldInteractions {
	private static final Logger LOGGER = LogManager.getLogger();

	protected World world;

	public static HashMap<Block, Pair<Block,Block>> freezable = new HashMap<>();

	private WorldInteractions(World world) {
		this.world = world;
	}

	public static WorldInteractions fromWorld(World world){
		return new WorldInteractions(world);
	}

	public void freezeBlock(BlockPos position){
		Block targetedBlock = world.getBlockState(position).getBlock();
		if (freezable.containsKey(targetedBlock)) {
			Pair<Block,Block> replace = freezable.get(targetedBlock);
			world.setBlockState(position, replace.getFirst().getDefaultState());
			if (replace.getSecond() != Blocks.AIR){
				if (world.getBlockState(position.up()).isAir(world,position.up())){
					world.setBlockState(position.up(), replace.getSecond().getDefaultState());
				}
			}
		}
	}
}
