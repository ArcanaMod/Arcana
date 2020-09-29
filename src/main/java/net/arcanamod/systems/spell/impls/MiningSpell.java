package net.arcanamod.systems.spell.impls;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.*;
import net.arcanamod.util.RayTraceUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class MiningSpell extends Spell {

	private SpellData data;
	private int distance = 10;

	public boolean isBuilt = false;

	@Override
	public ISpell build(SpellData data, CompoundNBT compound) {
		this.data = data;
		if (compound.contains("distance"))
			this.distance = compound.getInt("distance");
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
		return  8
				+ SpellValues.getOrDefault(data.firstModifier,0)
				+ SpellValues.getOrDefault(data.secondModifier,0)
				+ SpellValues.getOrDefault(data.sinModifier,0)
				+ SpellValues.getOrDefault(data.primaryCast.getSecond(),0)
				+ SpellValues.getOrDefault(data.plusCast.getSecond(),0);
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
	public void useOnPlayer(PlayerEntity playerTarget) {
		playerTarget.sendMessage(new TranslationTextComponent("status.invalidspell"));
	}

	@Override
	public void useOnEntity(PlayerEntity caster, Entity entityTarget) {
		caster.sendMessage(new TranslationTextComponent("status.invalidspell"));
	}

	public void useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget) {
		try {
			if(caster.world.isRemote) return;
			BlockState blockToDestroy = caster.world.getBlockState(blockTarget);
			if (blockToDestroy.getBlock().canHarvestBlock(blockToDestroy, caster.world, blockTarget, caster) && blockToDestroy.getHarvestLevel() <= getMiningLevel()) {
				caster.world.destroyBlock(blockTarget, true, caster); // TODO: Add fortune and explosion power
				blockToDestroy.updateNeighbors(caster.world,blockTarget,3);
			}
		} catch (SpellNotBuiltError spellNotBuiltError) {
			spellNotBuiltError.printStackTrace();
		}
	}
}
