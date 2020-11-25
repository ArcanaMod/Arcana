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
		entity.setMotion(0,entity.getMotion().y,0);
	}

	@Override
	public boolean isReady(int duration, int amplifier){
		return true;
	}
}
