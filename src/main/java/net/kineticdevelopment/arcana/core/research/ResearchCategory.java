package net.kineticdevelopment.arcana.core.research;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Represents a research tab. Contains a number of research entries, stored by key.
 */
public class ResearchCategory{
	
	protected Map<ResourceLocation, ResearchEntry> entries;
	private ResourceLocation key, icon, bg;
	private ResearchBook in;
	private String name;
	
	protected int serializationIndex = 0;
	
	public ResearchCategory(Map<ResourceLocation, ResearchEntry> entries, ResourceLocation key, ResourceLocation icon, ResourceLocation bg, String name, ResearchBook in){
		this.entries = entries;
		this.key = key;
		this.in = in;
		this.icon = icon;
		this.name = name;
		this.bg = bg;
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
	
	public ResourceLocation getBg(){
		return bg;
	}
	
	int getSerializationIndex(){
		return serializationIndex;
	}
	
	public NBTTagCompound serialize(ResourceLocation tag, int index){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("id", tag.toString());
		nbt.setString("icon", icon.toString());
		nbt.setString("bg", bg.toString());
		nbt.setString("name", name);
		nbt.setInteger("index", index);
		NBTTagList list = new NBTTagList();
		entries.forEach((location, entry) -> list.appendTag(entry.serialize(location)));
		nbt.setTag("entries", list);
		return nbt;
	}
	
	public static ResearchCategory deserialize(NBTTagCompound nbt, ResearchBook in){
		ResourceLocation key = new ResourceLocation(nbt.getString("id"));
		ResourceLocation icon = new ResourceLocation(nbt.getString("icon"));
		ResourceLocation bg = new ResourceLocation(nbt.getString("bg"));
		String name = nbt.getString("name");
		NBTTagList entriesList = nbt.getTagList("entries", 10);
		// same story as ResearchBook
		Map<ResourceLocation, ResearchEntry> c = new LinkedHashMap<>();
		ResearchCategory category = new ResearchCategory(c, key, icon, bg, name, in);
		category.serializationIndex = nbt.getInteger("index");
		
		Map<ResourceLocation, ResearchEntry> entries = StreamSupport.stream(entriesList.spliterator(), false)
				.map(NBTTagCompound.class::cast)
				.map((NBTTagCompound nbt1) -> ResearchEntry.deserialize(nbt1, category))
				.collect(Collectors.toMap(ResearchEntry::key, Function.identity(), (a, b) -> a));
		
		c.putAll(entries);
		return category;
	}
	
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(!(o instanceof ResearchCategory))
			return false;
		ResearchCategory category = (ResearchCategory)o;
		return getKey().equals(category.getKey());
	}
	
	public int hashCode(){
		return Objects.hash(getKey());
	}
}