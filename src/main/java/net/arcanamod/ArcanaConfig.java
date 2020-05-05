package net.arcanamod;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

/**
 * Configuration
 *
 * @author Mozaran
 */
@Config(modid = Arcana.MODID)
public class ArcanaConfig{
	
	@Comment(value = "Enable ore retrogen")
	public static boolean ORE_RETROGEN = true;
	
	@Comment(value = "Enable node retrogen")
	public static boolean NODE_RETROGEN = false;
	
	@Comment(value = "Enable verbose logging for retrogen")
	public static boolean VERBOSE = false;
	
	@Comment(value = "Generate ore in the overworld")
	public static boolean GENERATE_OVERWORLD = true;
	
	@Comment(value = "Minimum size of every amber ore vein")
	public static int AMBER_MIN_VEIN_SIZE = 4;
	
	@Comment(value = "Maximum size of every amber ore vein")
	public static int AMBER_MAX_VEIN_SIZE = 8;
	
	@Comment(value = "Maximum amber veins per chunk")
	public static int AMBER_CHANCES_TO_SPAWN = 3;
	
	@Comment(value = "Minimum height for the amber ore")
	public static int AMBER_MIN_Y = 2;
	
	@Comment(value = "Maximum height for the amber ore")
	public static int AMBER_MAX_Y = 50;
	
	@Comment(value = "Probability of a node existing in a chunk (out of 1000)")
	public static int NODE_CHANCE = 1;
	
	@Comment(value = "Probability of a node being a special (not normal) node (out of 100)")
	public static int SPECIAL_NODE_CHANCE = 25;
}
