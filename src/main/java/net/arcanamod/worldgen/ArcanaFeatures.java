package net.arcanamod.worldgen;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.worldgen.trees.features.GreatwoodFoliagePlacer;
import net.arcanamod.worldgen.trees.features.GreatwoodTrunkPlacer;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
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
	
	// have to delay populating it until block registry is done because forge thonk
	//public static TreeFeatureConfig SILVERWOOD_TREE_CONFIG;
	public static BaseTreeFeatureConfig GREATWOOD_TREE_CONFIG;
	//public static HugeTreeFeatureConfig TAINTED_GREATWOOD_TREE_CONFIG;
	
	// features have to exist first because forge is stupid and insists on registering biomes first
	public static Feature<NoFeatureConfig> NODE = new NodeFeature(NoFeatureConfig.CODEC);
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> GREATWOOD_TREE;
	
	public static RegistryObject<FoliagePlacerType<GreatwoodFoliagePlacer>> GREATWOOD_FOLIAGE = FOLAIGE_PLACERS.register("greatwood_foliage_placer", () -> new FoliagePlacerType<>(GreatwoodFoliagePlacer.CODEC));
	
	public static void addMagicalForestTrees(Biome biome){
		// blocks must be registered first, but these configs have to be made at biome time, not feature time
		/*if(SILVERWOOD_TREE_CONFIG == null){
			SILVERWOOD_TREE_CONFIG = new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(ArcanaBlocks.SILVERWOOD_LOG.get().getDefaultState()), new SimpleBlockStateProvider(ArcanaBlocks.SILVERWOOD_LEAVES.get().getDefaultState()), new BlobFoliagePlacer(3, 1)).heightRandA(2).heightRandB(2).baseHeight(12).build();
			GREATWOOD_TREE_CONFIG = new HugeTreeFeatureConfig.Builder(new SimpleBlockStateProvider(ArcanaBlocks.GREATWOOD_LOG.get().getDefaultState()), new SimpleBlockStateProvider(ArcanaBlocks.GREATWOOD_LEAVES.get().getDefaultState())).setSapling(ArcanaBlocks.GREATWOOD_SAPLING.get()).baseHeight(18).build();
			TAINTED_GREATWOOD_TREE_CONFIG = new HugeTreeFeatureConfig.Builder(new SimpleBlockStateProvider(ArcanaBlocks.TAINTED_GREATWOOD_LOG.get().getDefaultState()), new SimpleBlockStateProvider(ArcanaBlocks.TAINTED_GREATWOOD_LEAVES.get().getDefaultState())).setSapling(ArcanaBlocks.TAINTED_GREATWOOD_SAPLING.get()).baseHeight(18).build();
		}*/
		
		//biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, SILVERWOOD_TREE.withConfiguration(SILVERWOOD_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(0, 0.04f, 1))));
		//biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, GREATWOOD_TREE.withConfiguration(GREATWOOD_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(0, 0.1f, 1))));
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onItemRegister(BiomeLoadingEvent event){
		event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, ArcanaFeatures.NODE.withConfiguration(new NoFeatureConfig()).withPlacement(Placement.TOP_SOLID_HEIGHTMAP.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
	}
}