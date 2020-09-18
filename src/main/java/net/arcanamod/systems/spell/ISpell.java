package net.arcanamod.systems.spell;

import net.arcanamod.aspects.Aspect;

public interface ISpell {

	Aspect getSpellAspect();
	Aspect[] getModAspects(); // Mod 1, Mod 2
	CastAspect[] getCastAspects(); // Cast, Cast+

	int getComplexity();



}
