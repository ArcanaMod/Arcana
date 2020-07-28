package net.arcanamod.research;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static net.arcanamod.util.StreamUtils.streamAndApply;

/**
 * Represents a node in the research tree. Stores an ordered list of entry sections representing its content.
 */
public class ResearchEntry{
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private ResourceLocation key;
	private List<EntrySection> sections;
	private List<String> meta;
	private List<ResourceLocation> parents;
	private List<Icon> icons;
	private ResearchCategory category;
	
	private String name, desc;
	
	private int x, y;
	
	public ResearchEntry(ResourceLocation key, List<EntrySection> sections, List<Icon> icons, List<String> meta, List<ResourceLocation> parents, ResearchCategory category, String name, String desc, int x, int y){
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
		icons().forEach((icon) -> icons.add(StringNBT.valueOf(icon.toString())));
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
		List<EntrySection> sections = streamAndApply(nbt.getList("sections", 10), CompoundNBT.class, EntrySection::deserialze).collect(Collectors.toList());
		List<ResourceLocation> parents = streamAndApply(nbt.getList("parents", 8), StringNBT.class, StringNBT::getString).map(ResourceLocation::new).collect(Collectors.toList());
		List<Icon> icons = streamAndApply(nbt.getList("icons", 8), StringNBT.class, StringNBT::getString).map(Icon::fromString).collect(Collectors.toList());
		List<String> meta = streamAndApply(nbt.getList("meta", 8), StringNBT.class, StringNBT::getString).collect(Collectors.toList());
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
	
	public static class Icon{
		
		// Either an item, with optional NBT data, or an direct image reference.
		// Images are assumed to be in <namespace>:textures/.
		// Any resource locations that point to items are assumed to be items; otherwise its assumed to be an image.
		// NBT data can be encoded too. When NBT data is present, an error will be logged if the reference is not an item.
		// NBT data is added in curly braces after the ID, as valid JSON.
		// See JsonToNBT.
		
		private ResourceLocation resourceLocation;
		@Nullable
		private ItemStack stack;
		
		public Icon(ResourceLocation resourceLocation, @Nullable ItemStack stack){
			this.resourceLocation = resourceLocation;
			this.stack = stack;
		}
		
		@Nullable
		public ItemStack getStack(){
			return stack;
		}
		
		public ResourceLocation getResourceLocation(){
			return resourceLocation;
		}
		
		public static Icon fromString(String string){
			// Check if theres NBT data.
			CompoundNBT tag = null;
			if(string.contains("{")){
				String[] split = string.split("\\{", 2);
				try{
					tag = JsonToNBT.getTagFromJson("{" + split[1]);
					string = split[0];
				}catch(CommandSyntaxException e){
					e.printStackTrace();
					LOGGER.error("Unable to parse JSON: {" + split[1]);
				}
			}
			// Check if there's an item that corresponds to the ID.
			ResourceLocation key = new ResourceLocation(string);
			if(ForgeRegistries.ITEMS.containsKey(key)){
				Item item = ForgeRegistries.ITEMS.getValue(key);
				ItemStack stack = new ItemStack(item);
				// Apply NBT, if any.
				if(tag != null)
					stack.setTag(tag);
				// Return icon.
				return new Icon(key, stack);
			}
			// Otherwise, return the ID as an image.
			// If NBT was encoded, this is probably wrong.
			if(tag != null)
				LOGGER.error("NBT data was encoded for research entry icon " + key + ", but " + key + " is not an item!");
			// Add "textures/" to path.
			key = new ResourceLocation(key.getNamespace(), "textures/" + key.getPath());
			return new Icon(key, null);
		}
		
		public String toString(){
			// If ItemStack is null, just provide the key, but substring'd by 9.
			if(stack == null)
				return new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath().substring(9)).toString();
			// If there's no NBT, just send over the item's ID.
			if(!stack.hasTag())
				return resourceLocation.toString();
			// Otherwise, we need to send over both.
			return resourceLocation.toString() + nbtToJson(stack.getTag());
		}
		
		private static String nbtToJson(CompoundNBT nbt){
			StringBuilder stringbuilder = new StringBuilder("{");
			Collection<String> collection = nbt.keySet();
			
			for(String s : collection){
				if(stringbuilder.length() != 1)
					stringbuilder.append(',');
				stringbuilder.append(handleEscape(s)).append(':').append(nbt.get(s) instanceof StringNBT ? "\"" + nbt.getString(s) + "\"" : nbt.get(s));
			}
			return stringbuilder.append('}').toString();
		}
		
		private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
		protected static String handleEscape(String in) {
			return SIMPLE_VALUE.matcher(in).matches() ? "\"" + in + "\"" : StringNBT.quoteAndEscape(in);
		}
	}
}