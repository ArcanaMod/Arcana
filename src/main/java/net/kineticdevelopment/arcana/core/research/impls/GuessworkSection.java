package net.kineticdevelopment.arcana.core.research.impls;

import net.kineticdevelopment.arcana.core.research.EntrySection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * An entry section that displays a guesswork game's status, and allows the player to obtain a corresponding scroll (or whatever its called).
 * The actual guessing itself is performed at a research table.
 */
public class GuessworkSection extends EntrySection{
	
	public static final String TYPE = "GuessworkSection";
	
	protected ResourceLocation puzzleId;
	
	public GuessworkSection(String guessworkId){
		this.puzzleId = new ResourceLocation(guessworkId);
	}
	
	public GuessworkSection(ResourceLocation guessworkId){
		this.puzzleId = guessworkId;
	}
	
	public String getType(){
		return TYPE;
	}
	
	public NBTTagCompound getData(){
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("guesswork", getGuessworkId().toString());
		return tag;
	}
	
	public ResourceLocation getGuessworkId(){
		return puzzleId;
	}
	
	public void addOwnRequirements(){
		addRequirement(new PuzzleRequirement(getGuessworkId()));
	}
}