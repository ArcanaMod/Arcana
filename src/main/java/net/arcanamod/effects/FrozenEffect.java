package net.arcanamod.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import javax.annotation.Nonnull;

public class FrozenEffect extends Effect{
	
	public FrozenEffect(){
		super(EffectType.HARMFUL, 0x0055FF);
	}
	
	@Override
	public void performEffect(@Nonnull LivingEntity entity, int amplifier){
		// if you want to change jump height, look at the Jump Boost effect
		// if you want to affect fall speed, look at the Slow Fall status effect
		// you will very likely need to mixin
	}
	
	@Override
	public boolean isReady(int duration, int amplifier){
		return true;
	}
}
