package net.arcanamod.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

import javax.annotation.Nonnull;

public class FrozenEffect extends Effect{
	
	public FrozenEffect(){
		super(EffectType.HARMFUL, 0x0055FF);
	}
	
	@Override
	public void performEffect(@Nonnull LivingEntity entity, int amplifier){
		entity.attackEntityFrom(DamageSource.MAGIC, 2.0F);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier){
		int j = 40 >> amplifier;
		if (j > 0) {
			return duration % j == 0;
		} else {
			return true;
		}
	}
}
