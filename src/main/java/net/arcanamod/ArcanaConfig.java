package net.arcanamod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

/**
 * Configuration
 *
 * @author Mozaran, Luna
 */
public class ArcanaConfig{
	
	private static final Builder COMMON_BUILDER = new Builder();
	private static final Builder CLIENT_BUILDER = new Builder();
	
	static{
		COMMON_BUILDER.push("General");
		ORE_RETROGEN = COMMON_BUILDER
				.comment("Enables Arcana ore generation in already-generated chunks.", "Chunks generated with Arcana will stay unaffected.")
				.define("OreRetrogen", false);
		NODE_RETROGEN = COMMON_BUILDER
				.comment("Enables node generation in already-generated chunks.", "Chunks generated with Arcana will stay unaffected.")
				.define("NodeRetrogen", false);
		VERBOSE = COMMON_BUILDER
				.comment("Enables verbose logging during retrogen.", "For debug purposes.")
				.define("VerboseRetrogen", false);
		GENERATE_OVERWORLD = COMMON_BUILDER
				.comment("Enables ore generation in the overworld.", "If disabled, Arcana's ores will not generate.")
				.define("OverworldGeneration", true);
		AMBER_MIN_VEIN_SIZE = COMMON_BUILDER
				.comment("The minimum size of amber ore veins.")
				.define("MinAmberSize", 4);
		AMBER_MAX_VEIN_SIZE = COMMON_BUILDER
				.comment("The maximum size of amber ore veins.")
				.define("MaxAmberSize", 8);
		AMBER_CHANCES_TO_SPAWN = COMMON_BUILDER
				.comment("The maximum number of amber ore veins per chunk.")
				.define("AmberSpawnChance", 3);
		AMBER_MIN_Y = COMMON_BUILDER
				.comment("The minimum Y level that amber ore will generate at.")
				.define("MinAmberY", 2);
		AMBER_MAX_Y = COMMON_BUILDER
				.comment("The maximum Y level that amber ore will generate at.")
				.define("MaxAmberY", 50);
		NODE_CHANCE = COMMON_BUILDER
				.comment("The chance of an aura node generating in a chunk, out of 1000.")
				.define("NodeChance", 1);
		SPECIAL_NODE_CHANCE = COMMON_BUILDER
				.comment("The chance of an aura node being a special (hungry, eldritch, bright, or pale) node, out of 100.")
				.define("SpecialNodeChance", 15);
		COMMON_BUILDER.pop();
		
		CLIENT_BUILDER.push("Client");
		CUSTOM_BOOK_WIDTH = CLIENT_BUILDER
				.comment("The width that the research book GUI should use.", "Setting it to -1 makes the GUI adjust to screen width automatically.")
				.define("ResearchBookWidth", -1);
		CUSTOM_BOOK_HEIGHT = CLIENT_BUILDER
				.comment("The height that the research book GUI should use.", "Setting it to -1 makes the GUI adjust to screen height automatically.")
				.define("ResearchBookHeight", -1);
		CLIENT_BUILDER.pop();
	}
	
	public static final ForgeConfigSpec COMMON_SPEC = COMMON_BUILDER.build();
	public static final ForgeConfigSpec CLIENT_SPEC = CLIENT_BUILDER.build();
	
	// Common
	public static ConfigValue<Boolean> ORE_RETROGEN; // false
	public static ConfigValue<Boolean> NODE_RETROGEN; // false
	public static ConfigValue<Boolean> VERBOSE; // false
	public static ConfigValue<Boolean> GENERATE_OVERWORLD; // true
	public static ConfigValue<Integer> AMBER_MIN_VEIN_SIZE; // 4
	public static ConfigValue<Integer> AMBER_MAX_VEIN_SIZE; // 8
	public static ConfigValue<Integer> AMBER_CHANCES_TO_SPAWN; // 3
	public static ConfigValue<Integer> AMBER_MIN_Y; // 2
	public static ConfigValue<Integer> AMBER_MAX_Y; // 50
	public static ConfigValue<Integer> NODE_CHANCE; // 1
	public static ConfigValue<Integer> SPECIAL_NODE_CHANCE; // 15
	
	// Client
	public static ConfigValue<Integer> CUSTOM_BOOK_WIDTH; // -1
	public static ConfigValue<Integer> CUSTOM_BOOK_HEIGHT; // -1
}
