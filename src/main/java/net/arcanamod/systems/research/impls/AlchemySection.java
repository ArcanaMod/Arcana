package net.arcanamod.systems.research.impls;

import net.minecraft.util.ResourceLocation;

public class AlchemySection extends AbstractCraftingSection{
	
	public static final String TYPE = "alchemy";
	
	public AlchemySection(ResourceLocation recipe){
		super(recipe);
	}
	
	public AlchemySection(String s){
		super(s);
	}
	
	public String getType(){
		return TYPE;
	}
}