package net.arcanamod.worldgen.trees.features;
/*
import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.HugeTreeFeatureConfig;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public class GreatwoodTreeFeature extends AbstractTreeFeature<HugeTreeFeatureConfig>{
	
	public GreatwoodTreeFeature(Function<Dynamic<?>, ? extends HugeTreeFeatureConfig> deserialize){
		super(deserialize);
	}
	
	protected boolean place(IWorldGenerationReader world, Random rand, BlockPos pos, Set<BlockPos> logs, Set<BlockPos> leaves, MutableBoundingBox box, HugeTreeFeatureConfig config){
		int height = rand.nextInt(3) + rand.nextInt(3) + config.baseHeight;
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		if(y >= 1 && y + height + 1 < world.getMaxHeight()){
			BlockPos ground = pos.down();
			if(!isSoil(world, ground, config.getSapling()))
				return false;
			else if(!isSpaceClearForHeight(world, pos, height))
				return false;
			else{
				setDirtAt(world, ground, pos);
				setDirtAt(world, ground.east(), pos);
				setDirtAt(world, ground.south(), pos);
				setDirtAt(world, ground.south().east(), pos);
				
				int top = y + height - 1;

				Set<BlockPos> leafNodes = new HashSet<>();

				// Roots
				for (int x1 = -1; x1 <= 2; x1++) {
				    for (int z1 = -1; z1 <= 2; z1++) {
				    	// Skip root placement if we're in the trunk
				    	if ((x1 == 0 || x1 == 1) && (z1 == 0 || z1 == 1)) {
				    		continue;
						}

				    	// Get the root height by nesting random calls to make it biased towards 0
				        int rootHeight = rand.nextInt(rand.nextInt(4) + 1);

						if (isRootOnEdge(x1) && isRootOnEdge(z1)) {
							rootHeight--; // Reduce on corners
						}

						if (rootHeight > 0) {
							BlockPos groundPos = new BlockPos(x + x1, y - 1, z + z1);

							if (isAir(world, groundPos)) {
								world.setBlockState(groundPos, Blocks.DIRT.getDefaultState(), 3);
							}

							// Place roots
							for (int curHeight = 0; curHeight < rootHeight; curHeight++) {
								int curY = y + curHeight;
								BlockPos curPos = new BlockPos(x + x1, curY, z + z1);

								if (isAirOrLeaves(world, curPos)) {
									func_227216_a_(world, rand, curPos, logs, box, config);
								}
							}
						}
				    }
				}

				// main trunk
				for(int curHeight = 0; curHeight < height; ++curHeight){
					int curY = y + curHeight;
					BlockPos curPos = new BlockPos(x, curY, z);
					if(isAirOrLeaves(world, curPos)) {
						func_227216_a_(world, rand, curPos, logs, box, config);
						func_227216_a_(world, rand, curPos.east(), logs, box, config);
						func_227216_a_(world, rand, curPos.south(), logs, box, config);
						func_227216_a_(world, rand, curPos.east().south(), logs, box, config);
					}

					// Branches
					if (curHeight > 6 && curHeight < height - 3) {
						int branchCount = 1 + (curHeight / 8);
						double offset = Math.PI * 2 * rand.nextDouble();

						// Make fewer branches at the bottom, but more at the top
						for (int i = 0; i < branchCount; i++) {
							double angle = (((double) i / branchCount) * (Math.PI * 2)) + offset + (rand.nextDouble() * 0.2);
							int length = rand.nextInt(2) + (6 - branchCount) + curHeight / 10;
							// Choose a starting location on the trunk
							BlockPos start = chooseStart(curPos, rand);

							for (int j = 0; j <= length; j++) {
								// Traverse through the branch
								BlockPos local = start.add(Math.cos(angle) * j, j / 2.0, Math.sin(angle) * j);

								// Place logs if it's air
								if (isAir(world, local)) {
									func_227216_a_(world, rand, local, logs, box, config);
								}

								// If we're at the end, mark this position for generating leaves
								if (j == length) {
									leafNodes.add(local);
								}
							}
						}

					}

					// Add leaves to the top of the trunk
					if (curHeight == height - 1) {
						leafNodes.add(curPos);
						leafNodes.add(curPos.east());
						leafNodes.add(curPos.south());
						leafNodes.add(curPos.east().south());
					}
				}

				for (BlockPos node : leafNodes) {
					// Iterate in a spheroid to place leaves
					for(int x1 = -3; x1 <= 3; x1++) {
						for(int z1 = -3; z1 <= 3; z1++) {
							for(int y1 = -2; y1 <= 2; y1++) {
								double rX = x1 / 3.0;
								double rZ = z1 / 3.0;
								double rY = y1 / 2.0;
								// Scale the distance to customize the blob shape
								rX *= 1.1;
								rZ *= 1.1;
								rY *= 0.95;
								double dist = rX * rX + rZ * rZ + rY * rY;

								// Apply randomness to the radius and place leaves
								if (dist <= 1 + (rand.nextDouble() * 0.3)) {
									BlockPos local = node.add(x1, y1, z1);
									if (isAir(world, local)) {
										world.setBlockState(local, config.leavesProvider.getBlockState(rand, local), 3);
									}
								}
							}
						}
					}
				}

				return true;
			}
		}
		return false;
	}

	private boolean isRootOnEdge(int axis) {
		return axis == -1 || axis == 2;
	}

	private static BlockPos chooseStart(BlockPos start, Random random) {
		switch (random.nextInt(4)) {
		default:
		case 0:
			return start;
		case 1:
			return start.east();
		case 2:
			return start.south();
		case 3:
			return start.east().south();
		}
	}
	
	private boolean isSpaceClearForHeight(IWorldGenerationBaseReader world, BlockPos pos, int height){
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		BlockPos.Mutable cursor = new BlockPos.Mutable();
		
		for(int i = 0; i <= height + 1; ++i){
			int r = 1;
			if(i == 0)
				r = 0;
			
			if(i >= height - 1)
				r = 2;
			
			for(int xr = -r; xr <= r; ++xr)
				for(int zr = -r; zr <= r; ++zr)
					if(!canBeReplacedByLogs(world, cursor.setPos(x + xr, y + i, z + zr)))
						return false;
		}
		
		return true;
	}
}*/