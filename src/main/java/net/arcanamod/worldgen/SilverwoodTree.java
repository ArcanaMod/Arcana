package net.arcanamod.worldgen;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
public class SilverwoodTree extends Tree{
	
	@Nullable
	protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random rand, boolean h){
		return ArcanaFeatures.SILVERWOOD_TREE.get().withConfiguration(ArcanaFeatures.SILVERWOOD_TREE_CONFIG);
	}
}
