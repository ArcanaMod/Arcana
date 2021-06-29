package net.arcanamod.worldgen.trees;

import net.arcanamod.worldgen.ArcanaFeatures;
import net.minecraft.block.trees.BigTree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
public class GreatwoodTree extends BigTree{
	
	@Nullable
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getHugeTreeFeature(Random rand){
		System.out.println(ArcanaFeatures.GREATWOOD_TREE);
		return ArcanaFeatures.GREATWOOD_TREE;
	}
	
	@Nullable
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive){
		return null;
	}
}
