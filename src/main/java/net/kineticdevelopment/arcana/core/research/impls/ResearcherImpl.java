package net.kineticdevelopment.arcana.core.research.impls;

import net.kineticdevelopment.arcana.common.event.ResearchEvent;
import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.kineticdevelopment.arcana.core.research.ResearchEntry;
import net.kineticdevelopment.arcana.core.research.Researcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ResearcherImpl implements Researcher{
	
	private Map<ResourceLocation, Integer> data = new HashMap<>();
	private Map<ResourceLocation, Boolean> puzzleData = new HashMap<>();
	private EntityPlayer player;
	
	public int entryStage(ResearchEntry entry){
		return data.getOrDefault(entry.key(), 0);
	}
	
	public boolean isPuzzleCompleted(Puzzle puzzle){
		return puzzleData.get(puzzle.getKey());
	}
	
	public void advanceEntry(ResearchEntry entry){
		// whether or not we can advance is already checked
		do{
			// increment stage
			data.put(entry.key(), entryStage(entry) + 1);
			// as long as there are more stages
			// and the current stage has no requirements
			// this ends up on entry.sections().size(); an entry with 1 section is on stage 0 by default and can be incremented to 1.
			// TODO: don't skip through addenda
		}while(entryStage(entry) < entry.sections().size() && entry.sections().get(entryStage(entry)).getRequirements().size() == 0);
		MinecraftForge.EVENT_BUS.post(new ResearchEvent(getPlayer(), this, entry, ResearchEvent.Type.ADVANCE));
	}
	
	public void completePuzzle(Puzzle puzzle){
		puzzleData.put(puzzle.getKey(), true);
	}
	
	public void resetPuzzle(Puzzle puzzle){
		puzzleData.put(puzzle.getKey(), false);
	}
	
	public void setPuzzleCompletion(Puzzle puzzle, boolean complete){
		puzzleData.put(puzzle.getKey(), complete);
	}
	
	public void completeEntry(ResearchEntry entry){
		if(entryStage(entry) < entry.sections().size()){
			data.put(entry.key(), entry.sections().size());
			MinecraftForge.EVENT_BUS.post(new ResearchEvent(getPlayer(), this, entry, ResearchEvent.Type.COMPLETE));
		}
	}
	
	public void resetEntry(ResearchEntry entry){
		if(entryStage(entry) > 0){
			data.remove(entry.key());
			MinecraftForge.EVENT_BUS.post(new ResearchEvent(getPlayer(), this, entry, ResearchEvent.Type.RESET));
		}
	}
	
	public Map<ResourceLocation, Integer> getEntryData(){
		return Collections.unmodifiableMap(data);
	}
	
	public void setEntryData(Map<ResourceLocation, Integer> data){
		this.data.clear();
		this.data.putAll(data);
	}
	
	public Map<ResourceLocation, Boolean> getPuzzleData(){
		return Collections.unmodifiableMap(puzzleData);
	}
	
	public void setPuzzleData(Map<ResourceLocation, Boolean> data){
		puzzleData.clear();
		puzzleData.putAll(data);
	}
	
	public void setPlayer(EntityPlayer player){
		this.player = player;
	}
	
	public EntityPlayer getPlayer(){
		return player;
	}
}
