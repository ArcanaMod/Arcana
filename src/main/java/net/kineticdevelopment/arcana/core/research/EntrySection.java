package net.kineticdevelopment.arcana.core.research;

import net.kineticdevelopment.arcana.core.research.impls.GuessworkSection;
import net.kineticdevelopment.arcana.core.research.impls.RecipeSection;
import net.kineticdevelopment.arcana.core.research.impls.StringSection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.kineticdevelopment.arcana.utilities.StreamUtils.streamAndApply;

/**
 * Represents one section of content - for example, continuous text, an image, or an inline recipe.
 */
public abstract class EntrySection{
	
	// static stuff
	
	// when addon support is to be added: change this from strings to ResourceLocations so mods can register more
	private static Map<String, Function<String, EntrySection>> factories = new LinkedHashMap<>();
	private static Map<String, Function<NBTTagCompound, EntrySection>> deserializers = new LinkedHashMap<>();
	
	public static Function<String, EntrySection> getFactory(String type){
		return factories.get(type);
	}
	
	public static EntrySection makeSection(String type, String content){
		if(getFactory(type) != null)
			return getFactory(type).apply(content);
		else
			return null;
	}
	
	public static EntrySection deserialze(NBTTagCompound passData){
		String type = passData.getString("type");
		NBTTagCompound data = passData.getCompoundTag("data");
		List<Requirement> requirements = streamAndApply(passData.getTagList("requirements", 10), NBTTagCompound.class, Requirement::deserialze)
				.collect(Collectors.toList());
		if(deserializers.get(type) != null){
			EntrySection section = deserializers.get(type).apply(data);
			requirements.forEach(section::addRequirement);
			// recieving on client
			section.entry = new ResourceLocation(passData.getString("entry"));
			return section;
		}
		return null;
	}
	
	public static void init(){
		factories.put("string", StringSection::new);
		deserializers.put(StringSection.TYPE, nbt -> new StringSection(nbt.getString("text")));
		factories.put("guesswork", GuessworkSection::new);
		deserializers.put(GuessworkSection.TYPE, nbt -> new GuessworkSection(nbt.getString("guesswork")));
		factories.put("recipe", RecipeSection::new);
		deserializers.put(RecipeSection.TYPE, nbt -> new RecipeSection(new ResourceLocation(nbt.getString("recipe"))));
	}
	
	// instance stuff
	
	protected List<Requirement> requirements = new ArrayList<>();
	protected ResourceLocation entry;
	
	public void addRequirement(Requirement requirement){
		requirements.add(requirement);
	}
	
	public List<Requirement> getRequirements(){
		return Collections.unmodifiableList(requirements);
	}
	
	public NBTTagCompound getPassData(){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("type", getType());
		nbt.setTag("data", getData());
		nbt.setString("entry", getEntry().toString());
		
		NBTTagList list = new NBTTagList();
		getRequirements().forEach((requirement) -> list.appendTag(requirement.getPassData()));
		nbt.setTag("requirements", list);
		
		return nbt;
	}
	
	public ResourceLocation getEntry(){
		return entry;
	}
	
	public abstract String getType();
	public abstract NBTTagCompound getData();
	public void addOwnRequirements(){}
}