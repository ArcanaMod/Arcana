package net.kineticdevelopment.arcana.api.spells;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Interface for spell effect
 * 
 * @author Merijn
 */
public interface ISpellEffect {

    String getName();

    // Effect on block position
    void getEffect(BlockPos block, World world, int power);

    // Effect on entity
    void getEffect(EntityLivingBase entity, int power);

    int getParticleID();

    boolean isAttack();

}
