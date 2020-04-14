package net.kineticdevelopment.arcana.core.research.impls;

import com.google.gson.JsonObject;
import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class Chemistry extends Puzzle{
	
	public void load(JsonObject data, ResourceLocation file){
	
	}
	
	public String type(){
		return null;
	}
	
	public NBTTagCompound getData(){
		return null;
	}
	
	public String getDefaultDesc(){
		return null;
	}
	
	public ResourceLocation getDefaultIcon(){
		return null;
	}
}