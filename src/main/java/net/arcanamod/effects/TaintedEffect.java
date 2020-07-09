package net.arcanamod.effects;

import net.arcanamod.blocks.Taint;
import net.arcanamod.entities.ArcanaEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.server.ServerWorld;

public class TaintedEffect extends Effect {


    public TaintedEffect() {
        super(EffectType.HARMFUL, 0xa200ff);
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        // TODO: Check of entity isn't instanceof tainted
        entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, 1.0F + amplifier);
        if (entityLivingBaseIn.getHealth() <= (entityLivingBaseIn.getMaxHealth() / 4f)) {
            ChangeEntityToTainted(entityLivingBaseIn);
        }
    }

    private void ChangeEntityToTainted(LivingEntity entityLiving) {
        if (!(entityLiving instanceof PlayerEntity) && Taint.getTaintedOfEntity(entityLiving.getType())!=null) {
            LivingEntity l = (LivingEntity) Taint.getTaintedOfEntity(entityLiving.getType()).create(entityLiving.world);
            l.setPosition(entityLiving.getPosX(),entityLiving.getPosY(),entityLiving.getPosZ());
            if (!l.getEntityWorld().isRemote)
            ((ServerWorld)l.getEntityWorld()).summonEntity(l);
            entityLiving.remove();
            //Taint.getTaintedOfEntity(entityLiving).cre;
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }
}
