package net.arcanamod.worldgen.trees;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class DummyTree extends Tree{
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
}