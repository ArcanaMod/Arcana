package net.arcanamod.spells.effects;

import net.arcanamod.spells.SpellEffectHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Fire Spell Effect
 *
 * @author Merijn
 * @see SpellEffectHandler
 */
public class SpellEffectFire implements ISpellEffect{
	@Override
	public String getName(){
		return "FIRE";
	}
	
	@Override
	public void getEffect(BlockPos block, World world, int power){
		block.add(0, 1, 0);
		if(world.isAirBlock(block) && !world.isAirBlock(block.down())){
			world.setBlockState(block, Blocks.FIRE.getDefaultState(), 11);
		}
	}
	
	@Override
	public void getEffect(LivingEntity entity, int power){
		entity.setFire(power * 3);
	}
	
	@Override
	public int getParticleID(){
		return 0/*EnumParticleTypes.FLAME.getParticleID()*/;
	}
	
	@Override
	public boolean isAttack(){
		return true;
	}
}
