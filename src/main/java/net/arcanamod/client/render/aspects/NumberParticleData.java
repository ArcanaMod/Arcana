package net.arcanamod.client.render.aspects;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class NumberParticleData implements IParticleData {

	public static final IParticleData.IDeserializer<NumberParticleData> DESERIALIZER = new IParticleData.IDeserializer<NumberParticleData>() {
		public NumberParticleData deserialize(ParticleType<NumberParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			String iloc = reader.getRemaining();
			return new NumberParticleData(Integer.parseInt(iloc), particleType);
		}

		public NumberParticleData read(ParticleType<NumberParticleData> particleType, PacketBuffer buffer) {
			return new NumberParticleData(buffer.readInt(), particleType);
		}
	};

	int count;
	ParticleType<NumberParticleData> type;

	public NumberParticleData(int count, ParticleType<NumberParticleData> type){
		this.type = type;
		this.count = count;
	}

	public ParticleType<?> getType(){
		return type;
	}

	public void write(PacketBuffer buffer){
		buffer.writeInt(count);
	}

	public String getParameters(){
		return Objects.requireNonNull(ForgeRegistries.PARTICLE_TYPES.getKey(this.getType())).toString() + " " + count;
	}
}
