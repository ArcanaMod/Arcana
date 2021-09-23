package net.arcanamod.systems.research;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stores all of the research books.
 */
public class ResearchBooks{
	
	// public
	// thats a bad idea I think
	public static ArrayList<ResourceLocation> disabled = new ArrayList<>();
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
		return streamCategories().filter(x -> x.key().equals(key)).findFirst().orElse(null);
	}
	
	public static Stream<ResearchEntry> streamEntries(){
		return streamCategories().flatMap(ResearchCategory::streamEntries);
	}
	
	public static List<ResearchEntry> getEntries(){
		return streamEntries().collect(Collectors.toList());
	}
	
	public static Stream<ResearchEntry> streamChildrenOf(ResearchEntry parent){
		return streamEntries().filter(x -> x.parents().stream().anyMatch(it -> it.entry.equals(parent.key())));
	}
	
	public static List<ResearchEntry> getChildrenOf(ResearchEntry parent){
		return streamChildrenOf(parent).collect(Collectors.toList());
	}
	
	public static ResearchEntry getEntry(ResourceLocation key){
		return streamEntries().filter(x -> x.key().equals(key)).findFirst().orElse(null);
	}

	static {
		//disabled.add(Arcana.arcLoc("illustrious_grimoire"));
		//disabled.add(Arcana.arcLoc("tainted_codex"));
		//disabled.add(Arcana.arcLoc("crimson_rites"));
	}
}