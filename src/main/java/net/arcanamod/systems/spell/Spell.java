package net.arcanamod.systems.spell;

import net.arcanamod.aspects.Aspect;

public class Spell implements ISpell {

	@Override
	public Aspect getSpellAspect() {
		return null;
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
	public int getComplexity() {
		return 0;
	}
}
