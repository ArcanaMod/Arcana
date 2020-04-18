package net.kineticdevelopment.arcana.client.research.impls;

import net.kineticdevelopment.arcana.client.research.PuzzleRenderer;
import net.kineticdevelopment.arcana.common.containers.AspectSlot;
import net.kineticdevelopment.arcana.core.research.impls.Guesswork;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class GuessworkPuzzleRenderer extends Gui implements PuzzleRenderer<Guesswork>{
	
	public void render(Guesswork puzzle, List<AspectSlot> puzzleSlots, int screenWidth, int screenHeight, int mouseX, int mouseY, EntityPlayer player){
		drawPaper(screenWidth, screenHeight);
	}
}