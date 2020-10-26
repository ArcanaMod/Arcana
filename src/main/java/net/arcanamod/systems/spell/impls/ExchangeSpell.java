package net.arcanamod.systems.spell.impls;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;

public class ExchangeSpell extends Spell {

	private SpellData data;

	public boolean isBuilt = false;

	@Override
	public ISpell build(SpellData data, CompoundNBT compound) {
		this.data = data;
		isBuilt = true;
		return this;
	}

	@Override
	public ResourceLocation getId() {
		return ArcanaVariables.arcLoc("exchange");
	}

	@Override
	public Aspect getSpellAspect() {
		return Aspects.EXCHANGE;
	}

	@Override
	public SpellData getSpellData() {
		return data;
	}

	@Override
	public SpellCosts getSpellCosts() {
		return new SpellCosts(0,0,0,0,1,0,0);
	}

	@Override
	public int getComplexity() {
		if (!isBuilt) return -2;
		return  2
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

	public int getMiningLevel() throws SpellNotBuiltError {
		if (!isBuilt) throw new SpellNotBuiltError();
		return SpellValues.getOrDefault(data.firstModifier, 2);
	}

	public int getSize() throws SpellNotBuiltError {
		if (!isBuilt) throw new SpellNotBuiltError();
		return SpellValues.getOrDefault(data.secondModifier, 1);
	}

	@Override
	public ActionResultType useOnEntity(PlayerEntity caster, Entity targetEntity) {
		caster.sendStatusMessage(new TranslationTextComponent("status.arcana.invalid_spell"), true);
		return ActionResultType.FAIL;
	}

	@Override
	public ActionResultType useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget) {
		try {
			if (caster.world.isRemote) return ActionResultType.SUCCESS;
			BlockState blockToDestroy = caster.world.getBlockState(blockTarget);
			if (blockToDestroy.getBlock().canHarvestBlock(blockToDestroy, caster.world, blockTarget, caster) && blockToDestroy.getHarvestLevel() <= getMiningLevel()) {
				ItemStack held = caster.getHeldItem(Hand.OFF_HAND);
				held.shrink(1);
				for (ItemStack stack : caster.world.getBlockState(blockTarget).getDrops(new LootContext.Builder((ServerWorld) caster.world)
						.withParameter(LootParameters.POSITION, blockTarget).withParameter(LootParameters.TOOL, new ItemStack(getMiningLevel()>=3?Items.DIAMOND_PICKAXE:Items.IRON_PICKAXE)))) {
					caster.addItemStackToInventory(stack);
				}
				caster.world.setBlockState(blockTarget, Block.getBlockFromItem(held.getItem()).getDefaultState());
				blockToDestroy.updateNeighbors(caster.world,blockTarget,3);
			}
		} catch (SpellNotBuiltError spellNotBuiltError) {
			spellNotBuiltError.printStackTrace();
			return ActionResultType.FAIL;
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResultType useOnPlayer(PlayerEntity playerTarget) {
		playerTarget.sendStatusMessage(new TranslationTextComponent("status.arcana.invalid_spell"), true);
		return ActionResultType.FAIL;
	}
}
