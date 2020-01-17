package net.kineticdevelopment.arcana.api.spells;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellEffectFire implements ISpellEffect {
    @Override
    public String getName() {
        return "FIRE";
    }

    @Override
    public void getEffect(BlockPos block, World world, int power) {
        block.add(0, 1, 0);
        if(world.isAirBlock(block) && !world.isAirBlock(block.down())) {
            world.setBlockState(block, Blocks.FIRE.getDefaultState(),11);
        }
    }

    @Override
    public void getEffect(EntityLivingBase entity, int power) {
        entity.setFire(power * 3);
    }

    @Override
    public int getParticleID() {
        return EnumParticleTypes.FLAME.getParticleID();
    }

    @Override
    public boolean isAttack() {
        return true;
    }
}
