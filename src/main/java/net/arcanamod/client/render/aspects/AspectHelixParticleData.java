package net.arcanamod.client.render.aspects;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.Vec3d;
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
			reader.expect(' ');
			reader.expect('[');
			double x = reader.readDouble();
			reader.expect(',');
			reader.skipWhitespace();
			double y = reader.readDouble();
			reader.expect(',');
			reader.skipWhitespace();
			double z = reader.readDouble();
			reader.expect(']');
			return new AspectHelixParticleData(aspect, life, time, new Vec3d(x, y, z));
		}
		
		public AspectHelixParticleData read(ParticleType<AspectHelixParticleData> particleType, PacketBuffer buffer){
			return new AspectHelixParticleData(AspectUtils.getAspectByName(buffer.readString()), buffer.readInt(), buffer.readFloat(), new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()));
		}
	};
	
	private final Aspect aspect;
	private final int life;
	private final float time;
	private final Vec3d direction;
	
	public AspectHelixParticleData(@Nullable Aspect aspect, int life, float time, Vec3d direction){
		this.aspect = aspect;
		this.life = life;
		this.time = time;
		this.direction = direction;
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
	
	public Vec3d getDirection(){
		return direction;
	}
	
	public ParticleType<?> getType(){
		return ArcanaParticles.ASPECT_HELIX_PARTICLE.get();
	}
	
	public void write(PacketBuffer buffer){
		buffer.writeString(aspect != null ? aspect.name() : "null");
		buffer.writeInt(life);
		buffer.writeFloat(time);
		buffer.writeDouble(direction.x);
		buffer.writeDouble(direction.y);
		buffer.writeDouble(direction.z);
	}
	
	public String getParameters(){
		// expose proper parameters at some point
		return ForgeRegistries.PARTICLE_TYPES.getKey(getType()).toString();
	}
}