package net.kineticdevelopment.arcana.core.research.impls;

import net.kineticdevelopment.arcana.common.network.Connection;
import net.kineticdevelopment.arcana.common.network.inventory.PktGetNoteHandler;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.research.Requirement;
import net.kineticdevelopment.arcana.core.research.Researcher;
import net.kineticdevelopment.arcana.core.research.ServerBooks;
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
		return Researcher.getFrom(player).isPuzzleCompleted(ServerBooks.puzzles.get(puzzleId));
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
	
	public void onClick(){
		if(!(ServerBooks.puzzles.get(puzzleId) instanceof Fieldwork)) //
			Connection.network.sendToServer(new PktGetNoteHandler.PktGetNote(puzzleId));
	}
	
	public ResourceLocation getPuzzleId(){
		return puzzleId;
	}
}