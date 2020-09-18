package net.arcanamod.systems.spell.impls;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.CastAspect;
import net.arcanamod.systems.spell.ISpell;
import net.arcanamod.systems.spell.SpellExtraData;
import net.arcanamod.systems.spell.SpellValues;
import net.arcanamod.util.RayTraceUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class MiningSpell extends DefaultSpell {

	private Aspect[] modAspects;
	private CastAspect[] castAspects;
	private int distance = 10;

	public boolean isBuild = false;

	@Override
	public ISpell build(Aspect[] modAspects, CastAspect[] castAspects, SpellExtraData data) {
		this.modAspects = modAspects;
		this.castAspects = castAspects;
		if (data.contains("distance"))
			this.distance = data.read("distance");
		isBuild = true;
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
	public AspectStack[] getAspectCosts() {
		// Currently EVERYTHING uses ORDER aspect.
		return new AspectStack[] {new AspectStack(Aspects.ORDER,1)};
	}

	@Override
	public int getComplexity() {
		if (!isBuild) return -2;
		int temp = 0;
		for (Aspect aspect : modAspects){
			temp += SpellValues.modifierValues.getOrDefault(aspect, 0);
		}
		return temp != 0 ? -1 : temp;
	}

	public int getMiningLevel() {
		return SpellValues.modifierValues.getOrDefault(modAspects[0], 2);
	}

	public int getExplosivePower() {
		return SpellValues.modifierValues.getOrDefault(modAspects[1], 0);
	}

	public int getFortune() {
		return SpellValues.modifierValues.getOrDefault(modAspects[2], 0);
	}

	@Override
	public void use(PlayerEntity player, Action action) {
		BlockPos pos = RayTraceUtils.getTargetBlockPos(player,player.world,distance);
		BlockState blockToDestroy = player.world.getBlockState(pos);
		if (blockToDestroy.getBlock().canHarvestBlock(blockToDestroy,player.world,pos,player) && blockToDestroy.getHarvestLevel()>=getMiningLevel())
			player.world.destroyBlock(pos,true,player);
	}
}
