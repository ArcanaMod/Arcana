package net.arcanamod.research;

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
	private ResourceLocation key, icon, bg, requirement;
	private ResearchBook in;
	private String name;
	
	protected int serializationIndex = 0;
	
	public ResearchCategory(Map<ResourceLocation, ResearchEntry> entries, ResourceLocation key, ResourceLocation icon, ResourceLocation bg, ResourceLocation requirement, String name, ResearchBook in){
		this.entries = entries;
		this.key = key;
		this.requirement = requirement;
		this.in = in;
		this.icon = icon;
		this.name = name;
		this.bg = bg;
	}
	
	public ResourceLocation key(){
		return key;
	}
	
	public ResearchEntry entry(ResearchEntry entry){
		return entries.get(entry.key());
	}
	
	public List<ResearchEntry> entries(){
		return new ArrayList<>(entries.values());
	}
	
	public Stream<ResearchEntry> streamEntries(){
		return entries.values().stream();
	}
	
	public ResearchBook book(){
		return in;
	}
	
	public ResourceLocation icon(){
		return icon;
	}
	
	public String name(){
		return name;
	}
	
	public ResourceLocation bg(){
		return bg;
	}
	
	int serializationIndex(){
		return serializationIndex;
	}
	
	public ResourceLocation requirement(){
		return requirement;
	}
	
	public NBTTagCompound serialize(ResourceLocation tag, int index){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("id", tag.toString());
		nbt.setString("icon", icon.toString());
		nbt.setString("bg", bg.toString());
		nbt.setString("requirement", requirement != null ? requirement.toString() : "null");
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
		ResourceLocation requirement = nbt.getString("requirement").equals("null") ? null : new ResourceLocation(nbt.getString("requirement"));
		String name = nbt.getString("name");
		NBTTagList entriesList = nbt.getTagList("entries", 10);
		// same story as ResearchBook
		Map<ResourceLocation, ResearchEntry> c = new LinkedHashMap<>();
		ResearchCategory category = new ResearchCategory(c, key, icon, bg, requirement, name, in);
		category.serializationIndex = nbt.getInteger("index");
		
		Map<ResourceLocation, ResearchEntry> entries = StreamSupport.stream(entriesList.spliterator(), false).map(NBTTagCompound.class::cast).map((NBTTagCompound nbt1) -> ResearchEntry.deserialize(nbt1, category)).collect(Collectors.toMap(ResearchEntry::key, Function.identity(), (a, b) -> a));
		
		c.putAll(entries);
		return category;
	}
	
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(!(o instanceof ResearchCategory))
			return false;
		ResearchCategory category = (ResearchCategory)o;
		return key().equals(category.key());
	}
	
	public int hashCode(){
		return Objects.hash(key());
	}
}