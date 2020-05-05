package net.arcanamod.util;

import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.io.File;

/**
 * File Utilities for Arcana
 *
 * @author Atlas
 */
public class ArcanaFileUtils{
	
	/**
	 * Gets the directory of a world's file
	 *
	 * @param world
	 * @return
	 */
	public static File getWorldDirectory(World world){
		ServerWorld world1 = (ServerWorld)world;
		return world1.getSaveHandler().getWorldDirectory();
	}
}
