package net.kineticdevelopment.arcana.core.research;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Represents a node in the research tree. Provides an ordered list of entry sections representing its content.
 */
public interface ResearchEntry{
	
	ResourceLocation key();
	List<EntrySection> sections();
	List<Item> icons();
	List<String> meta();
	List<ResearchEntry> parents();
	ResearchCategory category();
	
	String name();
	String description();
	
	int x();
	int y();
}