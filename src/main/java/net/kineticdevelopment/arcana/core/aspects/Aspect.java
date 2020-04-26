package net.kineticdevelopment.arcana.core.aspects;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.kineticdevelopment.arcana.utilities.Pair;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static net.kineticdevelopment.arcana.utilities.Pair.of;

/**
 * Enumeration containing all aspect types
 *
 * @author Atlas
 */
public enum Aspect{
	AIR,
	CHAOS,
	FIRE,
	EARTH,
	ORDER,
	WATER,
	
	ARMOUR,
	AURA,
	BEAST,
	CRAFTING,
	CRYSTAL,
	DARKNESS,
	DEATH,
	ELDRITCH,
	ENDER,
	ENERGY,
	ENVY,
	EXCHANGE,
	FABRIC,
	FLESH,
	FLIGHT,
	GLUTTONY,
	GREED,
	HARVEST,
	HUMAN,
	ICE,
	IMPRISON,
	JOURNEY,
	LIFE,
	LIGHT,
	LUST,
	MACHINE,
	MANA,
	METAL,
	MINING,
	MIND,
	MOVEMENT,
	NETHER,
	OVERWORLD,
	PLANT,
	PRIDE,
	SEEDS,
	SENSES,
	SLIME,
	SLOTH,
	SPIRIT,
	STRENGTH,
	TAINT,
	TOOL,
	TREE,
	UNDEAD,
	VACUUM,
	VENOM,
	WEAPON,
	WIND,
	WRATH;
	
	public static final BiMap<Pair<Aspect, Aspect>, Aspect> combinations = HashBiMap.create(Aspect.values().length);
	
	static{
		combinations.put(of(MANA, AIR), AURA);
		combinations.put(of(WATER, EARTH), LIFE);
		combinations.put(of(LIFE, EARTH), PLANT);
		combinations.put(of(PLANT, AIR), TREE);
		combinations.put(of(LIFE, WATER), SLIME);
		combinations.put(of(ORDER, FIRE), ENERGY);
		combinations.put(of(ORDER, EARTH), CRYSTAL);
		combinations.put(of(CRYSTAL, EARTH), METAL);
		combinations.put(of(ENERGY, AIR), MOVEMENT);
		combinations.put(of(ENERGY, MOVEMENT), WIND);
		combinations.put(of(CHAOS, WATER), VENOM);
		combinations.put(of(CHAOS, LIFE), DEATH);
		combinations.put(of(ORDER, ENERGY), LIGHT);
		combinations.put(of(ORDER, CHAOS), EXCHANGE);
		combinations.put(of(CHAOS, AIR), VACUUM);
		combinations.put(of(VACUUM, LIGHT), DARKNESS);
		combinations.put(of(VACUUM, ENERGY), MANA);
		combinations.put(of(LIFE, DEATH), SPIRIT);
		combinations.put(of(MOVEMENT, LIFE), BEAST);
		combinations.put(of(SPIRIT, AIR), SENSES);
		combinations.put(of(MANA, CHAOS), TAINT);
		combinations.put(of(MOVEMENT, AIR), FLIGHT);
		combinations.put(of(CHAOS, MOVEMENT), IMPRISON);
		combinations.put(of(FIRE, SPIRIT), MIND);
		combinations.put(of(MIND, BEAST), HUMAN);
		combinations.put(of(HUMAN, ORDER), TOOL);
		combinations.put(of(TOOL, FIRE), WEAPON);
		combinations.put(of(MOVEMENT, EARTH), JOURNEY);
		combinations.put(of(MOVEMENT, TOOL), MACHINE);
		combinations.put(of(DEATH, MOVEMENT), UNDEAD);
		combinations.put(of(VACUUM, LIGHT), GREED);
		combinations.put(of(CHAOS, FIRE), ICE);
		combinations.put(of(TOOL, EARTH), ARMOUR);
		combinations.put(of(HUMAN, TOOL), CRAFTING);
		combinations.put(of(DARKNESS, VACUUM), ELDRITCH);
		combinations.put(of(ELDRITCH, AIR), ENDER);
		combinations.put(of(FLESH, GREED), LUST);
		combinations.put(of(GREED, SENSES), ENVY);
		combinations.put(of(ORDER, LIFE), SEEDS);
		combinations.put(of(TOOL, BEAST), FABRIC);
		combinations.put(of(DEATH, BEAST), FLESH);
		combinations.put(of(FLESH, LIFE), GLUTTONY);
		combinations.put(of(TOOL, PLANT), HARVEST);
		combinations.put(of(HUMAN, EARTH), MINING);
		combinations.put(of(EARTH, FIRE), NETHER);
		combinations.put(of(LIGHT, LIFE), OVERWORLD);
		combinations.put(of(WEAPON, ARMOUR), STRENGTH);
		combinations.put(of(GLUTTONY, MOVEMENT), SLOTH);
		combinations.put(of(GLUTTONY, STRENGTH), PRIDE);
		combinations.put(of(STRENGTH, FIRE), WRATH);
	}
	
	@Nullable
	public static Aspect getCompound(Pair<Aspect, Aspect> components){
		return Aspect.combinations.containsKey(components) ? Aspect.combinations.get(components) : Aspect.combinations.getOrDefault(components.flip(), null);
	}
	
	public static final List<Aspect> aspects = Arrays.asList(values());
}