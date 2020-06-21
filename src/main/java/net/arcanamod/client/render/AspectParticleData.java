package net.arcanamod.client.render;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class AspectParticleData implements IParticleData {
	
	public static final IParticleData.IDeserializer<AspectParticleData> DESERIALIZER = new IParticleData.IDeserializer<AspectParticleData>() {
		public AspectParticleData deserialize(ParticleType<AspectParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			ResourceLocation rloc = new ResourceLocation(reader.getRemaining());
			return new AspectParticleData(rloc, particleType);
		}

		public AspectParticleData read(ParticleType<AspectParticleData> particleType, PacketBuffer buffer) {
			return new AspectParticleData(buffer.readResourceLocation(), particleType);
		}
	};

	ResourceLocation aspectTexture;
	ParticleType<AspectParticleData> type;

	public AspectParticleData(ResourceLocation aspectTexture, ParticleType<AspectParticleData> type){
		this.type = type;
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
