package net.arcanamod.effects;

import net.arcanamod.entities.TaintedGooWrapper;
import net.arcanamod.systems.taint.Taint;
import net.arcanamod.systems.taint.TaintDamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.world.Difficulty;
import net.minecraft.world.server.ServerWorld;

public class TaintedEffect extends Effect {
	public TaintedEffect() {
		super(EffectType.HARMFUL, 0xa200ff);
	}
	
	@Override
	public void performEffect(LivingEntity entity, int amplifier) {
		if (!Taint.isTainted(entity.getType())) {
			entity.attackEntityFrom(TaintDamageSource.TAINT, 1.0F + amplifier);
			if ((entity.getHealth() <= (entity.getMaxHealth() / 4f) || entity.getHealth() == 1)
					&& entity.world.getDifficulty() != Difficulty.PEACEFUL) {
				changeEntityToTainted(entity);
			}
		}
	}
	
	private void changeEntityToTainted(LivingEntity entityLiving) {
		if (!(entityLiving instanceof PlayerEntity) && Taint.getTaintedOfEntity(entityLiving.getType()) != null) {
			LivingEntity l = (LivingEntity)Taint.getTaintedOfEntity(entityLiving.getType()).create(entityLiving.world);
			if (l != null) {
				l.setPositionAndRotation(entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), entityLiving.rotationYaw, entityLiving.rotationPitch);
				if (!l.getEntityWorld().isRemote) {
					((ServerWorld) l.getEntityWorld()).summonEntity(l);
				}
				entityLiving.remove();
			}
		}
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		// Same rate as Regeneration
		int k = 30 >> amplifier;
		if(k > 0)
			return duration % k == 0;
		return true;
	}

	@Override
	public void removeAttributesModifiersFromEntity(LivingEntity entityLivingBaseIn, AttributeModifierManager attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
		((TaintedGooWrapper) entityLivingBaseIn).setGooTicks(0);
	}
}