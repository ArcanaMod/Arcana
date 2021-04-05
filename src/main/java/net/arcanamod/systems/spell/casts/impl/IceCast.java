package net.arcanamod.systems.spell.casts.impl;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.effects.ArcanaEffects;
import net.arcanamod.systems.spell.SpellValues;
import net.arcanamod.systems.spell.casts.Cast;
import net.arcanamod.world.WorldInteractions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.arcanamod.aspects.Aspects.ICE;

public class IceCast extends Cast {
	@Override
	public ResourceLocation getId() {
		return ArcanaVariables.arcLoc("ice");
	}

	@Override
	public ActionResultType useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget) {
		WorldInteractions.fromWorld(world).freezeBlock(blockTarget);
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResultType useOnPlayer(PlayerEntity playerTarget){
		playerTarget.addPotionEffect(new EffectInstance(ArcanaEffects.FROZEN.get(),getFrozenDuration(),getAmplifier(),false,false));
		playerTarget.setFire(-80);
		return ActionResultType.SUCCESS;
	}

	private int getAmplifier() {
		return SpellValues.getOrDefault(AspectUtils.getAspect(data,"secondModifier"),0);
	}

	private int getFrozenDuration() {
		return SpellValues.getOrDefault(AspectUtils.getAspect(data,"firstModifier"),3) * 20;
	}

	@Override
	public ActionResultType useOnEntity(PlayerEntity caster, Entity entityTarget){
		if (entityTarget instanceof LivingEntity) {
			((LivingEntity) entityTarget).addPotionEffect(new EffectInstance(ArcanaEffects.FROZEN.get(), getFrozenDuration(), getAmplifier(), false, false));
			((LivingEntity) entityTarget).setFire(-80);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public Aspect getSpellAspect() {
		return ICE;
	}

	@Override
	public int getSpellDuration() {
		return 1;
	}
}