package net.kineticdevelopment.arcana.common.entities;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TaintedChicken extends EntityMob 
{

	public TaintedChicken(World world) 
	{
		super(world);
		setSize(0.4f, 0.7f);
		experienceValue = 10;
		setNoAI(!true);
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayerMP.class, true, true));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.2, true));
		this.tasks.addTask(5, new EntityAIWander(this, 1));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.tasks.addTask(7, new EntityAISwimming(this));
		this.tasks.addTask(8, new EntityAIEatGrass(this));
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() 
	{
		return EnumCreatureAttribute.UNDEFINED;
	}

	@Override
	protected Item getDropItem() 
	{
		return Items.CHICKEN;
	}

	@Override
	public net.minecraft.util.SoundEvent getAmbientSound() 
	{
		return (SoundEvents.ENTITY_CHICKEN_AMBIENT);
	}

	@Override
	public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) 
	{
		return (SoundEvents.ENTITY_CHICKEN_HURT);
	}

	@Override
	public net.minecraft.util.SoundEvent getDeathSound() 
	{
		return (SoundEvents.ENTITY_CHICKEN_DEATH);
	}

	@Override
	protected float getSoundVolume() 
	{
		return 1.0F;
	}

	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4);
		if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) != null)
			this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2D);
	}
}