package net.arcanamod.research;

import net.arcanamod.util.StreamUtils;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a node in the research tree. Stores an ordered list of entry sections representing its content.
 */
public class ResearchEntry{
	
	private ResourceLocation key;
	private List<EntrySection> sections;
	private List<Item> icons;
	private List<String> meta;
	private List<ResourceLocation> parents;
	private ResearchCategory category;
	
	private String name, desc;
	
	private int x, y;
	
	public ResearchEntry(ResourceLocation key, List<EntrySection> sections, List<Item> icons, List<String> meta, List<ResourceLocation> parents, ResearchCategory category, String name, String desc, int x, int y){
		this.key = key;
		this.sections = sections;
		this.icons = icons;
		this.meta = meta;
		this.parents = parents;
		this.category = category;
		this.name = name;
		this.desc = desc;
		this.x = x;
		this.y = y;
	}
	
	public List<EntrySection> sections(){
		return Collections.unmodifiableList(sections);
	}
	
	public List<Item> icons(){
		return icons;
	}
	
	public List<String> meta(){
		return meta;
	}
	
	public List<ResourceLocation> parents(){
		return parents;
	}
	
	public ResearchCategory category(){
		return category;
	}
	
	public ResourceLocation key(){
		return key;
	}
	
	public String name(){
		return name;
	}
	
	public String description(){
		return desc;
	}
	
	public int x(){
		return x;
	}
	
	public int y(){
		return y;
	}
	
	public CompoundNBT serialize(ResourceLocation tag){
		CompoundNBT nbt = new CompoundNBT();
		// key
		nbt.putString("id", tag.toString());
		// name, desc
		nbt.putString("name", name());
		nbt.putString("desc", description());
		// x, y
		nbt.putInt("x", x());
		nbt.putInt("y", y());
		// sections
		ListNBT list = new ListNBT();
		sections().forEach((section) -> list.add(section.getPassData()));
		nbt.put("sections", list);
		// icons
		ListNBT icons = new ListNBT();
		icons().forEach((icon) -> icons.add(StringNBT.valueOf(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(icon), "Invalid item for icon on client side.").toString())));
		nbt.put("icons", icons);
		// parents
		ListNBT parents = new ListNBT();
		parents().forEach((parent) -> parents.add(StringNBT.valueOf(parent.toString())));
		nbt.put("parents", parents);
		// meta
		ListNBT meta = new ListNBT();
		meta().forEach((met) -> meta.add(StringNBT.valueOf(met)));
		nbt.put("meta", meta);
		return nbt;
	}
	
	public static ResearchEntry deserialize(CompoundNBT nbt, ResearchCategory in){
		ResourceLocation key = new ResourceLocation(nbt.getString("id"));
		String name = nbt.getString("name");
		String desc = nbt.getString("desc");
		int x = nbt.getInt("x");
		int y = nbt.getInt("y");
		List<EntrySection> sections = StreamUtils.streamAndApply(nbt.getList("sections", 10), CompoundNBT.class, EntrySection::deserialze).collect(Collectors.toList());
		List<ResourceLocation> parents = StreamUtils.streamAndApply(nbt.getList("parents", 8), StringNBT.class, StringNBT::getString).map(ResourceLocation::new).collect(Collectors.toList());
		List<Item> icons = StreamUtils.streamAndApply(nbt.getList("icons", 8), StringNBT.class, StringNBT::getString).map(ResourceLocation::new).map(ForgeRegistries.ITEMS::getValue).filter(Objects::nonNull).collect(Collectors.toList());
		List<String> meta = StreamUtils.streamAndApply(nbt.getList("meta", 8), StringNBT.class, StringNBT::getString).collect(Collectors.toList());
		return new ResearchEntry(key, sections, icons, meta, parents, in, name, desc, x, y);
	}
	
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(!(o instanceof ResearchEntry))
			return false;
		ResearchEntry entry = (ResearchEntry)o;
		return key.equals(entry.key);
	}
	
	public int hashCode(){
		return Objects.hash(key);
	}
}