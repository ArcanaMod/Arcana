package net.arcanamod.aspects;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.arcanamod.blocks.Taint;
import net.arcanamod.util.Pair;
import net.arcanamod.world.NodeType;
import net.minecraft.util.ResourceLocation;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.arcanamod.Arcana.arcLoc;
import static net.arcanamod.util.Pair.of;

public class Aspects {

	/**
	 * Returns array of ALL aspects. Including Empty.
	 * @deprecated Use getAll();
	 * @return ALL aspects.
	 */
	@Deprecated
	public static Aspect[] values() {
		return ASPECTS.values().toArray(new Aspect[ASPECTS.size()]);
	}

	protected static BiMap<ResourceLocation, Aspect> ASPECTS = HashBiMap.create();

	public static final Aspect
			EMPTY = Aspect.create("empty",AspectColorRange.create(0x0, 0x0, 0x0, 0x0, 0x0),null),

			AIR = Aspect.create("air",AspectColorRange.create(0x7c554b, 0xa7846a, 0xe8d2a0, 0xf4f0c4, 0xfdffe5),null),
			CHAOS = Aspect.create("chaos",AspectColorRange.create(0x90909, 0x1e1e1f, 0x4d4d4d, 0x757377, 0x9a979c),null),
			FIRE = Aspect.create("fire",AspectColorRange.create(0x972015, 0xd73d21, 0xed8a50, 0xf2c66c, 0xf4f2c6),null),
			EARTH = Aspect.create("earth",AspectColorRange.create(0x1d6861, 0x2ba24e, 0x58dd5f, 0xb4f87e, 0xfdffe5),null),
			ORDER = Aspect.create("order",AspectColorRange.create(0x6a686c, 0x9a979c, 0xb7b7b7, 0xd9d9d4, 0xfefff7),null),
			WATER = Aspect.create("water",AspectColorRange.create(0x26317c, 0x4b6fb0, 0x4aaed6, 0xa0e2cb, 0xefeee6),null),

