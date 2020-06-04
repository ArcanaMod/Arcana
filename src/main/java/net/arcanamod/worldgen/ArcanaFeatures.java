package net.arcanamod.worldgen;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.arcanamod.Arcana.MODID;

public class ArcanaFeatures{
	
	public static final DeferredRegister<Feature<?>> FEATURES = new DeferredRegister<>(ForgeRegistries.FEATURES, MODID);
	
	public static final RegistryObject<Feature<NoFeatureConfig>> NODE = FEATURES.register("node", () -> new NodeFeature(NoFeatureConfig::deserialize));
}