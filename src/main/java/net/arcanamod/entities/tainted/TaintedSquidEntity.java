package net.arcanamod.entities.tainted;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TaintedSquidEntity extends WaterMobEntity{
	public float squidPitch;
	public float prevSquidPitch;
	public float squidYaw;
	public float prevSquidYaw;
	public float squidRotation;
	public float prevSquidRotation;
	public float tentacleAngle;
	public float lastTentacleAngle;
	private float randomMotionSpeed;
	private float rotationVelocity;
	private float rotateSpeed;
	private float randomMotionVecX;
	private float randomMotionVecY;
	private float randomMotionVecZ;
	
	public TaintedSquidEntity(EntityType<? extends TaintedSquidEntity> type, World world){
		super(type, world);
		rand.setSeed(getEntityId());
		rotationVelocity = 1 / (rand.nextFloat() + 1) * .2f;
	}
	
	protected void registerGoals(){
		goalSelector.addGoal(0, new MoveRandomGoal(this));
		goalSelector.addGoal(1, new MeleeAttackGoal(this, 1f, false));
	}
	
	protected void registerAttributes(){
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
	}
	
	protected float getStandingEyeHeight(Pose pose, EntitySize size){
		return size.height * .5f;
	}
	
	protected SoundEvent getAmbientSound(){
		return SoundEvents.ENTITY_SQUID_AMBIENT;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSource){
		return SoundEvents.ENTITY_SQUID_HURT;
	}
	
	protected SoundEvent getDeathSound(){
		return SoundEvents.ENTITY_SQUID_DEATH;
	}
	
	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume(){
		return .4f;
	}
	
	protected boolean canTriggerWalking(){
		return false;
	}
	
	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	public void livingTick(){
		super.livingTick();
		prevSquidPitch = squidPitch;
		prevSquidYaw = squidYaw;
		prevSquidRotation = squidRotation;
		lastTentacleAngle = tentacleAngle;
		squidRotation += rotationVelocity;
		if(squidRotation > Math.PI * 2D)
			if(world.isRemote)
				squidRotation = (float)Math.PI * 2;
			else{
				squidRotation = (float)(squidRotation - (Math.PI * 2D));
				if(rand.nextInt(10) == 0)
					rotationVelocity = 1 / (rand.nextFloat() + 1) * .2f;
				
				world.setEntityState(this, (byte)19);
			}
		
		if(this.isInWaterOrBubbleColumn()){
			if(squidRotation < (float)Math.PI){
				float f = squidRotation / (float)Math.PI;
				tentacleAngle = MathHelper.sin(f * f * (float)Math.PI) * (float)Math.PI * .25f;
				if(f > .75){
					randomMotionSpeed = 1.0F;
					rotateSpeed = 1.0F;
				}else
					rotateSpeed *= 0.8F;
			}else{
				tentacleAngle = 0.0F;
				randomMotionSpeed *= 0.9F;
				rotateSpeed *= 0.99F;
			}
			
			if(!world.isRemote)
				setMotion(randomMotionVecX * randomMotionSpeed, randomMotionVecY * randomMotionSpeed, randomMotionVecZ * randomMotionSpeed);
			
			Vec3d vec3d = getMotion();
			float f1 = MathHelper.sqrt(horizontalMag(vec3d));
			renderYawOffset += (-((float)MathHelper.atan2(vec3d.x, vec3d.z)) * (180F / (float)Math.PI) - renderYawOffset) * 0.1F;
			rotationYaw = renderYawOffset;
			squidYaw = (float)(squidYaw + Math.PI * rotateSpeed * 1.5);
			squidPitch += -((float)MathHelper.atan2(f1, vec3d.y)) * (180 / (float)Math.PI) * .1f - squidPitch * .1f;
		}else{
			tentacleAngle = MathHelper.abs(MathHelper.sin(squidRotation)) * (float)Math.PI * 0.25F;
			if(!world.isRemote){
				double d0 = getMotion().y;
				if(isPotionActive(Effects.LEVITATION))
					d0 = .05 * (getActivePotionEffect(Effects.LEVITATION).getAmplifier() + 1);
				else if(!hasNoGravity())
					d0 -= .08;
				setMotion(0, d0 * 0.98, 0);
			}
			
			squidPitch = (float)(squidPitch - 90 * .02 - squidPitch * .02);
		}
		
	}
	
	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource source, float amount){
		if(super.attackEntityFrom(source, amount) && getRevengeTarget() != null){
			squirtInk();
			return true;
		}else{
			return false;
		}
	}
	
	private Vec3d func_207400_b(Vec3d vec){
		Vec3d vec3d = vec.rotatePitch(prevSquidPitch * ((float)Math.PI / 180f));
		vec3d = vec3d.rotateYaw(-prevRenderYawOffset * ((float)Math.PI / 180f));
		return vec3d;
	}
	
	private void squirtInk(){
		playSound(SoundEvents.ENTITY_SQUID_SQUIRT, getSoundVolume(), getSoundPitch());
		Vec3d vec3d = func_207400_b(new Vec3d(0.0D, -1.0D, 0.0D)).add(getPosX(), getPosY(), getPosZ());
		
		for(int i = 0; i < 30; ++i){
			Vec3d vec3d1 = func_207400_b(new Vec3d(rand.nextFloat() * .6 - .3, -1, rand.nextFloat() * .6 - .3));
			Vec3d vec3d2 = vec3d1.scale(.3 + (rand.nextFloat() * 2));
			((ServerWorld)world).spawnParticle(ParticleTypes.SQUID_INK, vec3d.x, vec3d.y + .5, vec3d.z, 0, vec3d2.x, vec3d2.y, vec3d2.z, .1);
		}
	}
	
	public void travel(Vec3d to){
		move(MoverType.SELF, getMotion());
	}

	/*public static boolean func_223365_b(EntityType<TaintedSquidEntity> p_223365_0_, IWorld p_223365_1_, SpawnReason reason, BlockPos p_223365_3_, Random p_223365_4_) {
		return p_223365_3_.getY() > 45 && p_223365_3_.getY() < p_223365_1_.getSeaLevel();
	}*/
	
	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id){
		if(id == 19)
			squidRotation = 0;
		else
			super.handleStatusUpdate(id);
	}
	
	public void setMovementVector(float randomMotionVecX, float randomMotionVecY, float randomMotionVecZ){
		this.randomMotionVecX = randomMotionVecX;
		this.randomMotionVecY = randomMotionVecY;
		this.randomMotionVecZ = randomMotionVecZ;
	}
	
	public boolean hasMovementVector(){
		return randomMotionVecX != 0.0F || randomMotionVecY != 0.0F || randomMotionVecZ != 0.0F;
	}
	
	static class MoveRandomGoal extends Goal{
		
		private final TaintedSquidEntity squid;
		
		public MoveRandomGoal(TaintedSquidEntity squid){
			this.squid = squid;
		}
		
		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean shouldExecute(){
			return true;
		}
		
		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick(){
			int i = squid.getIdleTime();
			if(i > 100)
				squid.setMovementVector(0.0F, 0.0F, 0.0F);
			else if(squid.getRNG().nextInt(50) == 0 || !squid.inWater || !squid.hasMovementVector()){
				float f = squid.getRNG().nextFloat() * ((float)Math.PI * 2);
				float f1 = MathHelper.cos(f) * .2f;
				float f2 = -.1f + squid.getRNG().nextFloat() * .2f;
				float f3 = MathHelper.sin(f) * .2f;
				squid.setMovementVector(f1, f2, f3);
			}
		}
	}
}