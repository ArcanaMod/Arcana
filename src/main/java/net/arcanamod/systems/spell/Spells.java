package net.arcanamod.systems.spell;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.arcanamod.systems.spell.casts.*;
import net.minecraft.util.ResourceLocation;

public class Spells {
	public static final BiMap<ResourceLocation, IOldSpell> spellMap = HashBiMap.create();

	// Don't build in registry!
	public static final IOldSpell EMPTY_SPELL = new TemporaryEmptyCast(); // This temporary variable will be removed
	public static final IOldSpell MINING_SPELL = new MiningCast();
	public static final IOldSpell EXCHANGE_SPELL = new ExchangeCast();
	public static final IOldSpell FABRIC_SPELL = new FabricCast();
	public static final IOldSpell VACUUM_SPELL = new VacuumCast();
	public static final IOldSpell WARDING_SPELL = new WardingCast();
}
