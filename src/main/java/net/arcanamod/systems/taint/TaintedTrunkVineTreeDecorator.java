package net.arcanamod.systems.taint;
/*
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.VineBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.treedecorator.TrunkVineTreeDecorator;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class TaintedTrunkVineTreeDecorator extends TrunkVineTreeDecorator {
	public void func_225576_a_(IWorld p_225576_1_, Random p_225576_2_, List<BlockPos> p_225576_3_, List<BlockPos> p_225576_4_, Set<BlockPos> p_225576_5_, MutableBoundingBox p_225576_6_) {
		p_225576_3_.forEach((p_227433_5_) -> {
			if (p_225576_2_.nextInt(3) > 0) {
				BlockPos blockpos = p_227433_5_.west();
				if (AbstractTreeFeature.isAir(p_225576_1_, blockpos)) {
					this.func_227424_a_(p_225576_1_, blockpos, VineBlock.EAST, p_225576_5_, p_225576_6_);
				}
			}

			if (p_225576_2_.nextInt(3) > 0) {
				BlockPos blockpos1 = p_227433_5_.east();
				if (AbstractTreeFeature.isAir(p_225576_1_, blockpos1)) {
					this.func_227424_a_(p_225576_1_, blockpos1, VineBlock.WEST, p_225576_5_, p_225576_6_);
				}
			}

			if (p_225576_2_.nextInt(3) > 0) {
				BlockPos blockpos2 = p_227433_5_.north();
				if (AbstractTreeFeature.isAir(p_225576_1_, blockpos2)) {
					this.func_227424_a_(p_225576_1_, blockpos2, VineBlock.SOUTH, p_225576_5_, p_225576_6_);
				}
			}

			if (p_225576_2_.nextInt(3) > 0) {
				BlockPos blockpos3 = p_227433_5_.south();
				if (AbstractTreeFeature.isAir(p_225576_1_, blockpos3)) {
					this.func_227424_a_(p_225576_1_, blockpos3, VineBlock.NORTH, p_225576_5_, p_225576_6_);
				}
			}

		});
	}

	protected void func_227424_a_(IWorldWriter p_227424_1_, BlockPos p_227424_2_, BooleanProperty p_227424_3_, Set<BlockPos> p_227424_4_, MutableBoundingBox p_227424_5_) {
		this.func_227423_a_(p_227424_1_, p_227424_2_, ArcanaBlocks.TAINTED_VINE.get().getDefaultState().with(p_227424_3_, Boolean.valueOf(true)), p_227424_4_, p_227424_5_);
	}
}
*/