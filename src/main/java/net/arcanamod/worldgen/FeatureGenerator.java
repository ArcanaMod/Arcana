package net.arcanamod.worldgen;

import net.arcanamod.ArcanaConfig;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class FeatureGenerator{
	
	//Not used ArcanaConfig.AMBER_MIN_VEIN_SIZE
	private static final CountRangeConfig AMBER_BLOCK_CONFIG = new CountRangeConfig(ArcanaConfig.AMBER_CHANCES_TO_SPAWN.get(), ArcanaConfig.AMBER_MIN_Y.get(), 0, ArcanaConfig.AMBER_MAX_Y.get());
	private static final ChanceConfig NODE_CONFIG = new ChanceConfig(1); // Probability config is used in NodeFeature.
	
	public static void setupFeatureGeneration(){
		for(Biome biome : ForgeRegistries.BIOMES)
			if(biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND){
				oreFeature(biome, OreFeatureConfig.FillerBlockType.NATURAL_STONE, ArcanaBlocks.AMBER_ORE.get(), AMBER_BLOCK_CONFIG, ArcanaConfig.AMBER_MAX_VEIN_SIZE.get());
				overworldNodeFeature(biome);
			}
	}
	
	/**
	 * Easy Ore Feature
	 *
	 * @param biome
	 * 		Biome where block/ore will be generated.
	 * @param fillerBlockType
	 * 		On which block will block/ore be generated.
	 * @param toGenerate
	 * 		Block/Ore that will be generated in the world.
	 * @param rangeConfig
	 * 		Config about where that ore will be generated.
	 * @param veinSize
	 * 		Size of vein.
	 */
	public static void oreFeature(Biome biome, OreFeatureConfig.FillerBlockType fillerBlockType, Block toGenerate, CountRangeConfig rangeConfig, int veinSize){
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, OreFeature.ORE.withConfiguration(new OreFeatureConfig(fillerBlockType, toGenerate.getDefaultState(), veinSize)).withPlacement(Placement.COUNT_RANGE.configure(rangeConfig)));
	}
	
	public static void overworldNodeFeature(Biome biome){
		biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, ArcanaFeatures.NODE.withConfiguration(new NoFeatureConfig()).withPlacement(Placement.CHANCE_TOP_SOLID_HEIGHTMAP.configure(NODE_CONFIG)));
	}
}