package net.arcanamod.effects;

import net.arcanamod.blocks.Taint;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.server.ServerWorld;

public class TaintedEffect extends Effect{
	
	public TaintedEffect(){
		super(EffectType.HARMFUL, 0xa200ff);
	}
	
	@Override
	public void performEffect(LivingEntity entity, int amplifier){
		if(!Taint.isTainted(entity.getType())){
			entity.attackEntityFrom(TaintDamageSource.TAINT, 1.0F + amplifier);
			if((entity.getHealth() <= (entity.getMaxHealth() / 4f) || entity.getHealth() == 1)
					&& entity.world.getDifficulty() != Difficulty.PEACEFUL)
				changeEntityToTainted(entity);
		}
	}
	
	private void changeEntityToTainted(LivingEntity entityLiving){
		if(!(entityLiving instanceof PlayerEntity) && Taint.getTaintedOfEntity(entityLiving.getType()) != null){
			LivingEntity l = (LivingEntity)Taint.getTaintedOfEntity(entityLiving.getType()).create(entityLiving.world);
			if(l != null){
				l.setPositionAndRotation(entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(),entityLiving.rotationYaw,entityLiving.rotationPitch);
				if(!l.getEntityWorld().isRemote)
					((ServerWorld)l.getEntityWorld()).summonEntity(l);
				entityLiving.remove();
			}
		}
	}
	
	@Override
	public boolean isReady(int duration, int amplifier){
		return true;
	}
}