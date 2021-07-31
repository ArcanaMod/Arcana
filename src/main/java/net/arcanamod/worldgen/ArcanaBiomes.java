package net.arcanamod.worldgen;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.arcanamod.Arcana.MODID;
import static net.minecraft.world.biome.DefaultBiomeFeatures.*;

public class ArcanaBiomes{
	
	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, MODID);
	
	public static final RegistryObject<Biome> MAGICAL_FOREST = BIOMES.register("magical_forest", ArcanaBiomes::makeMagicalForestBiome);
	
	private static Biome makeMagicalForestBiome(){
		BiomeGenerationSettings.Builder settings = (new BiomeGenerationSettings.Builder()).withSurfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
		withStrongholdAndMineshaft(settings);
		settings.withStructure(StructureFeatures.RUINED_PORTAL);
		withCavesAndCanyons(settings);
		withLavaAndWaterLakes(settings);
		withMonsterRoom(settings);
		withAllForestFlowerGeneration(settings);
		withCommonOverworldBlocks(settings);
		withOverworldOres(settings);
		withDisks(settings);
		withDefaultFlowers(settings);
		withForestGrass(settings);
		withNormalMushroomGeneration(settings);
		withSugarCaneAndPumpkins(settings);
		withLavaAndWaterSprings(settings);
		withBirchTrees(settings);

		
		MobSpawnInfo.Builder mobSpawnBuilder = getStandardMobSpawnBuilder()
				.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.WOLF, 5, 4, 4))
				.isValidSpawnBiomeForPlayer();
		
		Biome biome = (new Biome.Builder())
				.precipitation(Biome.RainType.RAIN)
				.category(Biome.Category.FOREST)
				.depth(.2f)
				.scale(.3f)
				.temperature(.6f)
				.downfall(.9f)
				.setEffects((new BiomeAmbience.Builder())
						.withGrassColor(0x7ff3ac)
						.setWaterColor(0x3f76e4)
						.setWaterFogColor(0x50533)
						.setFogColor(0xc0d8ff)
						.withSkyColor(getSkyColorWithTemperatureModifier(.7f))
						.setMoodSound(MoodSoundAmbience.DEFAULT_CAVE).build())
				.withMobSpawnSettings(mobSpawnBuilder.build())
				.withGenerationSettings(settings.build())
				.build();
		
		RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, MAGICAL_FOREST.getId());
		BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(key, 5));
		BiomeDictionary.addTypes(key, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.OVERWORLD, BiomeDictionary.Type.MAGICAL);

		
		return biome;
	}
	
	private static int getSkyColorWithTemperatureModifier(float temperature) {
		float temp = temperature / 3;
		temp = MathHelper.clamp(temp, -1, 1);
		return MathHelper.hsvToRGB(0.62222224F - temp * .05f, .5f + temp * .1f, 1);
	}
	
	private static MobSpawnInfo.Builder getStandardMobSpawnBuilder() {
		MobSpawnInfo.Builder mobspawninfo$builder = new MobSpawnInfo.Builder();
		DefaultBiomeFeatures.withPassiveMobs(mobspawninfo$builder);
		DefaultBiomeFeatures.withBatsAndHostiles(mobspawninfo$builder);
		return mobspawninfo$builder;
	}
}