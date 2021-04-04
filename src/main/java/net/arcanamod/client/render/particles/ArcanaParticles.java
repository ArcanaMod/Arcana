package net.arcanamod.client.render.particles;

import net.arcanamod.Arcana;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ArcanaParticles{
	
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = new DeferredRegister<>(ForgeRegistries.PARTICLE_TYPES, Arcana.MODID);
	
	public static final RegistryObject<ParticleType<NodeParticleData>> NODE_PARTICLE = PARTICLE_TYPES.register("node_particle", () -> new ParticleType<>(true, NodeParticleData.DESERIALIZER));
	public static final RegistryObject<ParticleType<AspectParticleData>> ASPECT_PARTICLE = PARTICLE_TYPES.register("aspect_particle", () -> new ParticleType<>(true, AspectParticleData.DESERIALIZER));
	public static final RegistryObject<ParticleType<AspectHelixParticleData>> ASPECT_HELIX_PARTICLE = PARTICLE_TYPES.register("aspect_helix_particle", () -> new ParticleType<>(true, AspectHelixParticleData.DESERIALIZER));
	public static final RegistryObject<ParticleType<NumberParticleData>> NUMBER_PARTICLE = PARTICLE_TYPES.register("number_particle", () -> new ParticleType<>(true, NumberParticleData.DESERIALIZER));
	public static final RegistryObject<ParticleType<BlockParticleData>> HUNGRY_NODE_BLOCK_PARTICLE = PARTICLE_TYPES.register("hungry_node_block_particle", () -> new ParticleType<>(true, BlockParticleData.DESERIALIZER));
}