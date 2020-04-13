package net.kineticdevelopment.arcana.client.research;

import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.kineticdevelopment.arcana.core.research.impls.Guesswork;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PuzzleRenderer<T extends Puzzle>{
	
	Map<String, PuzzleRenderer<?>> map = new HashMap<>();
	
	static void init(){
		map.put(Guesswork.TYPE, null);
	}
	
	static <T extends Puzzle> PuzzleRenderer<T> get(String type){
		return (PuzzleRenderer<T>)map.get(type);
	}
	
	static <T extends Puzzle> PuzzleRenderer<T> get(Puzzle puzzle){
		return get(puzzle.type());
	}
	
	void render(T puzzle);
	
	List<Pair<Integer, Integer>> getItemSlotLocations(T puzzle);
	
	List<Pair<Integer, Integer>> getAspectSlotLocations(T puzzle);
}