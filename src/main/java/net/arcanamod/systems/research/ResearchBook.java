package net.arcanamod.systems.research;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toMap;

/**
 * Represents a whole research book, such as the Arcanum or Tainted Codex.
 * Contains a number of research categories, stored by key.
 */
public class ResearchBook{
	
	protected Map<ResourceLocation, ResearchCategory> categories;
	private ResourceLocation key;
	private String prefix;
	
	public ResearchBook(ResourceLocation key, Map<ResourceLocation, ResearchCategory> categories, String prefix){
		this.categories = categories;
		this.key = key;
		this.prefix = prefix;
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
	
	public Map<ResourceLocation, ResearchCategory> getCategoriesMap(){
		return Collections.unmodifiableMap(categories);
	}
	
	public String getPrefix(){
		return prefix;
	}
	
	public CompoundNBT serialize(ResourceLocation tag){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("id", tag.toString());
		nbt.putString("prefix", prefix);
		ListNBT list = new ListNBT();
		int index = 0;
		for(Map.Entry<ResourceLocation, ResearchCategory> entry : categories.entrySet()){
			// enforce a specific order so things are transferred correctly
			list.add(entry.getValue().serialize(entry.getKey(), index));
			index++;
		}
		nbt.put("categories", list);
		return nbt;
	}
	
	public static ResearchBook deserialize(CompoundNBT nbt){
		ResourceLocation key = new ResourceLocation(nbt.getString("id"));
		String prefix = nbt.getString("prefix");
		ListNBT categoryList = nbt.getList("categories", 10);
		// need to have a book to put them *in*
		// book isn't in ClientBooks until all categories have been deserialized, so this is needed
		Map<ResourceLocation, ResearchCategory> c = new LinkedHashMap<>();
		ResearchBook book = new ResearchBook(key, c, prefix);
		
		Map<ResourceLocation, ResearchCategory> categories = StreamSupport.stream(categoryList.spliterator(), false).map(CompoundNBT.class::cast).map(nbt1 -> ResearchCategory.deserialize(nbt1, book)).sorted(Comparator.comparingInt(ResearchCategory::serializationIndex)).collect(toMap(ResearchCategory::key, Function.identity(), (a, b) -> a, LinkedHashMap::new));
		
		// this could be replaced by adding c to ClientBooks before deserializing, but this wouldn't look any different
		// and would leave a broken book in if an exception occurs.
		c.putAll(categories);
		return book;
	}
	
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(!(o instanceof ResearchBook))
			return false;
		ResearchBook book = (ResearchBook)o;
		return getKey().equals(book.getKey());
	}
	
	public int hashCode(){
		return Objects.hash(getKey());
	}
}