package net.arcanamod.systems.spell;

import net.minecraft.util.ResourceLocation;

public class SpellRegistry {
	public static void addSpell(ResourceLocation id, ISpell spell){
		if (!Spells.spellMap.containsKey(id))
			Spells.spellMap.put(id,spell);
	}
}
