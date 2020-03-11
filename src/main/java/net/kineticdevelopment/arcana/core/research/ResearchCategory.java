package net.kineticdevelopment.arcana.core.research;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Represents a research tab. Contains a number of research entries, stored by key.
 */
public class ResearchCategory{
	
	protected Map<ResourceLocation, ResearchEntry> entries;
	private ResourceLocation key;
	private ResearchBook in;
	
	public ResearchCategory(Map<ResourceLocation, ResearchEntry> entries, ResourceLocation key, ResearchBook in){
		this.entries = entries;
		this.key = key;
		this.in = in;
	}
	
	public ResourceLocation getKey(){
		return key;
	}
	
	public ResearchEntry getEntry(ResearchEntry entry){
		return entries.get(entry.key());
	}
	
	public List<ResearchEntry> getEntries(){
		return new ArrayList<>(entries.values());
	}
	
	public Stream<ResearchEntry> streamEntries(){
		return entries.values().stream();
	}
	
	public ResearchBook getBook(){
		return in;
	}
}