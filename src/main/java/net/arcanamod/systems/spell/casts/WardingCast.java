package net.arcanamod.systems.spell.casts;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.tiles.WardenedBlockTileEntity;
import net.arcanamod.effects.ArcanaEffects;
import net.arcanamod.systems.spell.*;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class WardingCast extends Cast {
	
	public ICast build(SpellData data, CompoundNBT compound) {
		this.data = data;
		isBuilt = true;
		return this;
	}

	@Override
	public ResourceLocation getId() {
		return ArcanaVariables.arcLoc("warding");
	}
	
	public ICast build(CompoundNBT compound){
		return null;
	}
	
	@Override
	public Aspect getSpellAspect() {
		return Aspects.ARMOUR;
	}

	@Override
	public SpellData getSpellData() {
		return data;
	}

	@Override
	public SpellCosts getSpellCosts() {
		return new SpellCosts(0,0,1,1,0,0,0);
	}

	@Override
	public int getComplexity() {
		if (!isBuilt) return -2;
		return  8
				+ SpellValues.getOrDefault(data.firstModifier,0)
				+ SpellValues.getOrDefault(data.secondModifier,0)
				+ SpellValues.getOrDefault(data.sinModifier,0)
				+ SpellValues.getOrDefault(data.primaryCast.getSecond(),0)/2
				+ SpellValues.getOrDefault(data.plusCast.getSecond(),0)/2;
	}

	@Override
	public int getSpellDuration() {
		return 1;
	}

	public int getWardingDuration() throws SpellNotBuiltError {
		if (!isBuilt) throw new SpellNotBuiltError();
		return SpellValues.getOrDefault(data.firstModifier, 10);
	}

	public int getAmplifier() throws SpellNotBuiltError {
		if (!isBuilt) throw new SpellNotBuiltError();
		return SpellValues.getOrDefault(data.secondModifier, 1);
	}

	@Override
	public ActionResultType useOnEntity(PlayerEntity caster, Entity targetEntity) {
		try {
			if (targetEntity instanceof LivingEntity)
				((LivingEntity)targetEntity).addPotionEffect(new EffectInstance(ArcanaEffects.WARDING.get(),getWardingDuration(),getAmplifier(),false,true));
		} catch (SpellNotBuiltError spellNotBuiltError) {
			spellNotBuiltError.printStackTrace();
		}
		return ActionResultType.FAIL;
	}

	@Override
	public ActionResultType useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget) {
			if (world.isRemote) return ActionResultType.SUCCESS;
			Block previousState = world.getBlockState(blockTarget).getBlock();
			world.setBlockState(blockTarget, ArcanaBlocks.WARDENED_BLOCK.get().getDefaultState());
			((WardenedBlockTileEntity)world.getTileEntity(blockTarget)).func_939844_a_(Optional.of(previousState.getDefaultState()));
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResultType useOnPlayer(PlayerEntity playerTarget) {
		try {
			playerTarget.addPotionEffect(new EffectInstance(ArcanaEffects.WARDING.get(),getWardingDuration(),getAmplifier(),false,true));
		} catch (SpellNotBuiltError spellNotBuiltError) {
			spellNotBuiltError.printStackTrace();
		}
		return ActionResultType.FAIL;
	}
}