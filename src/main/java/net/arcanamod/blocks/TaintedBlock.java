package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.world.ServerAuraView;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TaintedBlock extends DelegatingBlock{
	
	public TaintedBlock(Block block){
		super(block);
		Taint.addTaintMapping(block, this);
	}
	
	public boolean ticksRandomly(BlockState state){
		return true;
	}
	
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random){
		super.randomTick(state, world, pos, random);
		// If taint level is greater than 5,
		if(new ServerAuraView(world).getTaintAt(pos) > 5){
			// pick a block within a 4x6x4 area
			// If this block is air, stop. If this block doesn't have a tainted form, re-roll.
			// Do this up to 5 times.
			Block tainted = null;
			BlockPos taintingPos = pos;
			int iter = 0;
			while(tainted == null && iter < 5){
				taintingPos = pos.north(random.nextInt(9) - 4).west(random.nextInt(9) - 4).up(random.nextInt(13) - 6);
				tainted = world.getBlockState(taintingPos).getBlock();
				if(tainted.isAir(world.getBlockState(taintingPos), world, taintingPos)){
					tainted = null;
					break;
				}
				tainted = Taint.getTaintedOfBlock(tainted);
				iter++;
			}
			// Replace it with its tainted form if found.
			if(tainted != null){
				BlockState taintedState = switchBlock(world.getBlockState(taintingPos), tainted);
				world.setBlockState(taintingPos, taintedState);
			}
			// Schedule a tick?
		}
	}
}