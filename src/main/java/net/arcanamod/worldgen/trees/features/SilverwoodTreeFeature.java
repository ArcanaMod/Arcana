package net.arcanamod.worldgen.trees.features;
/*
import com.mojang.datafixers.Dynamic;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.capabilities.AuraChunk;
import net.arcanamod.event.WorldTickHandler;
import net.arcanamod.world.Node;
import net.arcanamod.world.NodeType;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@ParametersAreNonnullByDefault
public class SilverwoodTreeFeature extends AbstractTreeFeature<TreeFeatureConfig> {
	
	public SilverwoodTreeFeature(Function<Dynamic<?>, ? extends TreeFeatureConfig> deserialize){
		super(deserialize);
	}
	
	// mostly dark oak copypasta
	protected boolean place(IWorldGenerationReader world, Random rand, BlockPos pos, Set<BlockPos> logs, Set<BlockPos> leaves, MutableBoundingBox box, TreeFeatureConfig config){
		int height = rand.nextInt(2) + rand.nextInt(2) + config.baseHeight;
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		if(y >= 1 && y + height + 1 < world.getMaxHeight()){
			BlockPos ground = pos.down();
			if(!isSoil(world, ground, ArcanaBlocks.SILVERWOOD_SAPLING.get()))
				return false;
			else if(!isSpaceClearForHeight(world, pos, height))
				return false;
			else {

				for(int x1 = -2; x1 <= 2; x1++) {
				    for(int z1 = -2; z1 <= 2; z1++) {
				    	// Manhattan distance from center
				    	int dist = Math.abs(x1) + Math.abs(z1);

				    	// Place in a 2-wide plus formation
				    	if (dist <= 2) {
				    		// 0 distance: normal height
				    		int logHeight = height;
				    		if (dist == 1) {
								// plus shape: 70% of height
				    			logHeight = (int) (height * 0.7) + rand.nextInt(2);
							} else if (dist == 2) {
				    			// 2-wide plus shape: 1 high plus random
				    			logHeight = 1 + rand.nextInt(2);
							}

				    		// Place the logs
							generateLogColumn(world, rand, pos.add(x1, 0, z1), logHeight, config);
						}
				    }
				}

				// Iterate in a spheroid to place leaves
				for(int x1 = -4; x1 <= 4; x1++) {
				    for(int z1 = -4; z1 <= 4; z1++) {
				        for(int y1 = -5; y1 <= 5; y1++) {
				            double rX = x1 / 4.0;
				            double rZ = z1 / 4.0;
				            double rY = y1 / 5.0;
				            double dist = rX * rX + rZ * rZ + rY * rY;

				            // Apply randomness to the radius and place leaves
				            if (dist <= 0.8 + (rand.nextDouble() * 0.4)) {
				            	BlockPos local = pos.add(x1, height - 4 + y1, z1);
				            	if (isAir(world, local)) {
									world.setBlockState(local, config.leavesProvider.getBlockState(rand, local), 3);
								}
							}
				        }
				    }
				}

				// add pure node at half height
				if(rand.nextInt(100) < ArcanaConfig.SILVERWOOD_NODE_CHANCE.get())
					WorldTickHandler.onTick.add(w -> {
						NodeType type = NodeType.PURE;
						IAspectHandler aspects = type.genBattery(pos, w, rand);
						requireNonNull(AuraChunk.getFrom((Chunk)w.getChunk(pos))).addNode(new Node(aspects, type, x, y + height / 2f, z, 0));
					});
				
				return true;
			}
		}
		return false;
	}

	private void generateLogColumn(IWorldGenerationReader world, Random random, BlockPos start, int height, TreeFeatureConfig config) {
		for (int y = 0; y < height; y++) {
			BlockPos local = start.up(y);
			if (canBeReplacedByLogs(world, local)) {
				world.setBlockState(local, config.trunkProvider.getBlockState(random, local), 3);
			}
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