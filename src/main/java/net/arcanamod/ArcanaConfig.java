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

		SPAWN_WITH_NOTES = COMMON_BUILDER
				.comment("If the player should spawn with scribbled notes when first joining the world.", "True by default.")
				.define("SpawnWithNotes", true);
		ORE_RETROGEN = COMMON_BUILDER
				.comment("Enables Arcana ore generation in already-generated chunks.", "Chunks generated with Arcana will stay unaffected.", "False by default.")
				.define("OreRetrogen", false);
		NODE_RETROGEN = COMMON_BUILDER
				.comment("Enables node generation in already-generated chunks.", "Chunks generated with Arcana will stay unaffected.", "False by default.")
				.define("NodeRetrogen", false);
		VERBOSE = COMMON_BUILDER
				.comment("Enables verbose logging during retrogen.", "For debug purposes; false by default.")
				.define("VerboseRetrogen", false);
		GENERATE_OVERWORLD = COMMON_BUILDER
				.comment("Enables ore generation in the overworld.", "If disabled, Arcana's ores will not generate.", "True by default.")
				.define("OverworldGeneration", true);

		AMBER_MIN_VEIN_SIZE = COMMON_BUILDER
				.comment("The minimum size of amber ore veins.", "(Currently unused. 4 by default.)")
				.define("MinAmberSize", 4);
		AMBER_MAX_VEIN_SIZE = COMMON_BUILDER
				.comment("The maximum size of amber ore veins.", "8 by default.")
				.define("MaxAmberSize", 8);
		AMBER_CHANCES_TO_SPAWN = COMMON_BUILDER
				.comment("The maximum number of amber ore veins per chunk.", "3 by default.")
				.define("AmberSpawnChance", 3);
		AMBER_MIN_Y = COMMON_BUILDER
				.comment("The minimum Y level that amber ore will generate at.", "2 by default")
				.define("MinAmberY", 2);
		AMBER_MAX_Y = COMMON_BUILDER
				.comment("The maximum Y level that amber ore will generate at.", "50 by default.")
				.define("MaxAmberY", 50);

		SILVER_MIN_VEIN_SIZE = COMMON_BUILDER
				.comment("The minimum size of amber ore veins.", "(Currently unused. 3 by default.)")
				.define("MinAmberSize", 3);
		SILVER_MAX_VEIN_SIZE = COMMON_BUILDER
				.comment("The maximum size of amber ore veins.", "6 by default.")
				.define("MaxAmberSize", 6);
		SILVER_CHANCES_TO_SPAWN = COMMON_BUILDER
				.comment("The maximum number of amber ore veins per chunk.", "3 by default.")
				.define("AmberSpawnChance", 3);
		SILVER_MIN_Y = COMMON_BUILDER
				.comment("The minimum Y level that amber ore will generate at.", "1 by default")
				.define("MinAmberY", 1);
		SILVER_MAX_Y = COMMON_BUILDER
				.comment("The maximum Y level that amber ore will generate at.", "30 by default.")
				.define("MaxAmberY", 30);

		CINNABAR_MIN_VEIN_SIZE = COMMON_BUILDER
				.comment("The minimum size of amber ore veins.", "(Currently unused. 1 by default.)")
				.define("MinAmberSize", 1);
		CINNABAR_MAX_VEIN_SIZE = COMMON_BUILDER
				.comment("The maximum size of amber ore veins.", "3 by default.")
				.define("MaxAmberSize", 3);
		CINNABAR_CHANCES_TO_SPAWN = COMMON_BUILDER
				.comment("The maximum number of amber ore veins per chunk.", "4 by default.")
				.define("AmberSpawnChance", 4);
		CINNABAR_MIN_Y = COMMON_BUILDER
				.comment("The minimum Y level that amber ore will generate at.", "40 by default")
				.define("MinAmberY", 40);
		CINNABAR_MAX_Y = COMMON_BUILDER
				.comment("The maximum Y level that amber ore will generate at.", "80 by default.")
				.define("MaxAmberY", 80);

		NODE_CHANCE = COMMON_BUILDER
				.comment("The chance of an aura node generating in a chunk, out of 100.", "1 by default.")
				.define("NodeChance", 1);
		SPECIAL_NODE_CHANCE = COMMON_BUILDER
				.comment("The chance of an aura node being a special (hungry, eldritch, bright, or pale) node, out of 100.", "15 by default.")
				.define("SpecialNodeChance", 15);
		MAX_ALEMBIC_ASPECT_DISTILL = COMMON_BUILDER
				.comment("The maximum amount of aspects a single alembic can distill per tick.", "2 by default.")
				.define("MaxAlembicAspectDistill", 2);
		MAX_ALEMBIC_ASPECT_OUT = COMMON_BUILDER
				.comment("The maximum amount of aspects a single alembic can output to pipes per tick.", "3 by default.")
				.define("MaxAlembicAspectOut", 3);
		MAX_ALEMBIC_AIR = COMMON_BUILDER
				.comment("The maximum amount of air blocks between an alembic and a crucible.", "4 by default.")
				.define("MaxAlembicAir", 4);
		MAX_ALEMBIC_STACK = COMMON_BUILDER
				.comment("The maximum amount of alembics that can be stacked.", "3 by default.")
				.define("MaxAlembicStack", 3);
		ALCHEMY_ASPECT_CARRY_FRACTION = COMMON_BUILDER
				.comment("The fraction of aspects used in an alchemy recipe that should be assigned to the result through recipes.", "Setting this to 0.5, the default, for example, will cause an item crafted with 10 aer in alchemy to be given 5 aer from that recipe.")
				.define("AlchemyAspectCarryFraction", .5);
		TAINT_SPAWN_THRESHOLD = COMMON_BUILDER
				.comment("The amount of flux in a chunk that is required to spawn a taint block. Does not affect taint spreading.", "60 by default.")
				.define("TaintSpawnThreshold", 60);
		TAINT_SPAWN_COST = COMMON_BUILDER
				.comment("The amount of flux consumed spawning an initial taint block. Does not affect taint spreading.", "40 by default.")
				.define("TaintSpawnCost", 40);
		TAINT_EFFECT_TIME = COMMON_BUILDER
				.comment("The time, in ticks, an entity needs to spend in a tainted area to get the tainted status effect.", "80 by default.")
				.define("TaintEffectTime", 80);
		COMMON_BUILDER.pop();
		
		CLIENT_BUILDER.push("Client");
		CUSTOM_BOOK_WIDTH = CLIENT_BUILDER
				.comment("The width that the research book GUI should use.", "Setting it to -1, the default, makes the GUI adjust to screen width automatically.")
				.define("ResearchBookWidth", -1);
		CUSTOM_BOOK_HEIGHT = CLIENT_BUILDER
				.comment("The height that the research book GUI should use.", "Setting it to -1, the default, makes the GUI adjust to screen height automatically.")
				.define("ResearchBookHeight", -1);
		BOOK_TEXT_SCALING = CLIENT_BUILDER
				.comment("Text scaling for the research entry GUI.", ".07 by default")
				.define("ResearchBookTextScaling", 0.7);
		WAND_HUD_LEFT = CLIENT_BUILDER
				.comment("Whether the wand HUD should display on the left of the screen (true) or the right (false).", "True by default.")
				.define("WandHudHorizontalSide", true);
		WAND_HUD_TOP = CLIENT_BUILDER
				.comment("Whether the wand HUD should display on the top of the screen (true) or the bottom (false).", "True by default.")
				.define("WandHudVerticalSide", true);
		BLOCK_HUDS_TOP = CLIENT_BUILDER
				.comment("Whether block HUDS that display in the middle of the screen should display above the crosshair (true) or below (false).", "True by default.")
				.define("BlockHudsTop", true);
		JAR_ANIMATION_SPEED = CLIENT_BUILDER
				.comment("The speed of the warded jar filling animation, in vis per tick.", "1.0 by default.")
				.define("JarFillAnimationSpeed", 1.0);
		NO_JAR_ANIMATION = CLIENT_BUILDER
				.comment("Whether the warded jar's filling animation should be disabled.", "False by default.")
				.define("NoJarAnimation", false);
		CLIENT_BUILDER.pop();
	}
	
	public static final ForgeConfigSpec COMMON_SPEC = COMMON_BUILDER.build();
	public static final ForgeConfigSpec CLIENT_SPEC = CLIENT_BUILDER.build();
	
	// Common
	public static ConfigValue<Boolean> SPAWN_WITH_NOTES; // true
	public static ConfigValue<Boolean> ORE_RETROGEN; // false
	public static ConfigValue<Boolean> NODE_RETROGEN; // false
	public static ConfigValue<Boolean> VERBOSE; // false
	public static ConfigValue<Boolean> GENERATE_OVERWORLD; // true

	public static ConfigValue<Integer> AMBER_MIN_VEIN_SIZE; // 4
	public static ConfigValue<Integer> AMBER_MAX_VEIN_SIZE; // 8
	public static ConfigValue<Integer> AMBER_CHANCES_TO_SPAWN; // 3
	public static ConfigValue<Integer> AMBER_MIN_Y; // 2
	public static ConfigValue<Integer> AMBER_MAX_Y; // 50

	public static ConfigValue<Integer> SILVER_MIN_VEIN_SIZE; // 3
	public static ConfigValue<Integer> SILVER_MAX_VEIN_SIZE; // 6
	public static ConfigValue<Integer> SILVER_CHANCES_TO_SPAWN; // 3
	public static ConfigValue<Integer> SILVER_MIN_Y; // 1
	public static ConfigValue<Integer> SILVER_MAX_Y; // 30

	public static ConfigValue<Integer> CINNABAR_MIN_VEIN_SIZE; // 1
	public static ConfigValue<Integer> CINNABAR_MAX_VEIN_SIZE; // 3
	public static ConfigValue<Integer> CINNABAR_CHANCES_TO_SPAWN; // 4
	public static ConfigValue<Integer> CINNABAR_MIN_Y; // 40
	public static ConfigValue<Integer> CINNABAR_MAX_Y; // 80

	public static ConfigValue<Integer> NODE_CHANCE; // 1
	public static ConfigValue<Integer> SPECIAL_NODE_CHANCE; // 15
	public static ConfigValue<Integer> MAX_ALEMBIC_ASPECT_DISTILL; // 2
	public static ConfigValue<Integer> MAX_ALEMBIC_ASPECT_OUT; // 3
	public static ConfigValue<Integer> MAX_ALEMBIC_AIR; // 4
	public static ConfigValue<Integer> MAX_ALEMBIC_STACK; // 3
	public static ConfigValue<Double> ALCHEMY_ASPECT_CARRY_FRACTION; // .5
	public static ConfigValue<Integer> TAINT_SPAWN_THRESHOLD; // 60
	public static ConfigValue<Integer> TAINT_SPAWN_COST; // 40
	public static ConfigValue<Integer> TAINT_EFFECT_TIME; // 80

	
	// Client
	public static ConfigValue<Integer> CUSTOM_BOOK_WIDTH; // -1
	public static ConfigValue<Integer> CUSTOM_BOOK_HEIGHT; // -1
	public static ConfigValue<Double> BOOK_TEXT_SCALING; // 0.8
	public static ConfigValue<Boolean> WAND_HUD_LEFT; // true
	public static ConfigValue<Boolean> WAND_HUD_TOP; // true
	public static ConfigValue<Boolean> BLOCK_HUDS_TOP; // true
	public static ConfigValue<Double> JAR_ANIMATION_SPEED; // 1
	public static ConfigValue<Boolean> NO_JAR_ANIMATION; // false
}