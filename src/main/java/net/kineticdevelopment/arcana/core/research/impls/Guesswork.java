package net.kineticdevelopment.arcana.core.research.impls;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Guesswork extends Puzzle{
	
	private static final ResourceLocation ICON = new ResourceLocation(Main.MODID, "textures/item/research_note.png");
	private static final Logger LOGGER = LogManager.getLogger();
	
	// Check RecipeSectionRenderer for how non-crafting recipes are handled.
	protected ResourceLocation recipe;
	protected Map<ResourceLocation, String> hints;
	
	public Guesswork(){
		// empty for loading
	}
	
	public Guesswork(ResourceLocation recipe, Map<ResourceLocation, String> hints){
		this.recipe = recipe;
		this.hints = hints;
	}
	
	public void load(JsonObject data, ResourceLocation file){
		ResourceLocation recipe = new ResourceLocation(data.get("recipe").getAsString());
		JsonArray hints = data.getAsJsonArray("hints");
		Map<ResourceLocation, String> hintMap = new LinkedHashMap<>();
		for(JsonElement hint : hints){
			if(hint.isJsonPrimitive()){
				String hintSt = hint.getAsString();
				if(hintSt.contains("=")){
					ResourceLocation key = new ResourceLocation(hintSt.split("=")[0]);
					String value = hintSt.split("=")[1];
					hintMap.put(key, value);
				}else
					LOGGER.error("String not containing \"=\" found in puzzle in " + file + "!");
			}else
				LOGGER.error("Non-String found in hints array in puzzle in " + file + "!");
		}
		this.recipe = recipe;
		this.hints = hintMap;
	}
	
	public String type(){
		return "guesswork";
	}
	
	public ResourceLocation getRecipe(){
		return recipe;
	}
	
	public Map<ResourceLocation, String> getHints(){
		return Collections.unmodifiableMap(hints);
	}
	
	public NBTTagCompound getData(){
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagCompound hints = new NBTTagCompound();
		getHints().forEach((location, s) -> hints.setString(location.toString(), s));
		compound.setString("recipe", getRecipe().toString());
		compound.setTag("hints", hints);
		return compound;
	}
	
	public String getDefaultDesc(){
		return "requirement.guesswork";
	}
	
	public ResourceLocation getDefaultIcon(){
		return ICON;
	}
	
	public static Guesswork deserialize(NBTTagCompound passData){
		ResourceLocation recipe = new ResourceLocation(passData.getString("recipe"));
		Map<ResourceLocation, String> hints = new HashMap<>();
		NBTTagCompound serialHints = passData.getCompoundTag("hints");
		for(String s : serialHints.getKeySet())
			hints.put(new ResourceLocation(s), serialHints.getString(s));
		return new Guesswork(recipe, hints);
	}
	
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(!(o instanceof Guesswork))
			return false;
		Guesswork guesswork = (Guesswork)o;
		return getRecipe().equals(guesswork.getRecipe()) && getHints().equals(guesswork.getHints());
	}
	
	public int hashCode(){
		return Objects.hash(getRecipe(), getHints());
	}
}