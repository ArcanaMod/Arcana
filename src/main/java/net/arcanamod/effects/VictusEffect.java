package net.arcanamod.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class VictusEffect extends Effect{
	
	protected VictusEffect(){
		super(EffectType.BENEFICIAL, 0xCD5CAB);
	}
	
	// regen amount scales with amplifier rather than rate, unlike Regeneration
	
	public void performEffect(LivingEntity target, int amplifier){
		if(target.getHealth() < target.getMaxHealth())
			target.heal(amplifier + 1);
	}
	
	public boolean isReady(int duration, int amplifier){
		return duration % 50 == 0;
	}
}