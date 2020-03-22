package net.kineticdevelopment.arcana.core.research;

import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.Map;

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
}