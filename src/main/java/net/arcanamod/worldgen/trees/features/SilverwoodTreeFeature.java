package net.arcanamod.worldgen.trees.features;

import com.mojang.datafixers.Dynamic;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.capabilities.AuraChunk;
import net.arcanamod.event.WorldTickHandler;
import net.arcanamod.world.Node;
import net.arcanamod.world.NodeType;
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
public class SilverwoodTreeFeature extends AbstractTreeFeature<TreeFeatureConfig>{
	
	public SilverwoodTreeFeature(Function<Dynamic<?>, ? extends TreeFeatureConfig> deserialize){
		super(deserialize);
	}
	
	// mostly dark oak copypasta
	protected boolean place(IWorldGenerationReader world, Random rand, BlockPos pos, Set<BlockPos> logs, Set<BlockPos> leaves, MutableBoundingBox box, TreeFeatureConfig config){
		int height = rand.nextInt(3) + rand.nextInt(3) + config.baseHeight;
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		if(y >= 1 && y + height + 1 < world.getMaxHeight()){
			BlockPos ground = pos.down();
			if(!isSoil(world, ground, ArcanaBlocks.SILVERWOOD_SAPLING.get()))
				return false;
			else if(!isSpaceClearForHeight(world, pos, height))
				return false;
			else{
				setDirtAt(world, ground, pos);
				setDirtAt(world, ground.east(), pos);
				setDirtAt(world, ground.south(), pos);
				setDirtAt(world, ground.south().east(), pos);
				
				int top = y + height - 1;
				
				// main trunk
				for(int curHeight = 0; curHeight < height; ++curHeight){
					int k2 = y + curHeight;
					BlockPos curPos = new BlockPos(x, k2, z);
					if(isAirOrLeaves(world, curPos)){
						func_227216_a_(world, rand, curPos, logs, box, config);
						func_227216_a_(world, rand, curPos.east(), logs, box, config);
						func_227216_a_(world, rand, curPos.south(), logs, box, config);
						func_227216_a_(world, rand, curPos.east().south(), logs, box, config);
					}
				}
				
				// main leaves
				for(int cX = -3; cX <= 0; ++cX)
					for(int cZ = -3; cZ <= 0; ++cZ){
						func_227219_b_(world, rand, new BlockPos(x + cX, top - 1, z + cZ), leaves, box, config);
						func_227219_b_(world, rand, new BlockPos(1 + x - cX, top - 1, z + cZ), leaves, box, config);
						func_227219_b_(world, rand, new BlockPos(x + cX, top - 1, 1 + z - cZ), leaves, box, config);
						func_227219_b_(world, rand, new BlockPos(1 + x - cX, top - 1, 1 + z - cZ), leaves, box, config);
						if((cX > -3 || cZ > -2) && (cX != -2 || cZ != -3)){
							func_227219_b_(world, rand, new BlockPos(x + cX, top + 1, z + cZ), leaves, box, config);
							func_227219_b_(world, rand, new BlockPos(1 + x - cX, top + 1, z + cZ), leaves, box, config);
							func_227219_b_(world, rand, new BlockPos(x + cX, top + 1, 1 + z - cZ), leaves, box, config);
							func_227219_b_(world, rand, new BlockPos(1 + x - cX, top + 1, 1 + z - cZ), leaves, box, config);
						}
					}
				
				// add little square of leaves on top
				func_227219_b_(world, rand, new BlockPos(x, top + 2, z), leaves, box, config);
				func_227219_b_(world, rand, new BlockPos(x + 1, top + 2, z), leaves, box, config);
				func_227219_b_(world, rand, new BlockPos(x + 1, top + 2, z + 1), leaves, box, config);
				func_227219_b_(world, rand, new BlockPos(x, top + 2, z + 1), leaves, box, config);
				
				for(int k3 = -4; k3 <= 5; ++k3)
					for(int j4 = -4; j4 <= 5; ++j4)
						if((k3 != -4 || j4 != -4) && (k3 != -4 || j4 != 5) && (k3 != 5 || j4 != -4) && (k3 != 5 || j4 != 5) && (Math.abs(k3) < 4 || Math.abs(j4) < 4))
							func_227219_b_(world, rand, new BlockPos(x + k3, top, z + j4), leaves, box, config);
				
				for(int l3 = -1; l3 <= 2; ++l3)
					for(int k4 = -1; k4 <= 2; ++k4)
						if(l3 < 0 || l3 > 1 || k4 < 0 || k4 > 1){
							if(rand.nextInt(3) <= 0){
								// at extra logs at top
								for(int l2 = 0; l2 < rand.nextInt(3) + 2; ++l2)
									func_227216_a_(world, rand, new BlockPos(x + l3, top - l2 - 1, z + k4), logs, box, config);
								
								for(int xOff = -1; xOff <= 1; ++xOff)
									for(int zOff = -1; zOff <= 1; ++zOff)
										func_227219_b_(world, rand, new BlockPos(x + l3 + xOff, top, z + k4 + zOff), leaves, box, config);
								
								for(int k5 = -2; k5 <= 2; ++k5)
									for(int l5 = -2; l5 <= 2; ++l5)
										if(Math.abs(k5) != 2 || Math.abs(l5) != 2)
											func_227219_b_(world, rand, new BlockPos(x + l3 + k5, top - 1, z + k4 + l5), leaves, box, config);
							}
							// add extra logs at roots
							for(int l2 = 0; l2 < rand.nextInt(3) + 1; ++l2)
								func_227216_a_(world, rand, new BlockPos(x + l3, y + l2 - 1, z + k4), logs, box, config);
						}
				
				// add pure node at half height
				if(rand.nextInt(100) < ArcanaConfig.SILVERWOOD_NODE_CHANCE.get())
					WorldTickHandler.onTick.add(w -> {
						NodeType type = NodeType.PURE;
						IAspectHandler aspects = type.genBattery(pos, w, rand);
						requireNonNull(AuraChunk.getFrom((Chunk)w.getChunk(pos))).addNode(new Node(aspects, type, x + 1, y + height / 2f, z + 1, 0));
					});
				
				return true;
			}
		}
		return false;
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
}