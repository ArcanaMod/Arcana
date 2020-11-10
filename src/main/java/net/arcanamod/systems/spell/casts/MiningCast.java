package net.arcanamod.systems.spell.casts;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.HashMap;

public class MiningCast extends Cast {

	@Override
	public IOldSpell build(SpellData data, CompoundNBT compound) {
		this.data = data;
		isBuilt = true;
		return this;
	}

	@Override
	public ResourceLocation getId() {
		return ArcanaVariables.arcLoc("mining");
	}

	@Override
	public Aspect getSpellAspect() {
		return Aspects.MINING;
	}

	@Override
	public SpellData getSpellData() {
		return data;
	}

	@Override
	public SpellCosts getSpellCosts() {
		return new SpellCosts(0,0,0,1,0,0,0);
	}

	@Override
	public int getComplexity() {
		if (!isBuilt) return -2;
		return  (int)(6
				+ SpellValues.getOrDefault(data.firstModifier,0)*1.5f
				+ SpellValues.getOrDefault(data.secondModifier,0)*2
				+ SpellValues.getOrDefault(data.sinModifier,0)*2
				+ SpellValues.getOrDefault(data.primaryCast.getSecond(),0)*2
				+ SpellValues.getOrDefault(data.plusCast.getSecond(),0))*2;
	}

	@Override
	public int getSpellDuration() {
		return 1;
	}

	public int getMiningLevel() throws SpellNotBuiltError {
		if (!isBuilt) throw new SpellNotBuiltError();
		return SpellValues.getOrDefault(data.firstModifier, 2);
	}

	public int getExplosivePower() throws SpellNotBuiltError {
		if (!isBuilt) throw new SpellNotBuiltError();
		return SpellValues.getOrDefault(data.firstModifier, 0);
	}

	public int getFortune() throws SpellNotBuiltError {
		if (!isBuilt) throw new SpellNotBuiltError();
		return SpellValues.getOrDefault(data.firstModifier, 0);
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

	public ActionResultType useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget) {
		try {
			if(caster.world.isRemote) return ActionResultType.SUCCESS;
			BlockState blockToDestroy = caster.world.getBlockState(blockTarget);
			if (blockToDestroy.getBlock().canHarvestBlock(blockToDestroy, caster.world, blockTarget, caster) && blockToDestroy.getHarvestLevel() <= getMiningLevel()) {
				caster.world.destroyBlock(blockTarget, true, caster);
				TileEntity tileentity = blockToDestroy.hasTileEntity() ? world.getTileEntity(blockTarget) : null;
				HashMap<Enchantment, Integer> map = new HashMap<>();
				map.put(Enchantments.FORTUNE,getFortune());
				ItemStack pickaxe = createDummyPickaxe(getMiningLevel());
				EnchantmentHelper.setEnchantments(map,pickaxe);
				Block.spawnDrops(blockToDestroy, world, blockTarget, tileentity, caster, pickaxe);
				blockToDestroy.updateNeighbors(caster.world,blockTarget,3);
			}
		} catch (SpellNotBuiltError spellNotBuiltError) {
			spellNotBuiltError.printStackTrace();
			return ActionResultType.FAIL;
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
