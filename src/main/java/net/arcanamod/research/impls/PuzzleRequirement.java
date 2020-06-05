package net.arcanamod.research.impls;

import net.arcanamod.Arcana;
import net.arcanamod.network.Connection;
import net.arcanamod.research.Requirement;
import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.ResearchEntry;
import net.arcanamod.research.Researcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class PuzzleRequirement extends Requirement{
	
	public static final ResourceLocation TYPE = new ResourceLocation(Arcana.MODID, "puzzle");
	
	protected ResourceLocation puzzleId;
	
	public PuzzleRequirement(ResourceLocation puzzleId){
		this.puzzleId = puzzleId;
	}
	
	public boolean satisfied(PlayerEntity player){
		return Researcher.getFrom(player).isPuzzleCompleted(ResearchBooks.puzzles.get(puzzleId));
	}
	
	public void take(PlayerEntity player){
		// no-op
	}
	
	public ResourceLocation type(){
		return TYPE;
	}
	
	public CompoundNBT data(){
		CompoundNBT compound = new CompoundNBT();
		compound.putString("puzzle", puzzleId.toString());
		return compound;
	}
	
	public boolean onClick(ResearchEntry entry){
		if(!(ResearchBooks.puzzles.get(puzzleId) instanceof Fieldwork))
			Connection.sendGetNoteHandler(puzzleId, entry.key().toString());
		return false;
	}
	
	public ResourceLocation getPuzzleId(){
		return puzzleId;
	}
}