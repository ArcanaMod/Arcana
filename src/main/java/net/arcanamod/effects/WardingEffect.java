package net.arcanamod.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class WardingEffect extends Effect{
	
	public WardingEffect(){
		super(EffectType.BENEFICIAL, 0x3255FF);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier){
		return true;
	}
}
