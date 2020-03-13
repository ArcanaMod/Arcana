package net.kineticdevelopment.arcana.core.research;

import net.kineticdevelopment.arcana.core.research.impls.GuessworkSection;
import net.kineticdevelopment.arcana.core.research.impls.StringSection;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;
import java.util.function.Function;

/**
 * Represents one section of content - for example, continuous text, an image, or an inline recipe.
 */
public abstract class EntrySection{
	
	// when addon support is to be added: change this from strings to ResourceLocations so mods can register more
	private static Map<String, Function<String, EntrySection>> factories = new LinkedHashMap<>();
	private static Map<String, Function<NBTTagCompound, EntrySection>> deserializers = new LinkedHashMap<>();
	
	protected List<Requirement> requirements = new ArrayList<>();
	
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
		if(deserializers.get(type) != null)
			return deserializers.get(type).apply(data);
		return null;
	}
	
	public static void init(){
		factories.put("string", StringSection::new);
		deserializers.put(StringSection.TYPE, nbt -> new StringSection(nbt.getString("text")));
		factories.put("guesswork", GuessworkSection::new);
		deserializers.put(GuessworkSection.TYPE, nbt -> new GuessworkSection(nbt.getString("guesswork")));
	}
	
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
		return nbt;
	}
	
	public abstract String getType();
	public abstract NBTTagCompound getData();
}