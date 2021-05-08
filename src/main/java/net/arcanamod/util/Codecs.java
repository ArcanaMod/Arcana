package net.arcanamod.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.client.render.particles.NodeParticleData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import java.util.UUID;

public final class Codecs{
	
	private Codecs(){}
	
	public static final Codec<UUID> UUID_CODEC = Codec.STRING.xmap(UUID::fromString, UUID::toString);
	public static final Codec<Aspect> ASPECT_CODEC = ResourceLocation.CODEC.xmap(Aspects.ASPECTS::get, Aspects.ASPECT_IDS::get);
	
	public static final Codec<Vector3d> VECTOR_3D_CODEC = RecordCodecBuilder.create(o ->
			o.group(Codec.DOUBLE.fieldOf("x")
							.forGetter(e -> e.x),
					Codec.DOUBLE.fieldOf("y")
							.forGetter(e -> e.y),
					Codec.DOUBLE.fieldOf("z")
							.forGetter(e -> e.z))
					.apply(o, Vector3d::new));
}