package net.arcanamod.systems.taint;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import javax.annotation.Nullable;
import java.util.Random;

public class TaintedAcaciaTree extends Tree {
	/**
	 * Get a {@link net.minecraft.world.gen.feature.ConfiguredFeature} of tree
	 */
	@Nullable
	protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean beeLike) {
		return Feature.ACACIA_TREE.withConfiguration(TaintedFeatures.Config.ACACIA_TREE_CONFIG);
	}
}
