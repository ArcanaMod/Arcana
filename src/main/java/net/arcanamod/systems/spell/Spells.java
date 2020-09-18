package net.arcanamod.systems.spell;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.arcanamod.systems.spell.impls.MiningSpell;
import net.minecraft.util.ResourceLocation;

public class Spells {
	public static final BiMap<ResourceLocation,ISpell> spellMap = HashBiMap.create();

	public static final ISpell MINING_SPELL = new MiningSpell();
}
