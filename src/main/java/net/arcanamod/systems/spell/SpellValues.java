package net.arcanamod.systems.spell;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;

public class SpellValues {
	public static final BiMap<Aspect, Integer> modifierValues = HashBiMap.create();

	static{
		modifierValues.put(Aspects.AIR,       1);
		modifierValues.put(Aspects.WATER,     3);
		modifierValues.put(Aspects.FIRE,      5);
		modifierValues.put(Aspects.EARTH,     7);
		modifierValues.put(Aspects.ORDER,     9);
		modifierValues.put(Aspects.CHAOS,    11);

		modifierValues.put(Aspects.ENVY,      1);
		modifierValues.put(Aspects.LUST,      2);
		modifierValues.put(Aspects.SLOTH,     3);
		modifierValues.put(Aspects.PRIDE,     4);
		modifierValues.put(Aspects.GREED,     5);
		modifierValues.put(Aspects.GLUTTONY,  6);
		modifierValues.put(Aspects.WRATH,     7);
	}
}
