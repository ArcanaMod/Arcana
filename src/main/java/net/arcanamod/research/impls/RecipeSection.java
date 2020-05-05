package net.arcanamod.research.impls;

import net.arcanamod.research.EntrySection;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class RecipeSection extends EntrySection{
	
	public static final String TYPE = "RecipeSection";
	
	ResourceLocation recipe;
	
	public RecipeSection(ResourceLocation recipe){
		this.recipe = recipe;
	}
	
	public RecipeSection(String s){
		this(new ResourceLocation(s));
	}
	
	public String getType(){
		return TYPE;
	}
	
	public CompoundNBT getData(){
		CompoundNBT compound = new CompoundNBT();
		compound.setString("recipe", recipe.toString());
		return compound;
	}
	
	public ResourceLocation getRecipe(){
		return recipe;
	}
}