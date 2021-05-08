package net.arcanamod.client.render.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.util.Codecs;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectParticleData implements IParticleData {
	
	public static final Codec<AspectParticleData> CODEC = RecordCodecBuilder.create(o ->
			o.group(ResourceLocation.CODEC.fieldOf("aspectTexture")
							.forGetter(e -> e.aspectTexture))
					.apply(o, AspectParticleData::new));
	
	public static final IParticleData.IDeserializer<AspectParticleData> DESERIALIZER = new IParticleData.IDeserializer<AspectParticleData>() {
		public AspectParticleData deserialize(ParticleType<AspectParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			ResourceLocation rloc = new ResourceLocation(reader.getRemaining());
			return new AspectParticleData(rloc);
		}

		public AspectParticleData read(ParticleType<AspectParticleData> particleType, PacketBuffer buffer) {
			return new AspectParticleData(buffer.readResourceLocation());
		}
	};

	ResourceLocation aspectTexture;
	ParticleType<AspectParticleData> type;

	public AspectParticleData(ResourceLocation aspectTexture){
		this.type = ArcanaParticles.ASPECT_PARTICLE.get();
		this.aspectTexture = aspectTexture;
	}

	public ParticleType<?> getType(){
		return type;
	}

	public void write(PacketBuffer buffer){
		buffer.writeResourceLocation(aspectTexture);
	}

	public String getParameters(){
		return Objects.requireNonNull(ForgeRegistries.PARTICLE_TYPES.getKey(this.getType())).toString() + " " + aspectTexture.toString();
	}
}
