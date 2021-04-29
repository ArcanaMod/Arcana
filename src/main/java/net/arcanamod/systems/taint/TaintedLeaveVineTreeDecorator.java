package net.arcanamod.systems.taint;
/*
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.VineBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.treedecorator.LeaveVineTreeDecorator;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class TaintedLeaveVineTreeDecorator extends LeaveVineTreeDecorator {

	public void func_225576_a_(IWorld world, Random random, List<BlockPos> lp0, List<BlockPos> lp1, Set<BlockPos> blockPositions, MutableBoundingBox box) {
		lp1.forEach((current) -> {
			if (random.nextInt(4) == 0) {
				BlockPos blockpos = current.west();
				if (AbstractTreeFeature.isAir(world, blockpos)) {
					this.generateVines(world, blockpos, VineBlock.EAST, blockPositions, box);
				}
			}

			if (random.nextInt(4) == 0) {
				BlockPos blockpos1 = current.east();
				if (AbstractTreeFeature.isAir(world, blockpos1)) {
					this.generateVines(world, blockpos1, VineBlock.WEST, blockPositions, box);
				}
			}

			if (random.nextInt(4) == 0) {
				BlockPos blockpos2 = current.north();
				if (AbstractTreeFeature.isAir(world, blockpos2)) {
					this.generateVines(world, blockpos2, VineBlock.SOUTH, blockPositions, box);
				}
			}

			if (random.nextInt(4) == 0) {
				BlockPos blockpos3 = current.south();
				if (AbstractTreeFeature.isAir(world, blockpos3)) {
					this.generateVines(world, blockpos3, VineBlock.NORTH, blockPositions, box);
				}
			}

		});
	}

	private void generateVines(IWorldGenerationReader p_227420_1_, BlockPos p_227420_2_, BooleanProperty p_227420_3_, Set<BlockPos> p_227420_4_, MutableBoundingBox p_227420_5_) {
		func_227424_a_(p_227420_1_, p_227420_2_, p_227420_3_, p_227420_4_, p_227420_5_);
		int i = 4;

		for(BlockPos blockpos = p_227420_2_.down(); AbstractTreeFeature.isAir(p_227420_1_, blockpos) && i > 0; --i) {
			func_227424_a_(p_227420_1_, blockpos, p_227420_3_, p_227420_4_, p_227420_5_);
			blockpos = blockpos.down();
		}
	}

	protected void func_227424_a_(IWorldWriter p_227424_1_, BlockPos p_227424_2_, BooleanProperty p_227424_3_, Set<BlockPos> p_227424_4_, MutableBoundingBox p_227424_5_) {
		this.func_227423_a_(p_227424_1_, p_227424_2_, ArcanaBlocks.TAINTED_VINE.get().getDefaultState().with(p_227424_3_, Boolean.valueOf(true)), p_227424_4_, p_227424_5_);
	}
}
*/