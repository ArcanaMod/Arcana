package net.arcanamod.entities.tainted;

import net.arcanamod.effects.ArcanaEffects;
import net.arcanamod.effects.ArcanaPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class TaintedSkeletonEntity extends AbstractSkeletonEntity {
	public TaintedSkeletonEntity(EntityType<? extends Entity> type, World world) {
		super((EntityType<? extends AbstractSkeletonEntity>) type, world);
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SKELETON_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_SKELETON_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SKELETON_DEATH;
	}

	protected SoundEvent getStepSound() {
		return SoundEvents.ENTITY_SKELETON_STEP;
	}

	protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
		super.dropSpecialItems(source, looting, recentlyHitIn);
		Entity entity = source.getTrueSource();
		if (entity instanceof CreeperEntity) {
			CreeperEntity creeperentity = (CreeperEntity)entity;
			if (creeperentity.ableToCauseSkullDrop()) {
				creeperentity.incrementDroppedSkulls();
				this.entityDropItem(Items.SKELETON_SKULL);
			}
		}

	}

	@Override
	protected AbstractArrowEntity fireArrow(ItemStack arrowStack, float distanceFactor) {
		return super.fireArrow(PotionUtils.addPotionToItemStack(new ItemStack(Items.TIPPED_ARROW,3), ArcanaPotions.TAINT), distanceFactor);
	}
}
