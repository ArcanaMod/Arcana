package net.arcanamod.worldgen.trees;

import net.arcanamod.worldgen.ArcanaFeatures;
import net.minecraft.block.trees.BigTree;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.HugeTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
public class TaintedGreatwoodTree extends BigTree{
	
	@Nullable
	protected ConfiguredFeature<HugeTreeFeatureConfig, ?> getHugeTreeFeature(Random rand){
		return ArcanaFeatures.GREATWOOD_TREE.withConfiguration(ArcanaFeatures.TAINTED_GREATWOOD_TREE_CONFIG);
	}
	
	@Nullable
	protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random rand, boolean h){
		return null;
	}
}
