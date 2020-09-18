package net.arcanamod.systems.spell.impls;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.CastAspect;
import net.arcanamod.systems.spell.ISpell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class MiningSpell extends DefaultSpell {

	private int miningLevel;
	private int explosivePower;
	private int fortune; // Sin Mod

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
		return new Aspect[0];
	}

	@Override
	public CastAspect[] getCastAspects() {
		return new CastAspect[0];
	}

	@Override
	public AspectStack[] getAspectCosts() {
		return new AspectStack[0];
	}

	@Override
	public int getComplexity() {
		return 0;
	}

	@Override
	public void use(PlayerEntity player, Action action) {

	}
}
