package net.arcanamod.systems.spell.casts;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.arcanamod.systems.spell.casts.*;
import net.minecraft.util.ResourceLocation;

public class Casts {
	public static final BiMap<ResourceLocation, ICast> spellMap = HashBiMap.create();

	// Don't build in registry!
	public static final ICast EMPTY_SPELL = new TemporaryEmptyCast(); // This temporary variable will be removed
	public static final ICast MINING_SPELL = new MiningCast();
	public static final ICast EXCHANGE_SPELL = new ExchangeCast();
	public static final ICast FABRIC_SPELL = new FabricCast();
	public static final ICast VACUUM_SPELL = new VacuumCast();
	public static final ICast WARDING_SPELL = new WardingCast();
}
