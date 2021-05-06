package net.arcanamod.entities.tainted.group;

import net.arcanamod.items.ArcanaItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.entity.passive.fish.AbstractGroupFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class TaintedFishEntity extends AbstractGroupFishEntity {
	public ItemStack bucket;

	/*@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttributes().registerAttribute(Attributes.ATTACK_DAMAGE);
		//this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK);
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5D);
		this.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(0.0D);
	}*/

	public TaintedFishEntity(EntityType<AbstractGroupFishEntity> type, World worldIn) {
		super(type, worldIn);
		this.bucket = new ItemStack(ArcanaItems.TAINTED_COD_BUCKED.get());
	}

	@Override
	protected ItemStack getFishBucket() {
		return bucket;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return SoundEvents.ENTITY_TROPICAL_FISH_FLOP;
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0,new MeleeAttackGoal(this,1,false));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAttack));
		//this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
		//this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 1.6D, 1.4D, EntityPredicates.NOT_SPECTATING::test));
		this.goalSelector.addGoal(4, new TaintedFishEntity.SwimGoal(this));
	}

	private boolean shouldAttack(LivingEntity entity) {
		return true;
	}

	static class SwimGoal extends RandomSwimmingGoal {
		private final TaintedFishEntity fish;

		public SwimGoal(TaintedFishEntity fish) {
			super(fish, 1.0D, 40);
			this.fish = fish;
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean shouldExecute() {
			return this.fish.func_212800_dy() && super.shouldExecute();
		}
	}
}
