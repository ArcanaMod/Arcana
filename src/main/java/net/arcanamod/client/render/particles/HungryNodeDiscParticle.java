package net.arcanamod.client.render.particles;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HungryNodeDiscParticle extends SpriteTexturedParticle{
	
	private final BlockState sourceState;
	private BlockPos sourcePos;
	private final float u;
	private final float v;
	
	protected HungryNodeDiscParticle(ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state){
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
		sourceState = state;
		motionX = motionX * 0.01f + xSpeed;
		motionY = motionY * 0.01f + ySpeed;
		motionZ = motionZ * 0.01f + zSpeed;
		posX += (rand.nextFloat() - rand.nextFloat()) * .05f;
		posY += (rand.nextGaussian()) * .2f;
		posZ += (rand.nextFloat() - rand.nextFloat()) * .05f;
		particleRed = .6f;
		particleGreen = .6f;
		particleBlue = .6f;
		particleScale /= 2;
		u = rand.nextFloat() * 3;
		v = rand.nextFloat() * 3;
		maxAge = 120;
	}
	
	@Nonnull
	public IParticleRenderType getRenderType(){
		return IParticleRenderType.TERRAIN_SHEET;
	}
	
	public void move(double x, double y, double z){
		setBoundingBox(getBoundingBox().offset(x, y, z));
		resetPositionToBB();
	}
	
	public void tick(){
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if(age++ >= maxAge)
			setExpired();
		else{
			float time = age / 6f;
			// TODO: allow off-axis rings
			move(MathHelper.cos(time) * motionX, motionY, MathHelper.sin(time) * motionZ);
		}
	}
	
	public HungryNodeDiscParticle setBlockPos(BlockPos pos){
		updateSprite(pos);
		sourcePos = pos;
		if(sourceState.getBlock() != Blocks.GRASS_BLOCK)
			multiplyColor(pos);
		return this;
	}
	
	public HungryNodeDiscParticle init(){
		sourcePos = new BlockPos(posX, posY, posZ);
		Block block = sourceState.getBlock();
		if(block != Blocks.GRASS_BLOCK)
			multiplyColor(sourcePos);
		return this;
	}
	
	protected void multiplyColor(@Nullable BlockPos pos){
		int i = Minecraft.getInstance().getBlockColors().getColor(sourceState, world, pos, 0);
		particleRed *= (float)(i >> 16 & 255) / 255f;
		particleGreen *= (float)(i >> 8 & 255) / 255f;
		particleBlue *= (float)(i & 255) / 255f;
	}
	
	protected float getMinU(){
		return sprite.getInterpolatedU((u + 1) / 4f * 16f);
	}
	
	protected float getMaxU(){
		return sprite.getInterpolatedU(u / 4f * 16f);
	}
	
	protected float getMinV(){
		return sprite.getInterpolatedV(v / 4f * 16f);
	}
	
	protected float getMaxV(){
		return sprite.getInterpolatedV((v + 1) / 4f * 16f);
	}
	
	private HungryNodeDiscParticle updateSprite(BlockPos pos){
		if(pos != null)
			setSprite(Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getTexture(sourceState, world, pos));
		return this;
	}
	
	public static class Factory implements IParticleFactory<BlockParticleData>{
		@SuppressWarnings("deprecation")
		public Particle makeParticle(BlockParticleData type, @Nonnull ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			BlockState blockstate = type.getBlockState();
			return !blockstate.isAir() && blockstate.getBlock() != Blocks.MOVING_PISTON ? (new HungryNodeDiscParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, blockstate)).init().updateSprite(type.getPos()) : null;
		}
	}
}