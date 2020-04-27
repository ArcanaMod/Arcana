package net.arcanamod.spells.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Interface used to create a spell effect
 *
 * @author Merijn
 */
public interface ISpellEffect{
	
	/**
	 * @return Name of the effect
	 */
	String getName();
	
	/**
	 * @param block
	 * 		Block that the effect is used on
	 * @param world
	 * 		World the block is in
	 * @param power
	 * 		The power of the spell
	 */
	void getEffect(BlockPos block, World world, int power);
	
	/**
	 * @param entity
	 * 		Entity the effect is used on
	 * @param power
	 * 		The power of the spell
	 */
	void getEffect(EntityLivingBase entity, int power);
	
	/**
	 * @return ID of the particle used
	 */
	int getParticleID();
	
	/**
	 * Used for death messages
	 *
	 * @return Attack boolean
	 */
	boolean isAttack();
	
}
