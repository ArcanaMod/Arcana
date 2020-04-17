package net.kineticdevelopment.arcana.client.research.impls;

import net.kineticdevelopment.arcana.client.research.PuzzleRenderer;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.research.impls.Chemistry;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ChemistryPuzzleRenderer extends Gui implements PuzzleRenderer<Chemistry>{
	
	private static final ResourceLocation TEX = new ResourceLocation(Main.MODID, "textures/gui/research/chemistry_overlay.png");
	
	public void render(Chemistry puzzle, int screenWidth, int screenHeight, int mouseX, int mouseY, EntityPlayer player){
		drawPaper(screenWidth, screenHeight);
		// how to generate a hexagonal grid?
		// 20x20 per hex
		// +23x, +-18y moving right
		// +11x on odd rows
		mc().getTextureManager().bindTexture(TEX);
		int gridWidth = 8, gridHeight = 6;
		for(int x = 0; x < gridWidth; x++){
			for(int y = 0; y < gridHeight; y++){
				int xx = x * 22 + (y % 2 == 0 ? 11 : 0);
				int yy = y * 19;
				drawTexturedModalRect(paperLeft(screenWidth) + xx + (214 - (23 * gridWidth - 2)) / 2, paperTop(screenHeight) + yy + (134 - (19 * gridHeight + 1)) / 2, 0, 0, 20, 20);
			}
		}
	}
	
	private List<Pair<Integer, Integer>> genHexGrid(int gridWidth, int gridHeight, int screenWidth,  int screenHeight){
		List<Pair<Integer, Integer>> grid = new ArrayList<>();
		for(int x = 0; x < gridWidth; x++){
			for(int y = 0; y < gridHeight; y++){
				int xx = x * 22 + (y % 2 == 0 ? 11 : 0);
				int yy = y * 19;
				int scX = xx + paperLeft(screenWidth) + (214 - (23 * gridWidth - 2)) / 2;
				int scY = yy + paperTop(screenHeight) + (134 - (19 * gridHeight + 1)) / 2;
				grid.add(Pair.of(scX, scY));
			
			}
		}
		return grid;
	}
}