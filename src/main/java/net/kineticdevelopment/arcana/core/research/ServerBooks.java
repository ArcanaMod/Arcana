package net.kineticdevelopment.arcana.core.research;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stores all of the research books known on the server.
 * This is considered to be the "master" copy of available research.
 * (To be) Synced with a player upon login, and synced to all players when research JSONs are loaded.
 */
public class ServerBooks{

	// public
	// thats a bad idea I think
	public static Map<ResourceLocation, ResearchBook> books = new LinkedHashMap<>();
	public static Map<ResourceLocation, Puzzle> puzzles = new LinkedHashMap<>();
	
	public static List<ResearchBook> getBooks(){
		return new ArrayList<>(books.values());
	}
	
	public static Stream<ResearchCategory> streamCategories(){
		return books.values().stream().flatMap(ResearchBook::streamCategories);
	}
	
	public static List<ResearchCategory> getCategories(){
		return streamCategories().collect(Collectors.toList());
	}
	
	public static ResearchCategory getCategory(ResourceLocation key){
		return streamCategories().filter(x -> x.getKey().equals(key)).findFirst().orElse(null);
	}
	
	public static Stream<ResearchEntry> streamEntries(){
		return streamCategories().flatMap(ResearchCategory::streamEntries);
	}
	
	public static List<ResearchEntry> getEntries(){
		return streamEntries().collect(Collectors.toList());
	}
	
	public static ResearchEntry getEntry(ResourceLocation key){
		return streamEntries().filter(x -> x.key().equals(key)).findFirst().orElse(null);
	}
}