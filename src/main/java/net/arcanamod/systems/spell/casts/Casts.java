package net.arcanamod.systems.spell.casts;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.arcanamod.systems.spell.casts.impl.*;
import net.minecraft.util.ResourceLocation;

public class Casts {
	public static final BiMap<ResourceLocation, ICast> castMap = HashBiMap.create();

	// Don't build in registry!
	public static final ICast EMPTY_SPELL = new TemporaryEmptyCast(); // This temporary variable will be removed
	public static final ICast MINING_CAST = new MiningCast();
	public static final ICast EXCHANGE_CAST = new ExchangeCast();
	public static final ICast FABRIC_CAST = new FabricCast();
	public static final ICast VACUUM_CAST = new VacuumCast();
	public static final ICast WARDING_CAST = new ArmourCast();
}
