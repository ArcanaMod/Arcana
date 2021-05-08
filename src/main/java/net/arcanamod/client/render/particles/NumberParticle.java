package net.arcanamod.client.render.particles;

import net.arcanamod.Arcana;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class NumberParticle extends SpriteTexturedParticle{
	
	protected NumberParticle(ClientWorld world, double x, double y, double z, TextureAtlasSprite sprite){
		super(world, x, y, z);
		particleGravity = 0;
		maxAge = 0;
		particleScale = .04f;
		canCollide = false;
		setSprite(sprite);
	}
	
	@Nonnull
	public IParticleRenderType getRenderType(){
		return IParticleRenderType.TERRAIN_SHEET;
	}
	
	protected int getBrightnessForRender(float partialTick){
		// fullbright
		return 0xf000f0;
	}
	
	@OnlyIn(Dist.CLIENT)
	@ParametersAreNonnullByDefault
	public static class Factory implements IParticleFactory<NumberParticleData>{
		@SuppressWarnings("deprecation")
		public Particle makeParticle(NumberParticleData data, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed){
			ResourceLocation countTex = Arcana.arcLoc("font/number_" + data.count);
			return new NumberParticle(world, x, y, z, Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(countTex));
		}
	}
}
