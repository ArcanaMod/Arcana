package net.kineticdevelopment.arcana.core.research;

import net.minecraft.util.ResourceLocation;

import java.util.Map;

/**
 * Stores all of the research books known on the server.
 * This is considered to be the "master" copy of available research.
 * (To be) Synced with a player upon login, and synced to all players when research JSONs are loaded.
 */
public class ServerBooks{

	// public
	// thats a bad idea I think
	public static Map<ResourceLocation, ResearchBook> books;
}