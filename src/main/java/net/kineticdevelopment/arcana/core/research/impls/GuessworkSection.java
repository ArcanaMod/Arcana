package net.kineticdevelopment.arcana.core.research.impls;

import net.kineticdevelopment.arcana.core.research.EntrySection;
import net.minecraft.nbt.NBTTagCompound;

/**
 * An entry section that displays a guesswork game's status, and allows the player to obtain a corresponding scroll (or whatever its called).
 * The actual guessing itself is performed at a research table.
 */
public class GuessworkSection extends EntrySection{
	
	public static final String TYPE = "GuessworkSection";
	
	int guessworkIndex;
	
	public GuessworkSection(String guessworkIndex){
		this.guessworkIndex = /*Integer.parseInt(guessworkIndex)*/0;
	}
	
	public GuessworkSection(int guessworkIndex){
		this.guessworkIndex = guessworkIndex;
	}
	
	public String getType(){
		return TYPE;
	}
	
	public NBTTagCompound getData(){
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("guesswork", getGuessworkIndex());
		return tag;
	}
	
	public int getGuessworkIndex(){
		return guessworkIndex;
	}
	
	public void addOwnRequirements(){
		addRequirement(new GuessworkRequirement(guessworkIndex));
	}
}