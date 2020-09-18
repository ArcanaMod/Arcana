package net.arcanamod.systems.research.impls;

import net.minecraft.util.ResourceLocation;

public class ArcaneCraftingSection extends AbstractCraftingSection{
	
	public static final String TYPE = "arcane_crafting";
	
	public ArcaneCraftingSection(ResourceLocation recipe){
		super(recipe);
	}
	
	public ArcaneCraftingSection(String s){
		super(s);
	}
	
	public String getType(){
		return TYPE;
	}
}