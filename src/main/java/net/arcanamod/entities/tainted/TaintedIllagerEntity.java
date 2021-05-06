package net.arcanamod.entities.tainted;

import com.google.common.collect.Maps;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Predicate;

public class TaintedIllagerEntity extends AbstractIllagerEntity {
	private static final Predicate<Difficulty> field_213681_b = (p_213678_0_) -> {
		return p_213678_0_ == Difficulty.NORMAL || p_213678_0_ == Difficulty.HARD;
	};

	public TaintedIllagerEntity(EntityType<? extends Entity> type, World world) {
		super((EntityType<? extends AbstractIllagerEntity>) type, world);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new TaintedIllagerEntity.BreakDoorGoal(this));
		this.goalSelector.addGoal(2, new AbstractIllagerEntity.RaidOpenDoorGoal(this));
		this.goalSelector.addGoal(3, new AbstractRaiderEntity.FindTargetGoal(this, 10.0F));
		this.goalSelector.addGoal(4, new TaintedIllagerEntity.AttackGoal(this));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setCallsForHelp());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
		this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
		this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
	}
	
	public void applyWaveBonus(int wave, boolean p_213660_2_){
	
	}
	
	protected void updateAITasks() {
		if (!this.isAIDisabled()) {
			PathNavigator pathnavigator = this.getNavigator();
			if (pathnavigator instanceof GroundPathNavigator) {
				boolean flag = ((ServerWorld)this.world).hasRaid(new BlockPos(getPosition()));
				((GroundPathNavigator)pathnavigator).setBreakDoors(flag);
			}
		}

		super.updateAITasks();
	}

	/*protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)0.35F);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(14.0D);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0D);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
	}*/

	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);

	}

	/*@OnlyIn(Dist.CLIENT)
	public AbstractIllagerEntity.ArmPose getArmPose() {
		if (this.isAggressive()) {
			return AbstractIllagerEntity.ArmPose.ATTACKING;
		} else {
			return this.func_213656_en() ? AbstractIllagerEntity.ArmPose.CELEBRATING : AbstractIllagerEntity.ArmPose.CROSSED;
		}
	}*/

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);

	}

	public SoundEvent getRaidLossSound() {
		return SoundEvents.ENTITY_VINDICATOR_CELEBRATE;
	}

	@Nullable
	public ILivingEntityData onInitialSpawn(ServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		ILivingEntityData ilivingentitydata = super.onInitialSpawn(world, difficulty, reason, spawnDataIn, dataTag);
		((GroundPathNavigator)this.getNavigator()).setBreakDoors(true);
		this.setEquipmentBasedOnDifficulty(difficulty);
		this.setEnchantmentBasedOnDifficulty(difficulty);
		return ilivingentitydata;
	}

	/**
	 * Gives armor or weapon for entity based on given DifficultyInstance
	 */
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		if (this.getRaid() == null) {
			this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
		}

	}

	/**
	 * Returns whether this Entity is on the same team as the given Entity.
	 */
	public boolean isOnSameTeam(Entity entityIn) {
		if (super.isOnSameTeam(entityIn)) {
			return true;
		} else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getCreatureAttribute() == CreatureAttribute.ILLAGER) {
			return this.getTeam() == null && entityIn.getTeam() == null;
		} else {
			return false;
		}
	}

	public void setCustomName(@Nullable ITextComponent name) {
		super.setCustomName(name);

	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_VINDICATOR_AMBIENT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_VINDICATOR_DEATH;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_VINDICATOR_HURT;
	}

	public void func_213660_a(int p_213660_1_, boolean p_213660_2_) {
		ItemStack itemstack = new ItemStack(Items.IRON_AXE);
		Raid raid = this.getRaid();
		int i = 1;
		if (p_213660_1_ > raid.getWaves(Difficulty.NORMAL)) {
			i = 2;
		}

		boolean flag = this.rand.nextFloat() <= raid.getEnchantOdds();
		if (flag) {
			Map<Enchantment, Integer> map = Maps.newHashMap();
			map.put(Enchantments.SHARPNESS, i);
			EnchantmentHelper.setEnchantments(map, itemstack);
		}

		this.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemstack);
	}

	class AttackGoal extends MeleeAttackGoal {
		public AttackGoal(TaintedIllagerEntity p_i50577_2_) {
			super(p_i50577_2_, 1.0D, false);
		}

		protected double getAttackReachSqr(LivingEntity attackTarget) {
			if (this.attacker.getRidingEntity() instanceof RavagerEntity) {
				float f = this.attacker.getRidingEntity().getWidth() - 0.1F;
				return (double)(f * 2.0F * f * 2.0F + attackTarget.getWidth());
			} else {
				return super.getAttackReachSqr(attackTarget);
			}
		}
	}

	static class BreakDoorGoal extends net.minecraft.entity.ai.goal.BreakDoorGoal {
		public BreakDoorGoal(MobEntity p_i50578_1_) {
			super(p_i50578_1_, 6, TaintedIllagerEntity.field_213681_b);
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean shouldContinueExecuting() {
			TaintedIllagerEntity vindicatorentity = (TaintedIllagerEntity)this.entity;
			return vindicatorentity.isRaidActive() && super.shouldContinueExecuting();
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean shouldExecute() {
			TaintedIllagerEntity vindicatorentity = (TaintedIllagerEntity)this.entity;
			return vindicatorentity.isRaidActive() && vindicatorentity.rand.nextInt(10) == 0 && super.shouldExecute();
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {
			super.startExecuting();
			this.entity.setIdleTime(0);
		}
	}
}
