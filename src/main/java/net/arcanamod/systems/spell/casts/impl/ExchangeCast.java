package net.arcanamod.systems.spell.casts.impl;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.SpellValues;
import net.arcanamod.systems.spell.casts.Cast;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ExchangeCast extends Cast {

	@Override
	public ResourceLocation getId() {
		return ArcanaVariables.arcLoc("exchange");
	}
	
	@Override
	public Aspect getSpellAspect() {
		return Aspects.EXCHANGE;
	}

	@Override
	public int getSpellDuration() {
		return 1;
	}

	public int getMiningLevel(){
		return SpellValues.getOrDefault(AspectUtils.getAspect(data,"firstModifier"), 2);
	}

	public int getSize(){
		return SpellValues.getOrDefault(AspectUtils.getAspect(data,"secondModifier"), 1);
	}

	@Override
	public ActionResultType useOnEntity(PlayerEntity caster, Entity targetEntity) {
		caster.sendStatusMessage(new TranslationTextComponent("status.arcana.invalid_spell"), true);
		return ActionResultType.FAIL;
	}

	@Override
	public ActionResultType useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget) {
		if (caster.world.isRemote) return ActionResultType.SUCCESS;
		BlockState blockToDestroy = caster.world.getBlockState(blockTarget);
		if (blockToDestroy.getBlock().canHarvestBlock(blockToDestroy, caster.world, blockTarget, caster) && blockToDestroy.getHarvestLevel() <= getMiningLevel()) {
			ItemStack held = caster.getHeldItem(Hand.OFF_HAND);
			if (!held.isEmpty() && Block.getBlockFromItem(held.getItem()) != Blocks.AIR) {
				for (ItemStack stack : caster.world.getBlockState(blockTarget).getDrops(new LootContext.Builder((ServerWorld) caster.world)
						.withParameter(LootParameters.ORIGIN, Vector3d.copyCentered(blockTarget)).withParameter(LootParameters.TOOL, new ItemStack(getMiningLevel() >= 3 ? Items.DIAMOND_PICKAXE : Items.IRON_PICKAXE)))) {
					caster.addItemStackToInventory(stack);
				}
				caster.world.setBlockState(blockTarget, Block.getBlockFromItem(held.getItem()).getDefaultState());
				held.shrink(1);
				blockToDestroy.updateNeighbours(caster.world, blockTarget, 3);
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResultType useOnPlayer(PlayerEntity playerTarget) {
		playerTarget.sendStatusMessage(new TranslationTextComponent("status.arcana.invalid_spell"), true);
		return ActionResultType.FAIL;
	}
}
