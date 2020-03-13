package net.kineticdevelopment.arcana.core.research;

import net.kineticdevelopment.arcana.core.research.impls.ResearchEntryImpl;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Represents a node in the research tree. Provides an ordered list of entry sections representing its content.
 */
public interface ResearchEntry{
	
	ResourceLocation key();
	List<EntrySection> sections();
	List<Item> icons();
	List<String> meta();
	List<ResourceLocation> parents();
	ResearchCategory category();
	
	String name();
	String description();
	
	int x();
	int y();
	
	default NBTTagCompound serialize(ResourceLocation tag){
		NBTTagCompound nbt = new NBTTagCompound();
		// key
		nbt.setString("id", tag.toString());
		// name, desc
		nbt.setString("name", name());
		nbt.setString("desc", description());
		// x, y
		nbt.setInteger("x", x());
		nbt.setInteger("y", y());
		// sections
		NBTTagList list = new NBTTagList();
		sections().forEach((section) -> list.appendTag(section.getPassData()));
		nbt.setTag("sections", list);
		// icons
		NBTTagList icons = new NBTTagList();
		icons().forEach((icon) -> list.appendTag(new NBTTagString(ForgeRegistries.ITEMS.getKey(icon).toString())));
		nbt.setTag("icons", icons);
		// parents
		NBTTagList parents = new NBTTagList();
		parents().forEach((parent) -> list.appendTag(new NBTTagString(parent.toString())));
		nbt.setTag("parents", parents);
		// meta
		NBTTagList meta = new NBTTagList();
		meta().forEach((met) -> list.appendTag(new NBTTagString(met)));
		nbt.setTag("meta", meta);
		return nbt;
	}
	
	static ResearchEntry deserialize(NBTTagCompound nbt, ResearchCategory in){
		ResourceLocation key = new ResourceLocation(nbt.getString("id"));
		String name = nbt.getString("name");
		String desc = nbt.getString("desc");
		int x = nbt.getInteger("x");
		int y = nbt.getInteger("y");
		List<EntrySection> sections = streamAndApply(nbt.getTagList("sections", 10), NBTTagCompound.class, EntrySection::deserialze)
				.collect(Collectors.toList());
		List<ResourceLocation> parents = streamAndApply(nbt.getTagList("parents", 8), NBTTagString.class, NBTTagString::getString)
				.map(ResourceLocation::new)
				.collect(Collectors.toList());
		List<Item> icons = streamAndApply(nbt.getTagList("icons", 8), NBTTagString.class, NBTTagString::getString)
				.map(ResourceLocation::new)
				.map(ForgeRegistries.ITEMS::getValue)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		List<String> meta = streamAndApply(nbt.getTagList("meta", 8), NBTTagString.class, NBTTagString::getString)
				.collect(Collectors.toList());
		return new ResearchEntryImpl(key, sections, icons, meta, parents, in, name, desc, x, y);
	}
	
	static <X extends NBTBase, Z> Stream<Z> streamAndApply(NBTTagList list, Class<X> filterType, Function<X, Z> applicator){
		return StreamSupport.stream(list.spliterator(), false)
				.filter(filterType::isInstance)
				.map(filterType::cast)
				.map(applicator);
	}
}