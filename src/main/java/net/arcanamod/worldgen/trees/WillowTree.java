package net.arcanamod.worldgen.trees;

import net.arcanamod.worldgen.ArcanaFeatures;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
public class WillowTree extends Tree{
	
	@Nullable
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random rand, boolean largeHive){
		return ArcanaFeatures.WILLOW_TREE;
	}
}