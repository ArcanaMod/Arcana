package net.arcanamod.worldgen;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.arcanamod.Arcana.MODID;

public class ArcanaBiomes{
	
	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, MODID);
	
	//public static final RegistryObject<Biome> MAGICAL_FOREST = BIOMES.register("magical_forest", MagicalForestBiome::new);
}