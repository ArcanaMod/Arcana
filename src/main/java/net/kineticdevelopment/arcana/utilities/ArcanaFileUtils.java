package net.kineticdevelopment.arcana.utilities;

import java.io.File;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * File Utilities for Arcana
 * 
 * @author Atlas
 */
public class ArcanaFileUtils {
	
	/**
	 * Gets the directory of a world's file
	 * @param world
	 * @return
	 */
	public static File getWorldDirectory(World world) {

		WorldServer world1 = (WorldServer) world;

		File dir = world1.getSaveHandler().getWorldDirectory();

		return dir;
	}
}
