package net.kineticdevelopment.arcana.core.aspects;

import net.kineticdevelopment.arcana.utilities.Pair;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public static final Map<Pair<Aspect, Aspect>, Aspect> combinations = new HashMap<>();
	
	static{
		combinations.put(of(MANA, AIR), AURA);
		combinations.put(of(WATER, EARTH), LIFE);
		combinations.put(of(LIFE, EARTH), TREE);
		combinations.put(of(LIFE, WATER), SLIME);
		combinations.put(of(ORDER, FIRE), ENERGY);
		combinations.put(of(ORDER, EARTH), METAL);
		combinations.put(of(ENERGY, AIR), MOVEMENT);
		combinations.put(of(ENERGY, MOVEMENT), WIND);
		combinations.put(of(CHAOS, LIFE), VENOM);
		combinations.put(of(ORDER, ENERGY), LIGHT);
	}
	
	@Nullable
	public static Aspect getCompound(Pair<Aspect, Aspect> components){
		return Aspect.combinations.containsKey(components) ? Aspect.combinations.get(components) : Aspect.combinations.getOrDefault(components.flip(), null);
	}
	
	public static final List<Aspect> aspects = Arrays.asList(values());
}