package net.arcanamod.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class FrozenEffect extends Effect {
	public FrozenEffect(){
		super(EffectType.HARMFUL, 0x0055FF);
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier){
		// this uhhhh
		// does not work in the slightest
		//entity.setMotion(0,entity.getMotion().y*2,0);
		
		// if you want to change jump height, look at the Jump Boost effect
		// if you want to affect fall speed, look at the Slow Fall status effect
		// you will very likely need to mixin
	}

	@Override
	public boolean isReady(int duration, int amplifier){
		return true;
	}
}
