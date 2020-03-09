package net.kineticdevelopment.arcana.client.research;

import net.kineticdevelopment.arcana.core.research.ResearchBook;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

/**
 * Stores all research books known on a client. Is provided data from {@link net.kineticdevelopment.arcana.core.research.ServerBooks}.
 */
public class ClientBooks{
	
	// public
	// thats still a bad idea
	public static Map<ResourceLocation, ResearchBook> books;
}