package net.arcanamod.aspects;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.arcanamod.util.Pair;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static net.arcanamod.util.Pair.of;

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
	CREATION,
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
		combinations.put(of(FIRE, CHAOS), ICE);
		combinations.put(of(FIRE, AIR), LIGHT);
		combinations.put(of(ORDER, AIR), MOVEMENT);
		combinations.put(of(ORDER, CHAOS), EXCHANGE);
		combinations.put(of(FIRE, ORDER), STRENGTH);
		combinations.put(of(AIR, WATER), WIND);
		combinations.put(of(AIR, CHAOS), VACUUM);
		combinations.put(of(WATER, CHAOS), VENOM);
		combinations.put(of(WATER, EARTH), LIFE);
		combinations.put(of(ORDER, EARTH), CRYSTAL);
		
		combinations.put(of(LIGHT, LIFE), OVERWORLD);
		combinations.put(of(MOVEMENT, LIFE), BEAST);
		combinations.put(of(EARTH, LIFE), PLANT);
		combinations.put(of(EARTH, MOVEMENT), JOURNEY);
		combinations.put(of(WATER, LIFE), SLIME);
		combinations.put(of(EARTH, CRYSTAL), METAL);
		combinations.put(of(LIFE, CHAOS), DEATH);
		combinations.put(of(STRENGTH, VACUUM), MANA);
		combinations.put(of(LIGHT, VACUUM), DARKNESS);
		combinations.put(of(MOVEMENT, CHAOS), IMPRISON);
		combinations.put(of(AIR, MOVEMENT), FLIGHT);
		
		combinations.put(of(VACUUM, DARKNESS), ENDER);
		combinations.put(of(AIR, PLANT), TREE);
		combinations.put(of(AIR, MANA), AURA);
		combinations.put(of(BEAST, DEATH), FLESH);
		combinations.put(of(MOVEMENT, DEATH), UNDEAD);
		combinations.put(of(LIFE, DEATH), SPIRIT);
		
		combinations.put(of(AIR, SPIRIT), SENSES);
		combinations.put(of(ORDER, SPIRIT), MIND);
		combinations.put(of(FIRE, SPIRIT), NETHER);
		
		combinations.put(of(MIND, CHAOS), ELDRITCH);
		combinations.put(of(MANA, CHAOS), TAINT);
		combinations.put(of(MIND, SENSES), CREATION);
		
		combinations.put(of(CREATION, ORDER), TOOL);
		combinations.put(of(CREATION, PLANT), SEEDS);
		combinations.put(of(CREATION, EARTH), MINING);
		
		combinations.put(of(CREATION, TOOL), CRAFTING);
		combinations.put(of(MOVEMENT, TOOL), MACHINE);
		combinations.put(of(SEEDS, TOOL), HARVEST);
		combinations.put(of(BEAST, TOOL), FABRIC);
		combinations.put(of(FIRE, TOOL), WEAPON);
		combinations.put(of(CREATION, TOOL), ARMOUR);
		combinations.put(of(CREATION, MIND), HUMAN);
		
		combinations.put(of(HUMAN, FLESH), LUST);
		combinations.put(of(HUMAN, IMPRISON), SLOTH);
		combinations.put(of(HUMAN, VACUUM), GLUTTONY);
		combinations.put(of(HUMAN, WEAPON), WRATH);
		combinations.put(of(HUMAN, CREATION), ENVY);
		combinations.put(of(HUMAN, FABRIC), PRIDE);
		combinations.put(of(HUMAN, METAL), GREED);
	}
	
	@Nullable
	public static Aspect getCompound(Pair<Aspect, Aspect> components){
		return Aspect.combinations.containsKey(components) ? Aspect.combinations.get(components) : Aspect.combinations.getOrDefault(components.flip(), null);
	}
	
	public static final List<Aspect> aspects = Arrays.asList(values());
}