package net.arcanamod.worldgen;

import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HugeTreeFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.arcanamod.Arcana.MODID;

@SuppressWarnings("deprecation")
public class ArcanaFeatures{
	
	public static final DeferredRegister<Feature<?>> FEATURES = new DeferredRegister<>(ForgeRegistries.FEATURES, MODID);
	
	// have to delay populating it until block registry is done because forge thonk
	public static TreeFeatureConfig SILVERWOOD_TREE_CONFIG;
	
	public static final RegistryObject<Feature<NoFeatureConfig>> NODE = FEATURES.register("node", () -> new NodeFeature(NoFeatureConfig::deserialize));
	public static final RegistryObject<Feature<TreeFeatureConfig>> SILVERWOOD_TREE = FEATURES.register("silverwood_tree", () -> {
		SILVERWOOD_TREE_CONFIG = new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(ArcanaBlocks.SILVERWOOD_LOG.get().getDefaultState()), new SimpleBlockStateProvider(ArcanaBlocks.SILVERWOOD_LEAVES.get().getDefaultState()), new BlobFoliagePlacer(3, 1)).heightRandA(2).heightRandB(3).baseHeight(7).build();
		return new SilverwoodTreeFeature(TreeFeatureConfig::func_227338_a_);
	});
}