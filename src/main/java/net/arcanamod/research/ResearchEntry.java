package net.arcanamod.research;

import net.arcanamod.util.StreamUtils;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

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
		nbt.setString("id", tag.toString());
		// name, desc
		nbt.setString("name", name());
		nbt.setString("desc", description());
		// x, y
		nbt.setInteger("x", x());
		nbt.setInteger("y", y());
		// sections
		ListNBT list = new ListNBT();
		sections().forEach((section) -> list.appendTag(section.getPassData()));
		nbt.setTag("sections", list);
		// icons
		ListNBT icons = new ListNBT();
		icons().forEach((icon) -> icons.appendTag(new StringNBT(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(icon), "Invalid item for icon on client side.").toString())));
		nbt.setTag("icons", icons);
		// parents
		ListNBT parents = new ListNBT();
		parents().forEach((parent) -> parents.appendTag(new StringNBT(parent.toString())));
		nbt.setTag("parents", parents);
		// meta
		ListNBT meta = new ListNBT();
		meta().forEach((met) -> meta.appendTag(new StringNBT(met)));
		nbt.setTag("meta", meta);
		return nbt;
	}
	
	public static ResearchEntry deserialize(CompoundNBT nbt, ResearchCategory in){
		ResourceLocation key = new ResourceLocation(nbt.getString("id"));
		String name = nbt.getString("name");
		String desc = nbt.getString("desc");
		int x = nbt.getInteger("x");
		int y = nbt.getInteger("y");
		List<EntrySection> sections = StreamUtils.streamAndApply(nbt.getTagList("sections", 10), CompoundNBT.class, EntrySection::deserialze).collect(Collectors.toList());
		List<ResourceLocation> parents = StreamUtils.streamAndApply(nbt.getTagList("parents", 8), StringNBT.class, StringNBT::getString).map(ResourceLocation::new).collect(Collectors.toList());
		List<Item> icons = StreamUtils.streamAndApply(nbt.getTagList("icons", 8), StringNBT.class, StringNBT::getString).map(ResourceLocation::new).map(ForgeRegistries.ITEMS::getValue).filter(Objects::nonNull).collect(Collectors.toList());
		List<String> meta = StreamUtils.streamAndApply(nbt.getTagList("meta", 8), StringNBT.class, StringNBT::getString).collect(Collectors.toList());
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