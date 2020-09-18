package net.arcanamod.systems.spell;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;

import java.util.HashMap;

public class SpellValues {
	public static final HashMap<Aspect, Integer> modifierValues = new HashMap<>();

	public static int getOrDefault(Aspect aspect, int defaultValue){
		return modifierValues.getOrDefault(aspect, defaultValue);
	}

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
