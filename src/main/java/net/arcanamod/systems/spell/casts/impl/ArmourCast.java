package net.arcanamod.systems.spell.casts.impl;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.tiles.WardenedBlockTileEntity;
import net.arcanamod.effects.ArcanaEffects;
import net.arcanamod.systems.spell.*;
import net.arcanamod.systems.spell.casts.Cast;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class ArmourCast extends Cast {

	@Override
	public ResourceLocation getId() {
		return ArcanaVariables.arcLoc("armour");
	}

	@Override
	public Aspect getSpellAspect() {
		return Aspects.ARMOUR;
	}

	@Override
	public int getSpellDuration() {
		return 1;
	}

	public int getWardingDuration() {
		return SpellValues.getOrDefault(AspectUtils.getAspect(data,"firstModifier"), 10);
	}

	public int getAmplifier() {
		return SpellValues.getOrDefault(AspectUtils.getAspect(data,"secondModifier"), 1);
	}

	@Override
	public ActionResultType useOnEntity(PlayerEntity caster, Entity targetEntity) {
		if (targetEntity instanceof LivingEntity)
			((LivingEntity)targetEntity).addPotionEffect(new EffectInstance(ArcanaEffects.WARDING.get(),getWardingDuration(),getAmplifier(),false,false));
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResultType useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget) {
		if (world.isRemote) return ActionResultType.SUCCESS;
		Block previousState = world.getBlockState(blockTarget).getBlock();
		if (previousState.getBlock() != ArcanaBlocks.WARDENED_BLOCK.get()) {
			world.setBlockState(blockTarget, ArcanaBlocks.WARDENED_BLOCK.get().getDefaultState());
			((WardenedBlockTileEntity) world.getTileEntity(blockTarget)).setState(Optional.of(previousState.getDefaultState()));
		} else {
			world.setBlockState(blockTarget, ((WardenedBlockTileEntity) world.getTileEntity(blockTarget)).getState().orElse(Blocks.AIR.getDefaultState()));
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResultType useOnPlayer(PlayerEntity playerTarget) {
		playerTarget.addPotionEffect(new EffectInstance(ArcanaEffects.WARDING.get(),getWardingDuration(),getAmplifier(),false,false));
		return ActionResultType.SUCCESS;
	}
}