			ARMOUR = Aspect.create("armour",AspectColorRange.create(0x4a456f, 0x685683, 0x7a71b4, 0xa589cb, 0xd0b5e0),null),
			AURA = Aspect.create("aura",AspectColorRange.create(0x456978, 0x699fa7, 0x82c7bd, 0xa0e2cb, 0xefeee6),null),
			BEAST = Aspect.create("beast",AspectColorRange.create(0x5a1925, 0x87332d, 0x9c533f, 0xb27357, 0xd09c6b),null),
			CRAFTING = Aspect.create("crafting",AspectColorRange.create(0x381627, 0x5a2835, 0x804246, 0xa25f52, 0xb2826b),null),
			CREATION = Aspect.create("creation",AspectColorRange.create(0x0, 0x0, 0x0, 0x0, 0x0),null),
			CRYSTAL = Aspect.create("crystal",AspectColorRange.create(0x505fbe, 0x6aa5d6, 0x90cfed, 0xbeedf6, 0xffffff),null),
			DARKNESS = Aspect.create("darkness",AspectColorRange.create(0x0, 0x0, 0x0, 0x0, 0x0),null),
			DEATH = Aspect.create("death",AspectColorRange.create(0xb0b10, 0x1e1b28, 0x2c2438, 0x3f354a, 0x644d6a),null),
			ELDRITCH = Aspect.create("eldritch",AspectColorRange.create(0x6a0627, 0xa70d29, 0xc71a14, 0xe65a2d, 0xf2af67),null),
			ENDER = Aspect.create("ender",AspectColorRange.create(0x8080b, 0x1a1a26, 0x262036, 0x4d374b, 0x902d79),null),
			ENERGY = Aspect.create("energy",AspectColorRange.create(0x411016, 0x681230, 0x971227, 0xb22053, 0xcf438f),null),
			ENVY = Aspect.create("envy",AspectColorRange.create(0x160c20, 0x391848, 0x52215d, 0x6e2b71, 0x9c3394),null),
			EXCHANGE = Aspect.create("exchange",AspectColorRange.create(0x78155d, 0xc22563, 0xe22d49, 0xeb9164, 0xf6cc8a),null),
			FABRIC = Aspect.create("fabric",AspectColorRange.create(0x372961, 0x5b3781, 0x7e469e, 0xae59b7, 0xd284cd),null),
			FLESH = Aspect.create("flesh",AspectColorRange.create(0x3b1531, 0x5a1e3f, 0x71263e, 0xa9404d, 0xbe6e5b),null),
			FLIGHT = Aspect.create("flight",AspectColorRange.create(0x656d79, 0xa8b6bd, 0xbed1d4, 0xdee9e8, 0xffffff),null),
			GLUTTONY = Aspect.create("gluttony",AspectColorRange.create(0x4b0a14, 0x811811, 0xb23a13, 0xd26821, 0xe9ad32),null),
			GREED = Aspect.create("greed",AspectColorRange.create(0xa94f0d, 0xd08713, 0xefcc25, 0xf4f62d, 0xfdffe2),null),
			HARVEST = Aspect.create("harvest",AspectColorRange.create(0x256d6f, 0x35937b, 0x46b489, 0x54dd78, 0x8af17f),null),
			HUMAN = Aspect.create("human",AspectColorRange.create(0x160a0e, 0x261215, 0x3b211a, 0x513928, 0x6a4f33),null),
			ICE = Aspect.create("ice",AspectColorRange.create(0x678ab4, 0x94c1e0, 0xadddeb, 0xd3f6fb, 0xfafbfb),null),
			IMPRISON = Aspect.create("imprison",AspectColorRange.create(0x131214, 0x292629, 0x51464c, 0x756168, 0x907b7e),null),
			JOURNEY = Aspect.create("journey",AspectColorRange.create(0x8a2723, 0xb46147, 0xc69876, 0xd6b68a, 0xe8d2a0),null),
			LIFE = Aspect.create("life",AspectColorRange.create(0x712269, 0x883462, 0xb21f4e, 0xd2243a, 0xe2422a),null),
			LIGHT = Aspect.create("light",AspectColorRange.create(0xd6ae9e, 0xedd6c0, 0xfdf1dd, 0xffffff, 0xffffff),null),
			LUST = Aspect.create("lust",AspectColorRange.create(0x460742, 0x780b55, 0xd00970, 0xfb1772, 0xfba2b2),null),
			MACHINE = Aspect.create("machine",AspectColorRange.create(0xd0d1b, 0x201a34, 0x2a2b56, 0x404e6f, 0x52677e),null),
			MANA = Aspect.create("mana",AspectColorRange.create(0x400e6f, 0x8d1ca9, 0xc21ec7, 0xe242b3, 0xf881d6),null),
			METAL = Aspect.create("metal",AspectColorRange.create(0x3a383b, 0x504d51, 0x6e6a6f, 0x97939a, 0xb7b5c2),null),
			MINING = Aspect.create("mining",AspectColorRange.create(0xae6658, 0xd09367, 0xe9bf7c, 0xfaeaa8, 0xfafbd4),null),
			MIND = Aspect.create("mind",AspectColorRange.create(0x116d95, 0x14cbcb, 0x33e9ce, 0x74fab8, 0xd3fbd2),null),
			MOVEMENT = Aspect.create("movement",AspectColorRange.create(0x716468, 0x918a8a, 0xab9a96, 0xd0b9ac, 0xe6d3c0),null),
			NETHER = Aspect.create("nether",AspectColorRange.create(0x141229, 0x251b42, 0x481d5c, 0x631d6f, 0x911f8d),null),
			OVERWORLD = Aspect.create("overworld",AspectColorRange.create(0x261e1d, 0x3d312c, 0x544c3e, 0x677841, 0x6dac4c),null),
			PLANT = Aspect.create("plant",AspectColorRange.create(0x30a01d, 0x95e92e, 0xcaf464, 0xeef88d, 0xf7f8e6),null),
			PRIDE = Aspect.create("pride",AspectColorRange.create(0x227748, 0x2da23a, 0x88ed6a, 0xc1fb99, 0xf4fad5),null),
			SEEDS = Aspect.create("seeds",AspectColorRange.create(0x4f5c22, 0x7a7927, 0x8d9135, 0xab983d, 0xc2bd59),null),
			SENSES = Aspect.create("senses",AspectColorRange.create(0x38258e, 0x3f4dbb, 0x5374cf, 0x73b0f2, 0xa2dff6),null),
			SLIME = Aspect.create("slime",AspectColorRange.create(0x438844, 0x5eb06b, 0x79d473, 0xc9f181, 0xfbffe5),null),
			SLOTH = Aspect.create("sloth",AspectColorRange.create(0xc365d, 0xd5f7e, 0x2699a9, 0x3dcbcb, 0x5cede0),null),
			SPIRIT = Aspect.create("spirit",AspectColorRange.create(0x56638c, 0x688aa3, 0x8dc4cf, 0xc1f4ea, 0xf3ffff),null),
			STRENGTH = Aspect.create("strength",AspectColorRange.create(0x251c53, 0x2f3480, 0x2f5d95, 0x447ea7, 0x5aa8bb),null),
			TAINT = Aspect.create("taint",AspectColorRange.create(0x231638, 0x4e2c5c, 0x6e3877, 0x903e8e, 0xa95aa0), Taint::tickTaintInContainer),
			TOOL = Aspect.create("tool",AspectColorRange.create(0x27223d, 0x373556, 0x41487c, 0x4c6497, 0x608eb4),null),
			TREE = Aspect.create("tree",AspectColorRange.create(0x462635, 0x5d303b, 0x783b41, 0x955549, 0xb06756),null),
			UNDEAD = Aspect.create("undead",AspectColorRange.create(0x16261e, 0x1f4d27, 0x396c28, 0x6d8e37, 0xb9c450),null),
			VACUUM = Aspect.create("vacuum",AspectColorRange.create(0x1a1a26, 0x1a1a26, 0x1a1a26, 0x262036, 0x2e2c4b),null),
			VENOM = Aspect.create("venom",AspectColorRange.create(0xa0812, 0x1d1429, 0x2c223f, 0x2f3953, 0x36685d),null),
			WEAPON = Aspect.create("weapon",AspectColorRange.create(0x6a1445, 0x911b41, 0xb71831, 0xd94023, 0xf67c30),null),
			WIND = Aspect.create("wind",AspectColorRange.create(0x5d5f71, 0x888599, 0x9ea0bd, 0xccd0df, 0xfffde8),null),
			WRATH = Aspect.create("wrath",AspectColorRange.create(0x5a0b3d, 0x8e0f3e, 0xc61626, 0xed1507, 0xffbe8d),null);
	
	public static final BiMap<Pair<Aspect, Aspect>, Aspect> combinations = HashBiMap.create(Aspects.values().length);
	
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
		return Aspects.combinations.containsKey(components) ? Aspects.combinations.get(components) : Aspects.combinations.getOrDefault(components.flip(), null);
	}

	/**
	 * Returns array of ALL aspects. Including Empty.
	 * @return ALL aspects.
	 */
	public static List<Aspect> getWithoutEmpty()
	{
		Aspect[] modified_values = Arrays.copyOfRange(values(),1,values().length);
		List<Aspect> aspects = Arrays.asList(modified_values);
		return aspects;
	}

	/**
	 * Returns array of ALL aspects. Excluding Empty.
	 * @return ALL aspects.
	 */
	public static List<Aspect> getAll()
	{
		return new ArrayList<>(ASPECTS.values());
	}

	public static Aspect valueOf(String value) {
		return ASPECTS.values().stream()
				.filter(entry -> entry.name().equalsIgnoreCase(value))
				.findAny().orElse(Aspects.EMPTY);
	}

	public static void init() {}
}