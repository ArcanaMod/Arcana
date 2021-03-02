package net.arcanamod.systems.research.impls;

import net.arcanamod.systems.research.EntrySection;
import net.minecraft.nbt.CompoundNBT;

public class AspectCombosSection extends EntrySection{
	
	public static final String TYPE = "aspect_combos";
	
	public String getType(){
		return TYPE;
	}
	
	public CompoundNBT getData(){
		return new CompoundNBT();
	}
}