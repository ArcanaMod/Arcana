package net.kineticdevelopment.arcana.core.research.impls;

import net.kineticdevelopment.arcana.common.network.Connection;
import net.kineticdevelopment.arcana.common.network.inventory.PktGetNoteHandler;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.research.Requirement;
import net.kineticdevelopment.arcana.core.research.ResearchEntry;
import net.kineticdevelopment.arcana.core.research.Researcher;
import net.kineticdevelopment.arcana.core.research.ResearchBooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class PuzzleRequirement extends Requirement{
	
	public static final ResourceLocation TYPE = new ResourceLocation(Main.MODID, "puzzle");
	
	protected ResourceLocation puzzleId;
	
	public PuzzleRequirement(ResourceLocation puzzleId){
		this.puzzleId = puzzleId;
	}
	
	public boolean satisfied(EntityPlayer player){
		return Researcher.getFrom(player).isPuzzleCompleted(ResearchBooks.puzzles.get(puzzleId));
	}
	
	public void take(EntityPlayer player){
		// no-op
	}
	
	public ResourceLocation type(){
		return TYPE;
	}
	
	public NBTTagCompound data(){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("puzzle", puzzleId.toString());
		return compound;
	}
	
	public void onClick(ResearchEntry entry){
		if(!(ResearchBooks.puzzles.get(puzzleId) instanceof Fieldwork))
			Connection.network.sendToServer(new PktGetNoteHandler.PktGetNote(puzzleId.toString(), entry.key().toString()));
	}
	
	public ResourceLocation getPuzzleId(){
		return puzzleId;
	}
}