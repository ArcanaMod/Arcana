package net.arcanamod.systems.spell.casts.impl;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.SpellValues;
import net.arcanamod.systems.spell.casts.Cast;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.HashMap;

public class MiningCast extends Cast {

	@Override
	public ResourceLocation getId() {
		return ArcanaVariables.arcLoc("mining");
	}
	
	@Override
	public Aspect getSpellAspect() {
		return Aspects.MINING;
	}

	@Override
	public int getSpellDuration() {
		return 1;
	}

	public int getMiningLevel() {
		return SpellValues.getOrDefault(AspectUtils.getAspect(data,"firstModifier"), 2);
	}

	public int getExplosivePower() {
		return SpellValues.getOrDefault(AspectUtils.getAspect(data,"secondModifier"), 0);
	}

	public int getFortune() {
		return SpellValues.getOrDefault(AspectUtils.getAspect(data,"sinModifier"), 0);
	}

	@Override
	public ActionResultType useOnPlayer(PlayerEntity playerTarget) {
		playerTarget.sendStatusMessage(new TranslationTextComponent("status.arcana.invalid_spell"), true);
		return ActionResultType.FAIL;
	}

	@Override
	public ActionResultType useOnEntity(PlayerEntity caster, Entity entityTarget) {
		caster.sendStatusMessage(new TranslationTextComponent("status.arcana.invalid_spell"), true);
		return ActionResultType.FAIL;
	}

	@SuppressWarnings("deprecation")
	public ActionResultType useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget) {
		if(caster.world.isRemote) return ActionResultType.SUCCESS;
		BlockState blockToDestroy = caster.world.getBlockState(blockTarget);
		if (blockToDestroy.getHarvestLevel() <= getMiningLevel() && blockToDestroy.getBlockHardness(world,blockTarget) != -1 && blockTarget.getY() != 0) {
			// Spawn block_break particles
			world.playEvent(2001, blockTarget, Block.getStateId(blockToDestroy));

			// Check of it has tile entity
			TileEntity tileentity = blockToDestroy.hasTileEntity() ? world.getTileEntity(blockTarget) : null;

			// Create dummy Pickaxe with enchantments and mining level
			HashMap<Enchantment, Integer> map = new HashMap<>();
			map.put(Enchantments.FORTUNE,getFortune());
			ItemStack pickaxe = createDummyPickaxe(getMiningLevel());
			EnchantmentHelper.setEnchantments(map,pickaxe);

			// Spawn drops and destroy block.
			Block.spawnDrops(blockToDestroy, world, blockTarget, tileentity, caster, pickaxe);
			FluidState ifluidstate = blockToDestroy.getBlock().getFluidState(blockToDestroy);
			world.setBlockState(blockTarget, ifluidstate.getBlockState(), 3);
			blockToDestroy.updateNeighbours(caster.world, blockTarget,3);
		}
		return ActionResultType.SUCCESS;
	}

	private ItemStack createDummyPickaxe(int miningLevel) { // TODO: Check if it works
		return new ItemStack(new PickaxeItem(new IItemTier() {
			@Override
			public int getMaxUses() {
				return 1;
			}

			@Override
			public float getEfficiency() {
				return 1;
			}

			@Override
			public float getAttackDamage() {
				return 1;
			}

			@Override
			public int getHarvestLevel() {
				return miningLevel;
			}

			@Override
			public int getEnchantability() {
				return 0;
			}

			@Override
			public Ingredient getRepairMaterial() {
				return null;
			}
		},0,0,new Item.Properties()),1);
	}
}
