package net.arcanamod.client.render;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectHelixParticleData implements IParticleData{
	
	public static final IParticleData.IDeserializer<AspectHelixParticleData> DESERIALIZER = new IDeserializer<AspectHelixParticleData>(){
		public AspectHelixParticleData deserialize(ParticleType<AspectHelixParticleData> particleType, StringReader reader) throws CommandSyntaxException{
			reader.expect(' ');
			Aspect aspect = AspectUtils.getAspectByName(reader.readStringUntil(' '));
			int life = reader.readInt();
			reader.expect(' ');
			float time = reader.readFloat();
			return new AspectHelixParticleData(aspect, life, time);
		}
		
		public AspectHelixParticleData read(ParticleType<AspectHelixParticleData> particleType, PacketBuffer buffer){
			return new AspectHelixParticleData(AspectUtils.getAspectByName(buffer.readString()), buffer.readInt(), buffer.readFloat());
		}
	};
	
	private final Aspect aspect;
	private final int life;
	private final float time;
	
	public AspectHelixParticleData(@Nullable Aspect aspect, int life, float time){
		this.aspect = aspect;
		this.life = life;
		this.time = time;
	}
	
	@Nullable
	public Aspect getAspect(){
		return aspect;
	}
	
	public int getLife(){
		return life;
	}
	
	public float getTime(){
		return time;
	}
	
	public ParticleType<?> getType(){
		return ArcanaParticles.ASPECT_HELIX_PARTICLE.get();
	}
	
	public void write(PacketBuffer buffer){
		buffer.writeString(aspect != null ? aspect.name() : "null");
		buffer.writeInt(life);
		buffer.writeFloat(time);
	}
	
	public String getParameters(){
		// expose proper parameters at some point
		return ForgeRegistries.PARTICLE_TYPES.getKey(getType()).toString();
	}
}