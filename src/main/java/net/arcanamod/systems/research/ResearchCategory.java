package net.arcanamod.systems.research;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a research tab. Contains a number of research entries, stored by key.
 */
public class ResearchCategory{
	
	protected Map<ResourceLocation, ResearchEntry> entries;
	private ResourceLocation key, icon, bg, requirement;
	private ResearchBook in;
	private String name;
	private List<BackgroundLayer> bgs = new ArrayList<>();
	
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
	
	public List<BackgroundLayer> getBgs(){
		return bgs;
	}
	
	int serializationIndex(){
		return serializationIndex;
	}
	
	public ResourceLocation requirement(){
		return requirement;
	}
	
	public CompoundNBT serialize(ResourceLocation tag, int index){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("id", tag.toString());
		nbt.putString("icon", icon.toString());
		nbt.putString("bg", bg.toString());
		nbt.putString("requirement", requirement != null ? requirement.toString() : "null");
		nbt.putString("name", name);
		nbt.putInt("index", index);
		
		ListNBT list = new ListNBT();
		entries.forEach((location, entry) -> list.add(entry.serialize(location)));
		nbt.put("entries", list);
		
		ListNBT bgsList = new ListNBT();
		bgs.forEach(layer -> bgsList.add(layer.getPassData()));
		nbt.put("bgs", bgsList);
		return nbt;
	}
	
	public static ResearchCategory deserialize(CompoundNBT nbt, ResearchBook in){
		ResourceLocation key = new ResourceLocation(nbt.getString("id"));
		ResourceLocation icon = new ResourceLocation(nbt.getString("icon"));
		ResourceLocation bg = new ResourceLocation(nbt.getString("bg"));
		ResourceLocation requirement = nbt.getString("requirement").equals("null") ? null : new ResourceLocation(nbt.getString("requirement"));
		String name = nbt.getString("name");
		ListNBT entriesList = nbt.getList("entries", 10);
		// same story as ResearchBook
		Map<ResourceLocation, ResearchEntry> c = new LinkedHashMap<>();
		ResearchCategory category = new ResearchCategory(c, key, icon, bg, requirement, name, in);
		category.serializationIndex = nbt.getInt("index");
		
		Map<ResourceLocation, ResearchEntry> entries = entriesList.stream().map(CompoundNBT.class::cast).map((CompoundNBT nbt1) -> ResearchEntry.deserialize(nbt1, category)).collect(Collectors.toMap(ResearchEntry::key, Function.identity(), (a, b) -> a));
		c.putAll(entries);
		
		category.bgs = nbt.getList("bgs", Constants.NBT.TAG_COMPOUND).stream().map(CompoundNBT.class::cast).map(BackgroundLayer::deserialize).collect(Collectors.toList());
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