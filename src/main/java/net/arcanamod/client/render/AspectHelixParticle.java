package net.arcanamod.client.render;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.particle.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectHelixParticle extends SpriteTexturedParticle{
	
	private float time;
	private final IAnimatedSprite spriteSheet;
	
	protected AspectHelixParticle(World world, double x, double y, double z, IAnimatedSprite spriteSheet, AspectHelixParticleData data){
		super(world, x, y, z);
		this.spriteSheet = spriteSheet;
		selectSpriteWithAge(spriteSheet);
		maxAge = data.getLife();
		time = data.getTime();
		canCollide = false;
		motionX = 0;
		motionY = .05;
		motionZ = 0;
		if(data.getAspect() != null){
			int colour = data.getAspect().getColorRange().get(0);
			int r = (colour & 0xff0000) >> 16;
			int g = (colour & 0xff00) >> 8;
			int b = colour & 0xff;
			particleRed = Math.min(r / 127f, 1);
			particleGreen = Math.min(g / 127f, 1);
			particleBlue = Math.min(b / 127f, 1);
		}
		setSize(0.02F, 0.02F);
		particleScale *= rand.nextFloat() * 0.3f + 0.7f;
	}
	
	public void tick(){
		selectSpriteWithAge(spriteSheet);
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if(age++ >= maxAge)
			setExpired();
		else{
			float f = .6f;
			motionX += f * MathHelper.cos(time);
			motionZ += f * MathHelper.sin(time);
			motionX *= .07;
			motionZ *= .07;
			move(motionX, motionY, motionZ);
			
			time += .2f;
		}
	}
	
	public IParticleRenderType getRenderType(){
		return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}
	
	public static class Factory implements IParticleFactory<AspectHelixParticleData>{
		private final IAnimatedSprite spriteSet;
		
		public Factory(IAnimatedSprite sheet){
			this.spriteSet = sheet;
		}
		
		public Particle makeParticle(AspectHelixParticleData data, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed){
			return new AspectHelixParticle(world, x, y, z, spriteSet, data);
		}
	}
}