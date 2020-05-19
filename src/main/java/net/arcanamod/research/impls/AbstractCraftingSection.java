package net.arcanamod.research.impls;

import net.arcanamod.research.EntrySection;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractCraftingSection extends EntrySection{
	
	ResourceLocation recipe;
	
	public AbstractCraftingSection(ResourceLocation recipe){
		this.recipe = recipe;
	}
	
	public AbstractCraftingSection(String s){
		this(new ResourceLocation(s));
	}
	
	public CompoundNBT getData(){
		CompoundNBT compound = new CompoundNBT();
		compound.putString("recipe", recipe.toString());
		return compound;
	}
	
	public ResourceLocation getRecipe(){
		return recipe;
	}
}