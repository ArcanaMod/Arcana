package net.arcanamod.systems.spell.casts.impl;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.util.NotImplementedException;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.systems.spell.casts.Cast;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.arcanamod.aspects.Aspects.SENSES;

public class SensesCast extends Cast {
	@Override
	public ResourceLocation getId() {
		return ArcanaVariables.arcLoc("senses");
	}

	@Override
	public ActionResultType useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget) {
		throw new NotImplementedException();
	}

	@Override
	public ActionResultType useOnPlayer(PlayerEntity playerTarget) {
		throw new NotImplementedException();
	}

	@Override
	public ActionResultType useOnEntity(PlayerEntity caster, Entity entityTarget) {
		throw new NotImplementedException();
	}

	@Override
	public Aspect getSpellAspect() {
		return SENSES;
	}

	@Override
	public int getSpellDuration() {
		return 1;
	}
}