package net.arcanamod.systems.spell;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.arcanamod.systems.spell.impls.*;
import net.minecraft.util.ResourceLocation;

public class Spells {
	public static final BiMap<ResourceLocation,ISpell> spellMap = HashBiMap.create();

	// Don't build in registry!
	public static final ISpell EMPTY_SPELL = new EmptySpell(); // This temporary variable will be removed
	public static final ISpell MINING_SPELL = new MiningSpell();
	public static final ISpell EXCHANGE_SPELL = new ExchangeSpell();
	public static final ISpell FABRIC_SPELL = new FabricSpell();
	public static final ISpell VACUUM_SPELL = new VacuumSpell();
	public static final ISpell WARDING_SPELL = new WardingSpell();
}
