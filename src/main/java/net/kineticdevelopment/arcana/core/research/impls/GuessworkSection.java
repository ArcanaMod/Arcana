package net.kineticdevelopment.arcana.core.research.impls;

import net.kineticdevelopment.arcana.core.research.EntrySection;
import net.minecraft.nbt.NBTTagCompound;

/**
 * An entry section that displays a guesswork game's status, and allows the player to obtain a corresponding scroll (or whatever its called).
 * The actual guessing itself is performed at a research table.
 */
public class GuessworkSection extends EntrySection{
	
	public static final String TYPE = "GuessworkSection";
	
	String guessworkName;
	
	public GuessworkSection(String guessworkName){
		this.guessworkName = guessworkName;
	}
	
	public String getType(){
		return TYPE;
	}
	
	public NBTTagCompound getData(){
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("guesswork", getGuessworkName());
		return tag;
	}
	
	public String getGuessworkName(){
		return guessworkName;
	}
}