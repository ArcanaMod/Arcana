package net.arcanamod.systems.research;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * An icon associated with a research entry or pin. This can either directly reference an image file, or the texture of an item
 * with set NBT data.
 * <p>
 * An icon is parsed by first checking for an item with an ID that matches the icon. If there is an item, any NBT tags will be
 * parsed as JSON. If there are no items that correlate to that ID, then an image is checked for in <code>&lt;namespace&gt;:textures/</code>.
 *
 * @see ResearchEntry
 * @see Pin
 * @see JsonToNBT
 */
public class Icon{
	
	// Either an item, with optional NBT data, or an direct image reference.
	// Images are assumed to be in <namespace>:textures/.
	// Any resource locations that point to items are assumed to be items; otherwise its assumed to be an image.
	// NBT data can be encoded too. When NBT data is present, an error will be logged if the reference is not an item.
	// NBT data is added in curly braces after the ID, as valid JSON.
	// See JsonToNBT.
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private ResourceLocation resourceLocation;
	@Nullable
	private ItemStack stack;
	
	public Icon(ResourceLocation resourceLocation, @Nullable ItemStack stack){
		this.resourceLocation = resourceLocation;
		this.stack = stack;
	}
	
	public Icon(ItemStack stack){
		this.resourceLocation = stack.getItem().getRegistryName();
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
	
	protected static String handleEscape(String in){
		return SIMPLE_VALUE.matcher(in).matches() ? "\"" + in + "\"" : StringNBT.quoteAndEscape(in);
	}
}
