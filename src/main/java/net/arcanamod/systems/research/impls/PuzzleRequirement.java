package net.arcanamod.systems.research.impls;

import net.arcanamod.capabilities.Researcher;
import net.arcanamod.network.Connection;
import net.arcanamod.systems.research.Puzzle;
import net.arcanamod.systems.research.Requirement;
import net.arcanamod.systems.research.ResearchBooks;
import net.arcanamod.systems.research.ResearchEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import static net.arcanamod.Arcana.arcLoc;

public class PuzzleRequirement extends Requirement{
	
	public static final ResourceLocation TYPE = arcLoc("puzzle");
	
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
	
	public boolean onClick(ResearchEntry entry, PlayerEntity player){
		Puzzle puzzle = ResearchBooks.puzzles.get(puzzleId);
		if(!(puzzle instanceof Fieldwork || satisfied(player)))
			Connection.sendGetNoteHandler(puzzleId, entry.key().toString());
		return false;
	}
	
	public ResourceLocation getPuzzleId(){
		return puzzleId;
	}
}