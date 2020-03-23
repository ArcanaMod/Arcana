package net.kineticdevelopment.arcana.core.research;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class Guesswork{
	
	// Check RecipeSectionRenderer for how non-crafting recipes are handled.
	protected ResourceLocation recipe;
	protected Map<ResourceLocation, String> hints;
	
	public Guesswork(ResourceLocation recipe, Map<ResourceLocation, String> hints){
		this.recipe = recipe;
		this.hints = hints;
	}
	
	public ResourceLocation getRecipe(){
		return recipe;
	}
	
	public Map<ResourceLocation, String> getHints(){
		return Collections.unmodifiableMap(hints);
	}
	
	public NBTTagCompound getPassData(){
		return new NBTTagCompound();
	}
	
	public static Guesswork deserialize(NBTTagCompound passData){
		return null;
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