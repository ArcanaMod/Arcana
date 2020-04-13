package net.kineticdevelopment.arcana.client.research.impls;

import net.kineticdevelopment.arcana.client.research.PuzzleRenderer;
import net.kineticdevelopment.arcana.core.research.impls.Guesswork;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class GuessworkPuzzleRenderer implements PuzzleRenderer<Guesswork>{
	
	public void render(Guesswork puzzle){
	
	}
	
	public List<Pair<Integer, Integer>> getItemSlotLocations(Guesswork puzzle){
		return null;
	}
	
	public List<Pair<Integer, Integer>> getAspectSlotLocations(Guesswork puzzle){
		return null;
	}
}