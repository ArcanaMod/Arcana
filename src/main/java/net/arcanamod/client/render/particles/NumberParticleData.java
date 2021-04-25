package net.arcanamod.client.render.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NumberParticleData implements IParticleData{
	
	public static final IParticleData.IDeserializer<NumberParticleData> DESERIALIZER = new IParticleData.IDeserializer<NumberParticleData>(){
		public NumberParticleData deserialize(ParticleType<NumberParticleData> particleType, StringReader reader) throws CommandSyntaxException{
			reader.expect(' ');
			char c = reader.getRemaining().charAt(0);
			return new NumberParticleData(c, particleType);
		}
		
		public NumberParticleData read(ParticleType<NumberParticleData> particleType, PacketBuffer buffer){
			return new NumberParticleData(buffer.readChar(), particleType);
		}
	};
	
	char count;
	ParticleType<NumberParticleData> type;
	
	public NumberParticleData(char c, ParticleType<NumberParticleData> type){
		this.type = type;
		this.count = c;
	}
	
	public ParticleType<?> getType(){
		return type;
	}
	
	public void write(PacketBuffer buffer){
		buffer.writeChar(count);
	}
	
	public String getParameters(){
		return Objects.requireNonNull(ForgeRegistries.PARTICLE_TYPES.getKey(this.getType())).toString() + " " + count;
	}
}
