package net.arcanamod.systems.spell.impls;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.*;
import net.arcanamod.util.RayTraceUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;

import javax.annotation.Nullable;
import java.util.List;

public class ExchangeSpell extends Spell {

	private Aspect[] modAspects;
	private CastAspect[] castAspects;
	private int distance = 10;

	public boolean isBuilt = false;

	@Override
	public ISpell build(Aspect[] modAspects, CastAspect[] castAspects, SpellExtraData data) {
		this.modAspects = modAspects;
		this.castAspects = castAspects;
		if (data.contains("distance"))
			this.distance = data.read("distance");
		isBuilt = true;
		return this;
	}

	@Override
	public ResourceLocation getId() {
		return Arcana.arcLoc("exchange");
	}

	@Override
	public Aspect getSpellAspect() {
		return Aspects.EXCHANGE;
	}

	@Override
	public Aspect[] getModAspects() {
		return modAspects;
	}

	@Override
	public CastAspect[] getCastAspects() {
		return castAspects;
	}

	@Override
	public AspectStack[] getAspectCosts() {
		// Currently EVERYTHING uses ORDER aspect.
		return new AspectStack[] {new AspectStack(Aspects.ORDER,1)};
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
		return SpellValues.getOrDefault(modAspects[0], 2);
	}

	public int getSize() throws SpellNotBuiltError {
		if (!isBuilt) throw new SpellNotBuiltError();
		return SpellValues.getOrDefault(modAspects[1], 0);
	}

	@Override
	public void use(PlayerEntity player, ISpell.Action action) {
		try {
			if (player.world.isRemote) return;
			if (castAspects[0].isEmpty() && castAspects[1].isEmpty() && castAspects[2].isEmpty()) {
				// Default spell
				BlockPos pos = RayTraceUtils.getTargetBlockPos(player, player.world, distance);
				BlockState blockToDestroy = player.world.getBlockState(pos);
				if (blockToDestroy.getBlock().canHarvestBlock(blockToDestroy, player.world, pos, player) && blockToDestroy.getHarvestLevel() <= getMiningLevel()) {
					for (ItemStack stack : player.world.getBlockState(pos).getDrops(new LootContext.Builder((ServerWorld) player.world)
							.withParameter(LootParameters.POSITION, pos).withParameter(LootParameters.TOOL, ItemStack.EMPTY))){
						player.addItemStackToInventory(stack);
					}
					ItemStack held = player.getHeldItem(Hand.OFF_HAND);
						if (held!=ItemStack.EMPTY)
					player.world.setBlockState(pos, Block.getBlockFromItem(held.getItem()).getDefaultState());
					held.shrink(1);
				}
			} else {
				Spell.useCasts(this, player, castAspects);
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
				for (ItemStack stack : caster.world.getBlockState(blockTarget).getDrops(new LootContext.Builder((ServerWorld) caster.world)
						.withParameter(LootParameters.POSITION, blockTarget).withParameter(LootParameters.TOOL, ItemStack.EMPTY))){
					caster.addItemStackToInventory(stack);
				}
				ItemStack held = caster.getHeldItem(Hand.OFF_HAND);
				if (held!=ItemStack.EMPTY)
					caster.world.setBlockState(blockTarget, Block.getBlockFromItem(held.getItem()).getDefaultState());
				held.shrink(1);
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
				for (ItemStack stack : caster.world.getBlockState(blockTarget).getDrops(new LootContext.Builder((ServerWorld) caster.world)
						.withParameter(LootParameters.POSITION, blockTarget).withParameter(LootParameters.TOOL, ItemStack.EMPTY))){
					caster.addItemStackToInventory(stack);
				}
				ItemStack held = caster.getHeldItem(Hand.OFF_HAND);
				if (held!=ItemStack.EMPTY)
					caster.world.setBlockState(blockTarget, Block.getBlockFromItem(held.getItem()).getDefaultState());
				held.shrink(1);
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
				for (ItemStack stack : caster.world.getBlockState(blockTarget).getDrops(new LootContext.Builder((ServerWorld) caster.world)
						.withParameter(LootParameters.POSITION, blockTarget).withParameter(LootParameters.TOOL, ItemStack.EMPTY))){
					caster.addItemStackToInventory(stack);
				}
				ItemStack held = caster.getHeldItem(Hand.OFF_HAND);
				if (held!=ItemStack.EMPTY)
					caster.world.setBlockState(blockTarget, Block.getBlockFromItem(held.getItem()).getDefaultState());
				held.shrink(1);
			}
		} catch (SpellNotBuiltError spellNotBuiltError) {
			spellNotBuiltError.printStackTrace();
		}
	}
}
