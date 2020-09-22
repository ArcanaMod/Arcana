package net.arcanamod.systems.spell.impls;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.*;
import net.arcanamod.util.RayTraceUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class MiningSpell extends Spell {

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
		return Arcana.arcLoc("mining");
	}

	@Override
	public Aspect getSpellAspect() {
		return Aspects.MINING;
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
	public List<AspectStack> getAspectCosts() {
		return Collections.singletonList(new AspectStack(Aspects.EARTH, 1));
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
		return modAspects.length >= 1 ? SpellValues.getOrDefault(modAspects[0], 2) : 2;
	}

	public int getExplosivePower() throws SpellNotBuiltError {
		if (!isBuilt) throw new SpellNotBuiltError();
		return modAspects.length >= 2 ? SpellValues.getOrDefault(modAspects[1], 0) : 0;
	}

	public int getFortune() throws SpellNotBuiltError {
		if (!isBuilt) throw new SpellNotBuiltError();
		return modAspects.length >= 3 ? SpellValues.getOrDefault(modAspects[2], 0) : 0;
	}

	@Override
	public void use(PlayerEntity player, Action action){
		if(player.world.isRemote)
			return;
		if(castAspects.length < 3 || castAspects[0].isEmpty() && castAspects[1].isEmpty() && castAspects[2].isEmpty())
			defaultUse(player);
		else
			Spell.useCasts(this, player, castAspects);
	}

	@Override
	public void onAirCast(PlayerEntity caster, World world, BlockPos pos, int area, int duration) {
		caster.sendMessage(new TranslationTextComponent("status.invalidspell"));
	}

	@Override
	public void onWaterCast(PlayerEntity caster, List<Entity> entityTargets) {
		caster.sendMessage(new TranslationTextComponent("status.invalidspell"));
	}


	@Override
	public void onFireCast(PlayerEntity caster, @Nullable Entity entityTarget, BlockPos blockTarget) {
		try {
			if (caster.world.isRemote) return;
				BlockState blockToDestroy = caster.world.getBlockState(blockTarget);
			if (blockToDestroy.getBlock().canHarvestBlock(blockToDestroy, caster.world, blockTarget, caster) && blockToDestroy.getHarvestLevel() <= getMiningLevel()) {
				caster.world.destroyBlock(blockTarget, true, caster); // TODO: Add fortune and explosion power
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
				caster.world.destroyBlock(blockTarget, true, caster); // TODO: Add fortune and explosion power
				blockToDestroy.updateNeighbors(caster.world,blockTarget,3);
			}
		} catch (SpellNotBuiltError spellNotBuiltError) {
			spellNotBuiltError.printStackTrace();
		}
	}

	@Override
	public void onOrderCast(PlayerEntity playerTarget) {
		playerTarget.sendMessage(new TranslationTextComponent("status.invalidspell"));
	}

	@Override
	public void onChaosCast(PlayerEntity caster, Entity entityTarget, BlockPos blockTarget) {
		try {
			if (caster.world.isRemote) return;
			BlockState blockToDestroy = caster.world.getBlockState(blockTarget);
			if (blockToDestroy.getBlock().canHarvestBlock(blockToDestroy, caster.world, blockTarget, caster) && blockToDestroy.getHarvestLevel() <= getMiningLevel()) {
				caster.world.destroyBlock(blockTarget, true, caster); // TODO: Add fortune and explosion power
				blockToDestroy.updateNeighbors(caster.world,blockTarget,3);
			}
		} catch (SpellNotBuiltError spellNotBuiltError) {
			spellNotBuiltError.printStackTrace();
		}
	}

	protected void defaultUse(PlayerEntity caster) {
		try {
			BlockPos pos = RayTraceUtils.getTargetBlockPos(caster, caster.world, distance);
			BlockState blockToDestroy = caster.world.getBlockState(pos);
			if (blockToDestroy.getBlock().canHarvestBlock(blockToDestroy, caster.world, pos, caster) && blockToDestroy.getHarvestLevel() <= getMiningLevel()) {
				caster.world.destroyBlock(pos, true, caster); // TODO: Add fortune and explosion power
				blockToDestroy.updateNeighbors(caster.world,pos,3);
			}
		} catch (SpellNotBuiltError spellNotBuiltError) {
			spellNotBuiltError.printStackTrace();
		}
	}
}
