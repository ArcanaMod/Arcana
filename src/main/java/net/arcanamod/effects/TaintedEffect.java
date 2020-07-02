package net.arcanamod.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

public class TaintedEffect extends Effect {


    public TaintedEffect() {
        super(EffectType.HARMFUL, 0xa200ff);
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, 1.0F + amplifier);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }
}
