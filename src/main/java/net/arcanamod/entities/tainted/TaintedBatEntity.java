package net.arcanamod.entities.tainted;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Random;

// TODO: Bats attack player
public class TaintedBatEntity extends CreatureEntity {
	public TaintedBatEntity(EntityType<? extends Entity> type, World worldIn) {
		super((EntityType<? extends TaintedBatEntity>) type, worldIn);
		this.setIsBatHanging(true);
	}

	private static final DataParameter<Byte> HANGING = EntityDataManager.createKey(BatEntity.class, DataSerializers.BYTE);
	private static final EntityPredicate field_213813_c = (new EntityPredicate()).setDistance(4.0D).allowFriendlyFire();
	private BlockPos spawnPosition;

	protected void registerData() {
		super.registerData();
		this.dataManager.register(HANGING, (byte)0);
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume() {
		return 0.1F;
	}

	/**
	 * Gets the pitch of living sounds in living entities.
	 */
	protected float getSoundPitch() {
		return super.getSoundPitch() * 0.95F;
	}

	@Nullable
	public SoundEvent getAmbientSound() {
		return this.getIsBatHanging() && this.rand.nextInt(4) != 0 ? null : SoundEvents.ENTITY_BAT_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_BAT_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_BAT_DEATH;
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities when colliding.
	 */
	public boolean canBePushed() {
		return false;
	}

	protected void collideWithEntity(Entity entityIn) {
	}

	protected void collideWithNearbyEntities() {
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.registerAttributes().createMutableAttribute(Attributes.MAX_HEALTH, 6.0D);
	}

	public boolean getIsBatHanging() {
		return (this.dataManager.get(HANGING) & 1) != 0;
	}

	public void setIsBatHanging(boolean isHanging) {
		byte b0 = this.dataManager.get(HANGING);
		if (isHanging) {
			this.dataManager.set(HANGING, (byte)(b0 | 1));
		} else {
			this.dataManager.set(HANGING, (byte)(b0 & -2));
		}

	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void tick() {
		super.tick();
		if (this.getIsBatHanging()) {
			this.setMotion(Vector3d.ZERO);
			this.setRawPosition(this.getPosX(), (double) MathHelper.floor(this.getPosY()) + 1.0D - (double)this.getHeight(), this.getPosZ());
		} else {
			this.setMotion(this.getMotion().mul(1.0D, 0.6D, 1.0D));
		}

	}

	protected void updateAITasks() {
		super.updateAITasks();
		BlockPos blockpos = new BlockPos(this.getPosition());
		BlockPos blockpos1 = blockpos.up();
		if (this.getIsBatHanging()) {
			if (this.world.getBlockState(blockpos1).isNormalCube(this.world, blockpos)) {
				if (this.rand.nextInt(200) == 0) {
					this.rotationYawHead = (float)this.rand.nextInt(360);
				}

				if (this.world.getClosestPlayer(field_213813_c, this) != null) {
					this.setIsBatHanging(false);
				}
			} else {
				this.setIsBatHanging(false);
			}
		} else {
			if (this.spawnPosition != null && (!this.world.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
				this.spawnPosition = null;
			}

			if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.withinDistance(this.getPositionVec(), 2.0D)) {
				this.spawnPosition = new BlockPos(this.getPosX() + (double)this.rand.nextInt(7) - (double)this.rand.nextInt(7), this.getPosY() + (double)this.rand.nextInt(6) - 2.0D, this.getPosZ() + (double)this.rand.nextInt(7) - (double)this.rand.nextInt(7));
			}

			double d0 = (double)this.spawnPosition.getX() + 0.5D - this.getPosX();
			double d1 = (double)this.spawnPosition.getY() + 0.1D - this.getPosY();
			double d2 = (double)this.spawnPosition.getZ() + 0.5D - this.getPosZ();
			Vector3d vec3d = this.getMotion();
			Vector3d vec3d1 = vec3d.add((Math.signum(d0) * 0.5D - vec3d.x) * (double)0.1F, (Math.signum(d1) * (double)0.7F - vec3d.y) * (double)0.1F, (Math.signum(d2) * 0.5D - vec3d.z) * (double)0.1F);
			this.setMotion(vec3d1);
			float f = (float)(MathHelper.atan2(vec3d1.z, vec3d1.x) * (double)(180F / (float)Math.PI)) - 90.0F;
			float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
			this.moveForward = 0.5F;
			this.rotationYaw += f1;
			if (this.rand.nextInt(100) == 0 && this.world.getBlockState(blockpos1).isNormalCube(this.world, blockpos1)) {
				this.setIsBatHanging(true);
			}
		}

	}

	protected boolean canTriggerWalking() {
		return false;
	}

	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}

	protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	/**
	 * Return whether this entity should NOT trigger a pressure plate or a tripwire.
	 */
	public boolean doesEntityNotTriggerPressurePlate() {
		return true;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			if (!this.world.isRemote && this.getIsBatHanging()) {
				this.setIsBatHanging(false);
			}

			return super.attackEntityFrom(source, amount);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.dataManager.set(HANGING, compound.getByte("BatFlags"));
	}

	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putByte("BatFlags", this.dataManager.get(HANGING));
	}

	public static boolean func_223369_b(EntityType<BatEntity> p_223369_0_, IWorld p_223369_1_, SpawnReason reason, BlockPos p_223369_3_, Random p_223369_4_) {
		if (p_223369_3_.getY() >= p_223369_1_.getSeaLevel()) {
			return false;
		} else {
			int i = p_223369_1_.getLight(p_223369_3_);
			int j = 4;
			if (isNearHalloween()) {
				j = 7;
			} else if (p_223369_4_.nextBoolean()) {
				return false;
			}

			return i > p_223369_4_.nextInt(j) ? false : canSpawnOn(p_223369_0_, p_223369_1_, reason, p_223369_3_, p_223369_4_);
		}
	}

	private static boolean isNearHalloween() {
		LocalDate localdate = LocalDate.now();
		int i = localdate.get(ChronoField.DAY_OF_MONTH);
		int j = localdate.get(ChronoField.MONTH_OF_YEAR);
		return j == 10 && i >= 20 || j == 11 && i <= 3;
	}

	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height / 2.0F;
	}
}