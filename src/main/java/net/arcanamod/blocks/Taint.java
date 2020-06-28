package net.arcanamod.blocks;

import net.arcanamod.blocks.tainted.TaintedFallingBlock;
import net.arcanamod.blocks.tainted.TaintedGrassPathBlock;
import net.arcanamod.blocks.tainted.TaintedPlantBlock;
import net.arcanamod.world.ServerAuraView;
import net.minecraft.block.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static net.arcanamod.blocks.DelegatingBlock.switchBlock;

public class Taint{
	
	public static final BooleanProperty UNTAINTED = BooleanProperty.create("untainted"); // false by default
	private static final Map<Block, Block> taintMap = new HashMap<>();
	
	@SuppressWarnings("deprecation")
	public static Block taintedOf(Block block){
		if(block instanceof FallingBlock)
			return new TaintedFallingBlock(block);
		else if(block instanceof IPlantable || block instanceof IShearable || block instanceof IGrowable)
			return new TaintedPlantBlock(block);
		else if(block instanceof GrassPathBlock)
			return new TaintedGrassPathBlock(block);
		else
			return new TaintedBlock(block);
	}
	
	public static Block getPureOfBlock(Block block){
		return taintMap.entrySet().stream()
				.filter(entry -> entry.getValue() == block)
				.map(Map.Entry::getKey)
				.findAny().orElse(null);
	}
	
	public static Block getTaintedOfBlock(Block block){
		return taintMap.get(block);
	}
	
	public static void addTaintMapping(Block original, Block tainted){
		taintMap.put(original, tainted);
	}
	
	public static void tickTaintedBlock(BlockState state, ServerWorld world, BlockPos pos, Random random){
		if(!state.get(UNTAINTED)){
			// and if flux level is greater than 5,
			ServerAuraView auraView = new ServerAuraView(world);
			int at = auraView.getTaintAt(pos);
			if(at > 5){
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
					BlockState taintedState = switchBlock(world.getBlockState(taintingPos), tainted).with(UNTAINTED, false);
					world.setBlockState(taintingPos, taintedState);
					// Reduce flux level
					auraView.addTaintAt(pos, -1);
					// Schedule a tick
					world.getPendingBlockTicks().scheduleTick(pos, state.getBlock(), taintTickWait(at));
				}
			}
		}
	}
	
	public static int taintTickWait(int taintLevel){
		// more taint level -> less tick wait
		int base = (int)((1d / taintLevel) * 200);
		return base > 0 ? base : 1;
	}
}