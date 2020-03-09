package net.kineticdevelopment.arcana.core.research;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a whole research book, such as the Arcanomicon or Tainted Codex.
 * Contains a number of research categories, stored by key.
 */
public class ResearchBook{
	
	private Map<ResourceLocation, ResearchCategory> categories;
	private ResourceLocation key;
	
	public ResearchBook(ResourceLocation key, Map<ResourceLocation, ResearchCategory> categories){
		this.categories = categories;
		this.key = key;
	}
	
	public ResearchCategory getCategory(ResourceLocation key){
		return categories.get(key);
	}
	
	public List<ResearchCategory> getCategories(){
		return new ArrayList<>(categories.values());
	}
	
	public Stream<ResearchCategory> streamCategories(){
		return categories.values().stream();
	}
	
	public Stream<ResearchEntry> streamEntries(){
		return streamCategories().flatMap(ResearchCategory::streamEntries);
	}
	
	public List<ResearchEntry> getEntries(){
		return streamEntries().collect(Collectors.toList());
	}
	
	public ResearchEntry getEntry(ResourceLocation key){
		return streamEntries().filter(entry -> entry.key().equals(key)).findFirst().orElse(null);
	}
	
	public ResourceLocation getKey(){
		return key;
	}
}