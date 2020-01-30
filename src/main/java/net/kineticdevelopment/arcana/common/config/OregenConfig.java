package net.kineticdevelopment.arcana.common.config;

import net.kineticdevelopment.arcana.core.Main;
import net.minecraftforge.common.config.Config;

/**
 * OreGen Configuration
 * @author Mozaran
 */
@Config(modid = Main.MODID, category = "oregen")
public class OregenConfig {

    @Config.Comment(value = "Enable retrogen")
    public static boolean RETROGEN = true;

    @Config.Comment(value = "Enable verbose logging for retrogen")
    public static boolean VERBOSE = false;

    @Config.Comment(value = "Generate ore in the overworld")
    public static boolean GENERATE_OVERWORLD = true;

    @Config.Comment(value = "Minimum size of every amber ore vein")
    public static int AMBER_MIN_VEIN_SIZE = 4;

    @Config.Comment(value = "Maximum size of every amber ore vein")
    public static int AMBER_MAX_VEIN_SIZE = 8;

    @Config.Comment(value = "Maximum amber veins per chunk")
    public static int AMBER_CHANCES_TO_SPAWN = 3;

    @Config.Comment(value = "Minimum height for the amber ore")
    public static int AMBER_MIN_Y = 2;

    @Config.Comment(value = "Maximum height for the amber ore")
    public static int AMBER_MAX_Y = 50;
}
