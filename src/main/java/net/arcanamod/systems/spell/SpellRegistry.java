package net.arcanamod.systems.spell;

import net.arcanamod.systems.spell.casts.Casts;
import net.arcanamod.systems.spell.casts.ICast;
import net.minecraft.util.ResourceLocation;

/**
 * API's can register spells here!
 */
public class SpellRegistry {
	public static void addSpell(ResourceLocation id, ICast spell){
		if (!Casts.spellMap.containsKey(id))
			Casts.spellMap.put(id,spell);
	}
}
