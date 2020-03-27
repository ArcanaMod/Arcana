package net.kineticdevelopment.arcana.core.research.impls;

import com.google.gson.JsonObject;
import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.minecraft.nbt.NBTTagCompound;

public class Fieldwork extends Puzzle{
	
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
}