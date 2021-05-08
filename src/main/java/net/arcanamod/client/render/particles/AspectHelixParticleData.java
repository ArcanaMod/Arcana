package net.arcanamod.client.render.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.util.Codecs;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectHelixParticleData implements IParticleData{
	
	public static final Codec<AspectHelixParticleData> CODEC = RecordCodecBuilder.create(o ->
			o.group(Codecs.ASPECT_CODEC.fieldOf("aspect")
							.forGetter(e -> e.aspect),
					Codec.INT.fieldOf("life")
							.forGetter(e -> e.life),
					Codec.FLOAT.fieldOf("time")
							.forGetter(e -> e.time),
					Codecs.VECTOR_3D_CODEC.fieldOf("direction")
							.forGetter(e -> e.direction))
					.apply(o, AspectHelixParticleData::new));
	
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
			return new AspectHelixParticleData(aspect, life, time, new Vector3d(x, y, z));
		}
		
		public AspectHelixParticleData read(ParticleType<AspectHelixParticleData> particleType, PacketBuffer buffer){
			return new AspectHelixParticleData(AspectUtils.getAspectByName(buffer.readString()), buffer.readInt(), buffer.readFloat(), new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()));
		}
	};
	
	private final Aspect aspect;
	private final int life;
	private final float time;
	private final Vector3d direction;
	
	public AspectHelixParticleData(@Nullable Aspect aspect, int life, float time, Vector3d direction){
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
	
	public Vector3d getDirection(){
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