package net.arcanamod.client.render;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectParticle extends SpriteTexturedParticle {

	protected AspectParticle(World world, double x, double y, double z, TextureAtlasSprite sprite){
		super(world, x, y, z);
		particleGravity = 0;
		maxAge = 0;
		particleScale = .2f;
		canCollide = false;
		setSprite(sprite);
	}

	public IParticleRenderType getRenderType(){
		return IParticleRenderType.TERRAIN_SHEET;
	}

	@OnlyIn(Dist.CLIENT)
	@ParametersAreNonnullByDefault
	public static class Factory implements IParticleFactory<AspectParticleData> {

		public Particle makeParticle(AspectParticleData data, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed){
			return new AspectParticle(world, x, y, z, Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(data.aspectTexture));
		}
	}
}