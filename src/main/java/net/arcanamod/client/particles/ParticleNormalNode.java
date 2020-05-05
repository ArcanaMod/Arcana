package net.arcanamod.client.particles;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import javafx.geometry.Side;
import net.arcanamod.client.Sprites;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.world.World;

public class ParticleNormalNode extends Particle{
	
	public ParticleNormalNode(World worldIn, double posXIn, double posYIn, double posZIn){
		super(worldIn, posXIn, posYIn, posZIn);
		//setParticleTexture(Sprites.NORMAL_NODE);
		canCollide = false;
		particleGravity = 0.0F;
		setMaxAge(1);
		motionX = 0;
		motionY = 0;
		motionZ = 0;
		//particleScale = 3f;
	}
	
	public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks){
	
	}
	
	public IParticleRenderType getRenderType(){
		return null;
	}
	
	/*public static class Factory implements IParticleFactory{
		public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_){
			return new ParticleNormalNode(worldIn, xCoordIn, yCoordIn, zCoordIn);
		}
	}*/
	
	//@Override
	public int getFXLayer(){
		return 1;
	}
	
	@Override
	public int getBrightnessForRender(float p_189214_1_){
		return 3000;
	}
	
}

