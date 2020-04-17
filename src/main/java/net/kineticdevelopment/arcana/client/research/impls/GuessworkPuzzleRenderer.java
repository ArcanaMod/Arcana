package net.kineticdevelopment.arcana.client.research.impls;

import net.kineticdevelopment.arcana.client.research.PuzzleRenderer;
import net.kineticdevelopment.arcana.core.research.impls.Guesswork;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;

public class GuessworkPuzzleRenderer extends Gui implements PuzzleRenderer<Guesswork>{
	
	public void render(Guesswork puzzle, int screenWidth, int screenHeight, int mouseX, int mouseY, EntityPlayer player){
		drawPaper(screenWidth, screenHeight);
	}
	
	public List<Pair<Integer, Integer>> getItemSlotLocations(Guesswork puzzle, int screenWidth, int screenHeight, int mouseX, int mouseY, EntityPlayer player){
		return Collections.emptyList();
	}
	
	public List<Pair<Integer, Integer>> getAspectSlotLocations(Guesswork puzzle, int screenWidth, int screenHeight, int mouseX, int mouseY, EntityPlayer player){
		return Collections.emptyList();
	}
}