package net.arcanamod.worldgen;
/*
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.worldgen.trees.features.GreatwoodTreeFeature;
import net.arcanamod.worldgen.trees.features.SilverwoodTreeFeature;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HugeTreeFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.arcanamod.Arcana.MODID;

@SuppressWarnings("deprecation")
public class ArcanaFeatures{
	
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, MODID);
	
	// have to delay populating it until block registry is done because forge thonk
	public static TreeFeatureConfig SILVERWOOD_TREE_CONFIG;
	public static HugeTreeFeatureConfig GREATWOOD_TREE_CONFIG;
	public static HugeTreeFeatureConfig TAINTED_GREATWOOD_TREE_CONFIG;
	
	// features have to exist first because forge is stupid and insists on registering biomes first
	public static Feature<NoFeatureConfig> NODE = new NodeFeature(NoFeatureConfig::deserialize);
	public static Feature<TreeFeatureConfig> SILVERWOOD_TREE = new SilverwoodTreeFeature(TreeFeatureConfig::func_227338_a_);
	public static Feature<HugeTreeFeatureConfig> GREATWOOD_TREE = new GreatwoodTreeFeature(HugeTreeFeatureConfig::func_227277_a_);
	
	// TODO: move to regular registry event
	private static final RegistryObject<Feature<NoFeatureConfig>> NODE_R = FEATURES.register("node", () -> NODE);
	private static final RegistryObject<Feature<TreeFeatureConfig>> SILVERWOOD_TREE_R = FEATURES.register("silverwood_tree", () -> SILVERWOOD_TREE);
	private static final RegistryObject<Feature<HugeTreeFeatureConfig>> GREATWOOD_TREE_R = FEATURES.register("greatwood_tree", () -> GREATWOOD_TREE);
	
	public static void addMagicalForestTrees(Biome biome){
		// blocks must be registered first, but these configs have to be made at biome time, not feature time
		if(SILVERWOOD_TREE_CONFIG == null){
			SILVERWOOD_TREE_CONFIG = new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(ArcanaBlocks.SILVERWOOD_LOG.get().getDefaultState()), new SimpleBlockStateProvider(ArcanaBlocks.SILVERWOOD_LEAVES.get().getDefaultState()), new BlobFoliagePlacer(3, 1)).heightRandA(2).heightRandB(2).baseHeight(12).build();
			GREATWOOD_TREE_CONFIG = new HugeTreeFeatureConfig.Builder(new SimpleBlockStateProvider(ArcanaBlocks.GREATWOOD_LOG.get().getDefaultState()), new SimpleBlockStateProvider(ArcanaBlocks.GREATWOOD_LEAVES.get().getDefaultState())).setSapling(ArcanaBlocks.GREATWOOD_SAPLING.get()).baseHeight(18).build();
			TAINTED_GREATWOOD_TREE_CONFIG = new HugeTreeFeatureConfig.Builder(new SimpleBlockStateProvider(ArcanaBlocks.TAINTED_GREATWOOD_LOG.get().getDefaultState()), new SimpleBlockStateProvider(ArcanaBlocks.TAINTED_GREATWOOD_LEAVES.get().getDefaultState())).setSapling(ArcanaBlocks.TAINTED_GREATWOOD_SAPLING.get()).baseHeight(18).build();
		}
		
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, SILVERWOOD_TREE.withConfiguration(SILVERWOOD_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(0, 0.04f, 1))));
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, GREATWOOD_TREE.withConfiguration(GREATWOOD_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(0, 0.1f, 1))));
	}
}*/