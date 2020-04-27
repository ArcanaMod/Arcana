package net.arcanamod.research.impls;

import net.arcanamod.research.EntrySection;
import net.minecraft.nbt.NBTTagCompound;
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
	
	public NBTTagCompound getData(){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("recipe", recipe.toString());
		return compound;
	}
	
	public ResourceLocation getRecipe(){
		return recipe;
	}
}