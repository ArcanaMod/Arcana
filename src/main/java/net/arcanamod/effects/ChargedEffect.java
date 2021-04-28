package net.arcanamod.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class ChargedEffect extends Effect {
	public ChargedEffect(){
		super(EffectType.HARMFUL, 0x1212FF);
	}

	@Override
	public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
		super.performEffect(entityLivingBaseIn, amplifier);
		if (entityLivingBaseIn.world.isRainingAt(entityLivingBaseIn.getPosition()))
			addAttributesModifier(Attributes.ARMOR,"0963515b-6188-4415-9bb6-edb7a45914cd",0.5f, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public boolean isReady(int duration, int amplifier){
		return true;
	}
}
