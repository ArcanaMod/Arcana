package net.arcanamod.client.render.particles;

import com.mojang.serialization.Codec;
import net.arcanamod.Arcana;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class ArcanaParticles{
	
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Arcana.MODID);
	
	public static final RegistryObject<ParticleType<NodeParticleData>> NODE_PARTICLE = PARTICLE_TYPES.register("node_particle", () -> create(NodeParticleData.DESERIALIZER, __ -> NodeParticleData.CODEC));
	public static final RegistryObject<ParticleType<AspectParticleData>> ASPECT_PARTICLE = PARTICLE_TYPES.register("aspect_particle", () -> create(AspectParticleData.DESERIALIZER, __ -> AspectParticleData.CODEC));
	public static final RegistryObject<ParticleType<AspectHelixParticleData>> ASPECT_HELIX_PARTICLE = PARTICLE_TYPES.register("aspect_helix_particle", () -> create(AspectHelixParticleData.DESERIALIZER, __ -> AspectHelixParticleData.CODEC));
	public static final RegistryObject<ParticleType<NumberParticleData>> NUMBER_PARTICLE = PARTICLE_TYPES.register("number_particle", () -> create(NumberParticleData.DESERIALIZER, __ -> NumberParticleData.CODEC));
	public static final RegistryObject<ParticleType<BlockParticleData>> HUNGRY_NODE_BLOCK_PARTICLE = PARTICLE_TYPES.register("hungry_node_block_particle", () -> create(BlockParticleData.DESERIALIZER, BlockParticleData::func_239800_a_));
	public static final RegistryObject<ParticleType<BlockParticleData>> HUNGRY_NODE_DISC_PARTICLE = PARTICLE_TYPES.register("hungry_node_disc_particle", () -> create(BlockParticleData.DESERIALIZER, BlockParticleData::func_239800_a_));
	
	@SuppressWarnings("deprecation")
	private static <T extends IParticleData> ParticleType<T> create(IParticleData.IDeserializer<T> deserializer, final Function<ParticleType<T>, Codec<T>> codec){
		return new ParticleType<T>(true, deserializer){
			@Nonnull
			public Codec<T> func_230522_e_(){
				return codec.apply(this);
			}
		};
	}
}