package net.arcanamod.systems.spell;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.arcanamod.systems.spell.casts.*;
import net.minecraft.util.ResourceLocation;

public class Spells {
	public static final BiMap<ResourceLocation,ISpell> spellMap = HashBiMap.create();

	// Don't build in registry!
	public static final ISpell EMPTY_SPELL = new TemporaryEmptyCast(); // This temporary variable will be removed
	public static final ISpell MINING_SPELL = new MiningCast();
	public static final ISpell EXCHANGE_SPELL = new ExchangeCast();
	public static final ISpell FABRIC_SPELL = new FabricCast();
	public static final ISpell VACUUM_SPELL = new VacuumCast();
	public static final ISpell WARDING_SPELL = new WardingCast();
}
