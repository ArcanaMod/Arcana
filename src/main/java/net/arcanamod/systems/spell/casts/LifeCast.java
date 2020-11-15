package net.arcanamod.systems.spell.casts;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.effects.ArcanaEffects;
import net.arcanamod.systems.spell.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LifeCast extends Cast {
	
	public ICast build(CompoundNBT compound) {
		return this;
	}

	@Override
	public ResourceLocation getId() {
		return ArcanaVariables.arcLoc("life");
	}
	
	@Override
	public Aspect getSpellAspect() {
		return Aspects.LIFE;
	}

	@Override
	public int getSpellDuration() {
		return 1;
	}

	public int getWardingDuration() {
		return SpellValues.getOrDefault(data.firstModifier, 10);
	}

	public int getAmplifier() {
		return SpellValues.getOrDefault(data.secondModifier, 1);
	}

	@Override
	public ActionResultType useOnEntity(PlayerEntity caster, Entity targetEntity) {
		if (targetEntity instanceof LivingEntity)
			((LivingEntity)targetEntity).addPotionEffect(new EffectInstance(ArcanaEffects.VICTUS.get(),getWardingDuration(),getAmplifier(),false,true));
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResultType useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget) {

		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResultType useOnPlayer(PlayerEntity playerTarget) {
		playerTarget.addPotionEffect(new EffectInstance(ArcanaEffects.WARDING.get(),getWardingDuration(),getAmplifier(),false,true));
		return ActionResultType.SUCCESS;
	}
}