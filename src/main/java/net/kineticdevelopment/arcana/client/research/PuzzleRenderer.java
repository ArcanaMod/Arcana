package net.kineticdevelopment.arcana.client.research;

import net.kineticdevelopment.arcana.client.gui.ResearchTableGUI;
import net.kineticdevelopment.arcana.client.research.impls.ChemistryPuzzleRenderer;
import net.kineticdevelopment.arcana.client.research.impls.GuessworkPuzzleRenderer;
import net.kineticdevelopment.arcana.common.containers.AspectSlot;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.kineticdevelopment.arcana.core.research.impls.Chemistry;
import net.kineticdevelopment.arcana.core.research.impls.Guesswork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture;

public interface PuzzleRenderer<T extends Puzzle>{
	
	ResourceLocation PAPER = new ResourceLocation(Main.MODID, "textures/gui/research/temp_puzzle_overlay.png");
	
	Map<String, PuzzleRenderer<?>> map = new HashMap<>();
	
	static void init(){
		// could easily be if/else, but I do want to avoid hardcoding
		map.put(Guesswork.TYPE, new GuessworkPuzzleRenderer());
		map.put(Chemistry.TYPE, new ChemistryPuzzleRenderer());
	}
	
	static <T extends Puzzle> PuzzleRenderer<T> get(String type){
		return (PuzzleRenderer<T>)map.get(type);
	}
	
	static <T extends Puzzle> PuzzleRenderer<T> get(Puzzle puzzle){
		return get(puzzle.type());
	}
	
	void render(T puzzle, List<AspectSlot> puzzleSlots, int screenWidth, int screenHeight, int mouseX, int mouseY, EntityPlayer player);
	
	default Minecraft mc(){
		return Minecraft.getMinecraft();
	}
	
	default FontRenderer fr(){
		return mc().fontRenderer;
	}
	
	default void drawPaper(int screenWidth, int screenHeight){
		mc().getTextureManager().bindTexture(PAPER);
		drawModalRectWithCustomSizedTexture(guiLeft(screenWidth) + 141, guiTop(screenHeight) + 35, 0, 0, 214, 134, 214, 134);
	}
	
	default int guiLeft(int screenWidth){
		return (screenWidth - ResearchTableGUI.WIDTH) / 2;
	}
	
	default int guiTop(int screenHeight){
		return (screenHeight - ResearchTableGUI.HEIGHT) / 2;
	}
	
	default int paperLeft(int screenWidth){
		return guiLeft(screenWidth) + 141;
	}
	
	default int paperTop(int screenHeight){
		return guiTop(screenHeight) + 35;
	}
}