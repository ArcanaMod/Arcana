package net.arcanamod.worldgen;

import net.arcanamod.Arcana;
import net.arcanamod.worldgen.trees.features.GreatwoodFoliagePlacer;
import net.arcanamod.worldgen.trees.features.LargeOakFoliagePlacer;
import net.arcanamod.worldgen.trees.features.SilverwoodFoliagePlacer;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.arcanamod.Arcana.MODID;
import static net.arcanamod.Arcana.arcLoc;

@Mod.EventBusSubscriber(modid = Arcana.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArcanaFeatures{
	
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, MODID);
	public static final DeferredRegister<FoliagePlacerType<?>> FOLAIGE_PLACERS = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, MODID);
	
	public static BaseTreeFeatureConfig GREATWOOD_TREE_CONFIG;
	public static BaseTreeFeatureConfig SILVERWOOD_TREE_CONFIG;
	public static BaseTreeFeatureConfig LARGE_OAK_TREE_CONFIG;
	
	public static BaseTreeFeatureConfig TAINTED_GREATWOOD_TREE_CONFIG;
	
	public static BaseTreeFeatureConfig TAINTED_OAK_TREE_CONFIG;
	public static BaseTreeFeatureConfig TAINTED_DARK_OAK_TREE_CONFIG;
	public static BaseTreeFeatureConfig TAINTED_BIRCH_TREE_CONFIG;
	public static BaseTreeFeatureConfig TAINTED_ACACIA_TREE_CONFIG;
	public static BaseTreeFeatureConfig TAINTED_SPRUCE_TREE_CONFIG;
	public static BaseTreeFeatureConfig TAINTED_PINE_TREE_CONFIG;
	public static BaseTreeFeatureConfig TAINTED_FANCY_OAK_TREE_CONFIG;
	public static BaseTreeFeatureConfig TAINTED_JUNGLE_TREE_CONFIG; // Only the No Vines variant, since the vines variant only spawns during worldgen
	public static BaseTreeFeatureConfig TAINTED_MEGA_JUNGLE_TREE_CONFIG;
	public static BaseTreeFeatureConfig TAINTED_MEGA_SPRUCE_TREE_CONFIG;
	public static BaseTreeFeatureConfig TAINTED_MEGA_PINE_TREE_CONFIG;
	// couldn't bother to make the bee trees too. sue me
	
	// features have to exist first because forge is stupid and insists on registering biomes first
	public static Feature<NoFeatureConfig> NODE = new NodeFeature(NoFeatureConfig.CODEC);
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> GREATWOOD_TREE;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> SILVERWOOD_TREE;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> LARGE_OAK_TREE;
	
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TAINTED_GREATWOOD_TREE;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TAINTED_OAK_TREE;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TAINTED_DARK_OAK_TREE;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TAINTED_BIRCH_TREE;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TAINTED_ACACIA_TREE;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TAINTED_SPRUCE_TREE;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TAINTED_PINE_TREE;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TAINTED_FANCY_OAK_TREE;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TAINTED_JUNGLE_TREE;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TAINTED_MEGA_JUNGLE_TREE;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TAINTED_MEGA_SPRUCE_TREE;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> TAINTED_MEGA_PINE_TREE;
	
	public static ConfiguredFeature<?, ?> MAGICAL_FOREST_BONUS_TREES;
	public static ConfiguredFeature<?, ?> MAGICAL_FOREST_GIANT_MUSHROOMS;
	public static ConfiguredFeature<?, ?> MAGIC_MUSHROOM_PATCH;
	
	public static RegistryObject<FoliagePlacerType<GreatwoodFoliagePlacer>> GREATWOOD_FOLIAGE = FOLAIGE_PLACERS.register("greatwood_foliage_placer", () -> new FoliagePlacerType<>(GreatwoodFoliagePlacer.CODEC));
	public static RegistryObject<FoliagePlacerType<SilverwoodFoliagePlacer>> SILVERWOOD_FOLIAGE = FOLAIGE_PLACERS.register("silverwood_foliage_placer", () -> new FoliagePlacerType<>(SilverwoodFoliagePlacer.CODEC));
	public static RegistryObject<FoliagePlacerType<LargeOakFoliagePlacer>> LARGE_OAK_FOLIAGE = FOLAIGE_PLACERS.register("large_oak_foliage_placer", () -> new FoliagePlacerType<>(LargeOakFoliagePlacer.CODEC));

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onItemRegister(BiomeLoadingEvent event){
		event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, ArcanaFeatures.NODE.withConfiguration(new NoFeatureConfig()).withPlacement(Placement.TOP_SOLID_HEIGHTMAP.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		
		if(event.getName().equals(arcLoc("magical_forest"))){
			event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ArcanaFeatures.MAGICAL_FOREST_BONUS_TREES);
			event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ArcanaFeatures.MAGICAL_FOREST_GIANT_MUSHROOMS);
			event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ArcanaFeatures.MAGIC_MUSHROOM_PATCH);
		}
	}
}