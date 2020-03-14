package net.kineticdevelopment.arcana.core.research;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Represents a research tab. Contains a number of research entries, stored by key.
 */
public class ResearchCategory{
	
	protected Map<ResourceLocation, ResearchEntry> entries;
	private ResourceLocation key, icon;
	private ResearchBook in;
	private String name;
	
	public ResearchCategory(Map<ResourceLocation, ResearchEntry> entries, ResourceLocation key, ResourceLocation icon, String name, ResearchBook in){
		this.entries = entries;
		this.key = key;
		this.in = in;
		this.icon = icon;
		this.name = name;
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
	
	public ResourceLocation getIcon(){
		return icon;
	}
	
	public String getName(){
		return name;
	}
	
	public NBTTagCompound serialize(ResourceLocation tag){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("id", tag.toString());
		nbt.setString("icon", icon.toString());
		nbt.setString("name", name);
		NBTTagList list = new NBTTagList();
		entries.forEach((location, entry) -> list.appendTag(entry.serialize(location)));
		nbt.setTag("entries", list);
		return nbt;
	}
	
	public static ResearchCategory deserialize(NBTTagCompound nbt, ResearchBook in){
		ResourceLocation key = new ResourceLocation(nbt.getString("id"));
		ResourceLocation icon = new ResourceLocation(nbt.getString("icon"));
		String name = nbt.getString("name");
		NBTTagList entriesList = nbt.getTagList("entries", 10);
		// same story as ResearchBook
		Map<ResourceLocation, ResearchEntry> c = new LinkedHashMap<>();
		ResearchCategory category = new ResearchCategory(c, key, icon, name, in);
		
		Map<ResourceLocation, ResearchEntry> entries = StreamSupport.stream(entriesList.spliterator(), false)
				.map(NBTTagCompound.class::cast)
				.map((NBTTagCompound nbt1) -> ResearchEntry.deserialize(nbt1, category))
				.collect(Collectors.toMap(ResearchEntry::key, Function.identity(), (a, b) -> a));
		
		c.putAll(entries);
		return category;
	}
}