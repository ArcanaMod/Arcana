package net.arcanamod.systems.spell.impls;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.*;
import net.arcanamod.util.RayTraceUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ExchangeSpell extends Spell {

	private List<Aspect> modAspects;
	private List<CastAspect> castAspects;
	private int distance = 10;

	public boolean isBuilt = false;

	@Override
	public ISpell build(List<Aspect> modAspects, List<CastAspect> castAspects, CompoundNBT compound) {
		this.modAspects = modAspects;
		this.castAspects = castAspects;
		if (compound.contains("distance"))
			this.distance = compound.getInt("distance");
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
	public List<Aspect> getModAspects() {
		return modAspects;
	}

	@Override
	public List<CastAspect> getCastAspects() {
		return castAspects;
	}

	@Override
	public List<AspectStack> getAspectCosts() {
		// Currently EVERYTHING uses ORDER aspect.
		return Collections.singletonList(new AspectStack(Aspects.ORDER, 1));
	}

	@Override
	public int getComplexity() {
		if (!isBuilt) return -2;
		int temp = 0;
		for (Aspect aspect : modAspects){
			temp += SpellValues.getOrDefault(aspect, 0);
		}
		return temp != 0 ? -1 : temp;
	}

	@Override
	public int getSpellDuration() {
		return 1;
	}

	public int getMiningLevel() throws SpellNotBuiltError {
		if (!isBuilt) throw new SpellNotBuiltError();
		return modAspects.size() >= 1 ? SpellValues.getOrDefault(modAspects.get(0), 2) : 2;
	}

	public int getSize() throws SpellNotBuiltError {
		if (!isBuilt) throw new SpellNotBuiltError();
		return modAspects.size() >= 2 ? SpellValues.getOrDefault(modAspects.get(1), 0) : 0;
	}

	@Override
	public void use(PlayerEntity player, ISpell.Action action) {
		try {
			if (player.world.isRemote) return;
			ItemStack held = player.getHeldItem(Hand.OFF_HAND);
			if (held!=ItemStack.EMPTY && held.getItem() != Items.AIR && Block.getBlockFromItem(held.getItem()) != Blocks.AIR) {
				if (castAspects.size() <= 0) {
					// Default spell
					BlockPos pos = RayTraceUtils.getTargetBlockPos(player, player.world, distance);
					BlockState blockToDestroy = player.world.getBlockState(pos);
					if (blockToDestroy.getBlock().canHarvestBlock(blockToDestroy, player.world, pos, player) && blockToDestroy.getHarvestLevel() <= getMiningLevel()) {
						held.shrink(1);
						for (ItemStack stack : player.world.getBlockState(pos).getDrops(new LootContext.Builder((ServerWorld) player.world)
								.withParameter(LootParameters.POSITION, pos).withParameter(LootParameters.TOOL, new ItemStack(getMiningLevel()>=3?Items.DIAMOND_PICKAXE:Items.IRON_PICKAXE)))) {
							player.addItemStackToInventory(stack);
						}
						player.world.setBlockState(pos, Block.getBlockFromItem(held.getItem()).getDefaultState());
						blockToDestroy.updateNeighbors(player.world,pos,3);
					}
				} else {
					Spell.useCasts(this, player, castAspects);
				}
			}
		} catch (SpellNotBuiltError spellNotBuiltError) {
			spellNotBuiltError.printStackTrace();
		}
	}

	@Override
	public void onAirCast(PlayerEntity caster, World world, BlockPos pos, int area, int duration) {
		caster.sendMessage(new TranslationTextComponent("message.cantusethatspell"));
	}

	@Override
	public void onWaterCast(PlayerEntity caster, List<Entity> entityTargets) {
		caster.sendMessage(new TranslationTextComponent("message.cantusethatspell"));
	}

	@Override
	public void onFireCast(PlayerEntity caster, @Nullable Entity entityTarget, BlockPos blockTarget) {
		try {
			if (caster.world.isRemote) return;
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
		}
	}

	@Override
	public void onEarthCast(PlayerEntity caster, BlockPos blockTarget) {
		try {
			if (caster.world.isRemote) return;
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
		}
	}

	@Override
	public void onOrderCast(PlayerEntity playerTarget) {
		playerTarget.sendMessage(new TranslationTextComponent("message.cantusethatspell"));
	}

	@Override
	public void onChaosCast(PlayerEntity caster, Entity entityTarget, BlockPos blockTarget) {
		try {
			if (caster.world.isRemote) return;
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
		}
	}
}
