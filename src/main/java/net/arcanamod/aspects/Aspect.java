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
	AIR(0x7c554b, 0xa7846a, 0xe8d2a0, 0xf4f0c4, 0xfdffe5),
	CHAOS(0x90909, 0x1e1e1f, 0x4d4d4d, 0x757377, 0x9a979c),
	FIRE(0x972015, 0xd73d21, 0xed8a50, 0xf2c66c, 0xf4f2c6),
	EARTH(0x1d6861, 0x2ba24e, 0x58dd5f, 0xb4f87e, 0xfdffe5),
	ORDER(0x6a686c, 0x9a979c, 0xb7b7b7, 0xd9d9d4, 0xfefff7),
	WATER(0x26317c, 0x4b6fb0, 0x4aaed6, 0xa0e2cb, 0xefeee6),

	ARMOUR(0x4a456f, 0x685683, 0x7a71b4, 0xa589cb, 0xd0b5e0),
	AURA(0x456978, 0x699fa7, 0x82c7bd, 0xa0e2cb, 0xefeee6),
	BEAST(0x5a1925, 0x87332d, 0x9c533f, 0xb27357, 0xd09c6b),
	CRAFTING(0x381627, 0x5a2835, 0x804246, 0xa25f52, 0xb2826b),
	CRYSTAL(0x505fbe, 0x6aa5d6, 0x90cfed, 0xbeedf6, 0xffffff),
	DARKNESS(0x0, 0x0, 0x0, 0x0, 0x0),
	DEATH(0xb0b10, 0x1e1b28, 0x2c2438, 0x3f354a, 0x644d6a),
	ELDRITCH(0x6a0627, 0xa70d29, 0xc71a14, 0xe65a2d, 0xf2af67),
	ENDER(0x8080b, 0x1a1a26, 0x262036, 0x4d374b, 0x902d79),
	ENERGY(0x411016, 0x681230, 0x971227, 0xb22053, 0xcf438f),
	ENVY(0x160c20, 0x391848, 0x52215d, 0x6e2b71, 0x9c3394),
	EXCHANGE(0x78155d, 0xc22563, 0xe22d49, 0xeb9164, 0xf6cc8a),
	FABRIC(0x372961, 0x5b3781, 0x7e469e, 0xae59b7, 0xd284cd),
	FLESH(0x3b1531, 0x5a1e3f, 0x71263e, 0xa9404d, 0xbe6e5b),
	FLIGHT(0x656d79, 0xa8b6bd, 0xbed1d4, 0xdee9e8, 0xffffff),
	GLUTTONY(0x4b0a14, 0x811811, 0xb23a13, 0xd26821, 0xe9ad32),
	GREED(0xa94f0d, 0xd08713, 0xefcc25, 0xf4f62d, 0xfdffe2),
	HARVEST(0x256d6f, 0x35937b, 0x46b489, 0x54dd78, 0x8af17f),
	HUMAN(0x160a0e, 0x261215, 0x3b211a, 0x513928, 0x6a4f33),
	ICE(0x678ab4, 0x94c1e0, 0xadddeb, 0xd3f6fb, 0xfafbfb),
	IMPRISON(0x131214, 0x292629, 0x51464c, 0x756168, 0x907b7e),
	JOURNEY(0x8a2723, 0xb46147, 0xc69876, 0xd6b68a, 0xe8d2a0),
	LIFE(0x712269, 0x883462, 0xb21f4e, 0xd2243a, 0xe2422a),
	LIGHT(0xd6ae9e, 0xedd6c0, 0xfdf1dd, 0xffffff, 0xffffff),
	LUST(0x460742, 0x780b55, 0xd00970, 0xfb1772, 0xfba2b2),
	MACHINE(0xd0d1b, 0x201a34, 0x2a2b56, 0x404e6f, 0x52677e),
	MANA(0x400e6f, 0x8d1ca9, 0xc21ec7, 0xe242b3, 0xf881d6),
	METAL(0x3a383b, 0x504d51, 0x6e6a6f, 0x97939a, 0xb7b5c2),
	MIND(0x116d95, 0x14cbcb, 0x33e9ce, 0x74fab8, 0xd3fbd2),
	MINING(0xae6658, 0xd09367, 0xe9bf7c, 0xfaeaa8, 0xfafbd4),
	MOVEMENT(0x716468, 0x918a8a, 0xab9a96, 0xd0b9ac, 0xe6d3c0),
	NETHER(0x141229, 0x251b42, 0x481d5c, 0x631d6f, 0x911f8d),
	OVERWORLD(0x261e1d, 0x3d312c, 0x544c3e, 0x677841, 0x6dac4c),
	PLANT(0x30a01d, 0x95e92e, 0xcaf464, 0xeef88d, 0xf7f8e6),
	VENOM(0xa0812, 0x1d1429, 0x2c223f, 0x2f3953, 0x36685d),
	PRIDE(0x227748, 0x2da23a, 0x88ed6a, 0xc1fb99, 0xf4fad5),
	SEEDS(0x4f5c22, 0x7a7927, 0x8d9135, 0xab983d, 0xc2bd59),
	SENSES(0x38258e, 0x3f4dbb, 0x5374cf, 0x73b0f2, 0xa2dff6),
	SLIME(0x438844, 0x5eb06b, 0x79d473, 0xc9f181, 0xfbffe5),
	SLOTH(0xc365d, 0xd5f7e, 0x2699a9, 0x3dcbcb, 0x5cede0),
	SPIRIT(0x56638c, 0x688aa3, 0x8dc4cf, 0xc1f4ea, 0xf3ffff),
	STRENGTH(0x251c53, 0x2f3480, 0x2f5d95, 0x447ea7, 0x5aa8bb),
	TAINT(0x231638, 0x4e2c5c, 0x6e3877, 0x903e8e, 0xa95aa0),
	TOOL(0x27223d, 0x373556, 0x41487c, 0x4c6497, 0x608eb4),
	TREE(0x462635, 0x5d303b, 0x783b41, 0x955549, 0xb06756),
	UNDEAD(0x16261e, 0x1f4d27, 0x396c28, 0x6d8e37, 0xb9c450),
	VACUUM(0x1a1a26, 0x1a1a26, 0x1a1a26, 0x262036, 0x2e2c4b),
	WEAPON(0x6a1445, 0x911b41, 0xb71831, 0xd94023, 0xf67c30),
	WIND(0x5d5f71, 0x888599, 0x9ea0bd, 0xccd0df, 0xfffde8),
	WRATH(0x5a0b3d, 0x8e0f3e, 0xc61626, 0xed1507, 0xffbe8d);
	
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

	private final int color_0;
	private final int color_1;
	private final int color_2;
	private final int color_3;
	private final int color_l;
	Aspect(int color_0, int color_1, int color_2, int color_3, int color_l)
	{
		this.color_0 = color_0;
		this.color_1 = color_1;
		this.color_2 = color_2;
		this.color_3 = color_3;
		this.color_l = color_l;
	}

	public int[] getAspectColor()
	{
		return new int[] {color_0,color_1,color_2,color_3,color_l};
	}

	@Nullable
	public static Aspect getCompound(Pair<Aspect, Aspect> components){
		return Aspect.combinations.containsKey(components) ? Aspect.combinations.get(components) : Aspect.combinations.getOrDefault(components.flip(), null);
	}
	
	public static final List<Aspect> aspects = Arrays.asList(values());
}