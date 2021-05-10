package net.arcanamod.worldgen.trees;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
public class SilverwoodTree extends Tree{
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
	protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random rand, boolean h){
		return ArcanaFeatures.SILVERWOOD_TREE.withConfiguration(ArcanaFeatures.SILVERWOOD_TREE_CONFIG);
	}*/
}