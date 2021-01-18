package net.arcanamod.systems.research;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.arcanamod.util.StreamUtils.streamAndApply;

/**
 * Represents a node in the research tree. Stores an ordered list of entry sections representing its content.
 */
public class ResearchEntry{
	
	private ResourceLocation key;
	private List<EntrySection> sections;
	private List<String> meta;
	private List<Parent> parents;
	private List<Icon> icons;
	private ResearchCategory category;
	
	private String name, desc;
	
	private int x, y;
	
	public ResearchEntry(ResourceLocation key, List<EntrySection> sections, List<Icon> icons, List<String> meta, List<Parent> parents, ResearchCategory category, String name, String desc, int x, int y){
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
	
	public List<Icon> icons(){
		return icons;
	}
	
	public List<String> meta(){
		return meta;
	}
	
	public List<Parent> parents(){
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
		icons().forEach((icon) -> icons.add(StringNBT.valueOf(icon.toString())));
		nbt.put("icons", icons);
		// parents
		ListNBT parents = new ListNBT();
		parents().forEach((parent) -> parents.add(StringNBT.valueOf(parent.asString())));
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
		List<EntrySection> sections = streamAndApply(nbt.getList("sections", 10), CompoundNBT.class, EntrySection::deserialze).collect(Collectors.toList());
		List<Parent> betterParents = streamAndApply(nbt.getList("parents", 8), StringNBT.class, StringNBT::getString).map(Parent::parse).collect(Collectors.toList());
		List<Icon> icons = streamAndApply(nbt.getList("icons", 8), StringNBT.class, StringNBT::getString).map(Icon::fromString).collect(Collectors.toList());
		List<String> meta = streamAndApply(nbt.getList("meta", 8), StringNBT.class, StringNBT::getString).collect(Collectors.toList());
		return new ResearchEntry(key, sections, icons, meta, betterParents, in, name, desc, x, y);
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
	
	/**
	 * Returns a stream containing all of the pins of contained sections.
	 *
	 * @param world
	 * 		The world the player is in.
	 * @return A stream containing the pins of contained sections.
	 */
	public Stream<Pin> getAllPins(World world){
		return sections().stream().flatMap(section -> section.getPins(sections.indexOf(section), world, this));
	}
}