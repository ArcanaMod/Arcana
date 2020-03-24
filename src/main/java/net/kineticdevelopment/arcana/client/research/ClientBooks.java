package net.kineticdevelopment.arcana.client.research;

import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.kineticdevelopment.arcana.core.research.ResearchBook;
import net.kineticdevelopment.arcana.core.research.ResearchCategory;
import net.kineticdevelopment.arcana.core.research.ResearchEntry;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stores all research books known on a client. Is provided data from {@link net.kineticdevelopment.arcana.core.research.ServerBooks}.
 */
public class ClientBooks{
	
	// public
	// thats still a bad idea
	public static Map<ResourceLocation, ResearchBook> books;
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