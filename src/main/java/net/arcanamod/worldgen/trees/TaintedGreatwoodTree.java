package net.arcanamod.worldgen.trees;

import net.minecraft.block.trees.BigTree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
public class TaintedGreatwoodTree extends BigTree{
	/**
	 * Get a {@link ConfiguredFeature} of the huge variant of this tree
	 *
	 * @param rand
	 */
	@Nullable
	@Override
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getHugeTreeFeature(Random rand) {
		return null;
	}

	/**
	 * Get a {@link ConfiguredFeature} of tree
	 *
	 * @param randomIn
	 * @param largeHive
	 */
	@Nullable
	@Override
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
		return null;
	}
	
	/*@Nullable
	protected ConfiguredFeature<HugeTreeFeatureConfig, ?> getHugeTreeFeature(Random rand){
		return ArcanaFeatures.GREATWOOD_TREE.withConfiguration(ArcanaFeatures.TAINTED_GREATWOOD_TREE_CONFIG);
	}
	
	@Nullable
	protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random rand, boolean h){
		return null;
	}*/
}