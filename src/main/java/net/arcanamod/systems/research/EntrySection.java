package net.arcanamod.systems.research;

import net.arcanamod.systems.research.impls.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.arcanamod.util.StreamUtils.streamAndApply;

/**
 * Represents one section of content - for example, continuous text, an image, or an inline recipe. May provide a number of pins.
 */
public abstract class EntrySection{
	
	// static stuff
	// when addon support is to be added: change this from strings to ResourceLocations so mods can register more
	private static Map<String, Function<String, EntrySection>> factories = new LinkedHashMap<>();
	private static Map<String, Function<CompoundNBT, EntrySection>> deserializers = new LinkedHashMap<>();
	
	public static Function<String, EntrySection> getFactory(String type){
		return factories.get(type);
	}
	
	public static EntrySection makeSection(String type, String content){
		if(getFactory(type) != null)
			return getFactory(type).apply(content);
		else
			return null;
	}
	
	public static EntrySection deserialze(CompoundNBT passData){
		String type = passData.getString("type");
		CompoundNBT data = passData.getCompound("data");
		List<Requirement> requirements = streamAndApply(passData.getList("requirements", 10), CompoundNBT.class, Requirement::deserialize).collect(Collectors.toList());
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
		factories.put(StringSection.TYPE, StringSection::new);
		deserializers.put(StringSection.TYPE, nbt -> new StringSection(nbt.getString("text")));
		factories.put(CraftingSection.TYPE, CraftingSection::new);
		deserializers.put(CraftingSection.TYPE, nbt -> new CraftingSection(nbt.getString("recipe")));
		factories.put(SmeltingSection.TYPE, SmeltingSection::new);
		deserializers.put(SmeltingSection.TYPE, nbt -> new SmeltingSection(nbt.getString("recipe")));
		factories.put(AlchemySection.TYPE, AlchemySection::new);
		deserializers.put(AlchemySection.TYPE, nbt -> new AlchemySection(nbt.getString("recipe")));
		factories.put(ArcaneCraftingSection.TYPE, ArcaneCraftingSection::new);
		deserializers.put(ArcaneCraftingSection.TYPE, nbt -> new ArcaneCraftingSection(nbt.getString("recipe")));
		factories.put(ImageSection.TYPE, ImageSection::new);
		deserializers.put(ImageSection.TYPE, nbt -> new ImageSection(nbt.getString("image")));
		factories.put(AspectCombosSection.TYPE, __ -> new AspectCombosSection());
		deserializers.put(AspectCombosSection.TYPE, __ -> new AspectCombosSection());
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
	
	public CompoundNBT getPassData(){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("type", getType());
		nbt.put("data", getData());
		nbt.putString("entry", getEntry().toString());
		
		ListNBT list = new ListNBT();
		getRequirements().forEach((requirement) -> list.add(requirement.getPassData()));
		nbt.put("requirements", list);
		
		return nbt;
	}
	
	public ResourceLocation getEntry(){
		return entry;
	}
	
	public abstract String getType();
	
	public abstract CompoundNBT getData();
	
	public void addOwnRequirements(){
	}
	
	/**
	 * Returns a stream containing this entry's pins.
	 *
	 * @param index
	 * 		The index in the entry of this section.
	 * @param world
	 * 		The world the player is in.
	 * @param entry
	 * 		The entry this is in.
	 * @return This entry's pins.
	 */
	public Stream<Pin> getPins(int index, World world, ResearchEntry entry){
		return Stream.empty();
	}
}