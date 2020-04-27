package net.arcanamod.research.impls;

import net.arcanamod.Arcana;
import net.arcanamod.network.Connection;
import net.arcanamod.network.inventory.PktGetNoteHandler;
import net.arcanamod.research.Requirement;
import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.ResearchEntry;
import net.arcanamod.research.Researcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class PuzzleRequirement extends Requirement{
	
	public static final ResourceLocation TYPE = new ResourceLocation(Arcana.MODID, "puzzle");
	
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