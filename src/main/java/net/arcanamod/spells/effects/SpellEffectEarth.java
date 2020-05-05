package net.arcanamod.spells.effects;

import net.arcanamod.spells.SpellEffectHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Earth Spell Effect
 *
 * @author Merijn
 * @see SpellEffectHandler
 */
public class SpellEffectEarth implements ISpellEffect{
	
	@Override
	public String getName(){
		return "EARTH";
	}
	
	@Override
	public void getEffect(BlockPos block, World world, int power){
		if(!world.getBlockState(block.add(0, 1, 0)).getMaterial().isReplaceable()){
			block.add(0, 1, 0);
			world.setBlockState(block, Blocks.GRASS.getDefaultState());
		}else{
			world.setBlockState(block, Blocks.DIRT.getDefaultState());
		}
	}
	
	@Override
	public void getEffect(LivingEntity entity, int power){
		entity.setHealth(entity.getHealth() + power);
	}
	
	@Override
	public int getParticleID(){
		return 0/*ParticleTypes.BUBBLE_COLUMN_UP.getParticleID()*/;
	}
	
	@Override
	public boolean isAttack(){
		return false;
	}
	
}
