package net.arcanamod.client.render.particles;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.util.LocalAxis;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectHelixParticle extends SpriteTexturedParticle{
	
	private float time;
	private final IAnimatedSprite spriteSheet;
	private final Vector3d direction;
	
	protected AspectHelixParticle(ClientWorld world, double x, double y, double z, IAnimatedSprite spriteSheet, AspectHelixParticleData data){
		super(world, x, y, z);
		this.spriteSheet = spriteSheet;
		selectSpriteWithAge(spriteSheet);
		maxAge = data.getLife();
		time = data.getTime();
		direction = data.getDirection();
		canCollide = false;
		motionX = direction.x * .05;
		motionY = direction.y * .05;
		motionZ = direction.z * .05;
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
			float f = .05f;
			float x1 = f * MathHelper.cos(time);
			float z1 = f * MathHelper.sin(time);
			
			// FIXME: this, uhh, doesn't work, I'm working on it
			Vector3d motion = LocalAxis.toAbsolutePos(new Vector3d(x1, f, z1), direction, Vector3d.ZERO);
			move(motion.x, motion.y, motion.z);
			
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
		
		public Particle makeParticle(AspectHelixParticleData data, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed){
			return new AspectHelixParticle(world, x, y, z, spriteSet, data);
		}
	}
}