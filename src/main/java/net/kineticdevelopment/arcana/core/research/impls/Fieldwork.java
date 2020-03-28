package net.kineticdevelopment.arcana.core.research.impls;

import com.google.gson.JsonObject;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class Fieldwork extends Puzzle{
	
	private static final ResourceLocation ICON = new ResourceLocation(Main.MODID, "textures/gui/research/fieldwork.png");
	
	public void load(JsonObject data){
		// no-op
	}
	
	public String type(){
		return "fieldwork";
	}
	
	public NBTTagCompound getData(){
		// no-op
		return new NBTTagCompound();
	}
	
	public String getDefaultDesc(){
		return "requirement.fieldwork";
	}
	
	public ResourceLocation getDefaultIcon(){
		return ICON;
	}
}