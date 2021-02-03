package net.arcanamod.worldgen;

import com.mojang.datafixers.Dynamic;
import net.arcanamod.util.Pair;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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
				
				// lets pick a number of points to start a branch along with their directions
				Set<Pair<BlockPos, Direction>> branches = new HashSet<>();
				
				// main trunk
				for(int curHeight = 0; curHeight < height; ++curHeight){
					int curY = y + curHeight;
					BlockPos curPos = new BlockPos(x, curY, z);
					if(isAirOrLeaves(world, curPos)){
						func_227216_a_(world, rand, curPos, logs, box, config);
						func_227216_a_(world, rand, curPos.east(), logs, box, config);
						func_227216_a_(world, rand, curPos.south(), logs, box, config);
						func_227216_a_(world, rand, curPos.east().south(), logs, box, config);
						if(curHeight > 4 && curHeight < height - 3){
							// could make this random but it looks better just rotating around
							Direction branchDir = Direction.byHorizontalIndex(curHeight);
							branches.add(Pair.of(curPos.east(rand.nextInt(2)).south(rand.nextInt(2)).offset(branchDir), branchDir));
							// watch me do it again
							if(rand.nextBoolean()){
								branchDir = Direction.byHorizontalIndex(curHeight);
								branches.add(Pair.of(curPos.east(rand.nextInt(2)).south(rand.nextInt(2)).offset(branchDir), branchDir));
							}
						}
					}
				}
				
				// branches
				for(Pair<BlockPos, Direction> branch : branches){
					int flatness = rand.nextInt(2) + 2, length = rand.nextInt(3) + 4, lean = rand.nextInt(4);
					// place a log
					BlockPos.Mutable cursor = new BlockPos.Mutable(branch.getFirst());
					Direction dir = branch.getSecond();
					Direction leanDir = (lean == 0) ? dir.rotateY() : ((lean == 1) ? dir.rotateYCCW() : null);
					for(int i = 0; i < length; i++){
						if(i % flatness == 0)
							cursor.move(Direction.UP);
						func_227216_a_(world, rand, cursor, logs, box, config);
						cursor.move(dir);
						if(leanDir != null)
							cursor.move(leanDir);
						func_227219_b_(world, rand, cursor.offset(Direction.random(rand)), leaves, box, config);
					}
					// and a leaf blob
					BlockPos leafStart = cursor.toImmutable();
					func_227219_b_(world, rand, leafStart, leaves, box, config);
					for(int lX = -3; lX < 4; lX++)
						for(int lY = -3; lY < 4; lY++)
							for(int lZ = -3; lZ < 4; lZ++){
								cursor.setPos(leafStart).move(lX, lY, lZ);
								if(cursor.distanceSq(leafStart) <= 3 * 3 + rand.nextInt(2) - 1)
									func_227219_b_(world, rand, cursor, leaves, box, config);
							}
				}
				
				BlockPos.Mutable cursor = new BlockPos.Mutable(x, top, z);
				BlockPos leafStart = cursor.toImmutable();
				int h = 5;
				for(int lX = -h; lX <= h; lX++)
					for(int lY = -h; lY <= h; lY++)
						for(int lZ = -h; lZ <= h; lZ++){
							cursor.setPos(leafStart).move(lX, lY, lZ);
							if(cursor.distanceSq(leafStart) <= h * h + rand.nextInt(2) - 1)
								func_227219_b_(world, rand, cursor, leaves, box, config);
						}
				
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