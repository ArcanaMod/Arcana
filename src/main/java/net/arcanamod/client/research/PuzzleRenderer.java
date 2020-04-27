package net.arcanamod.client.research;

import net.arcanamod.client.gui.ResearchTableGUI;
import net.arcanamod.client.research.impls.ChemistryPuzzleRenderer;
import net.arcanamod.research.impls.Chemistry;
import net.arcanamod.Arcana;
import net.arcanamod.client.research.impls.GuessworkPuzzleRenderer;
import net.arcanamod.containers.AspectSlot;
import net.arcanamod.research.Puzzle;
import net.arcanamod.research.impls.Guesswork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture;

public interface PuzzleRenderer<T extends Puzzle>{
	
	ResourceLocation PAPER = new ResourceLocation(Arcana.MODID, "textures/gui/research/temp_puzzle_overlay.png");
	
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
	
	void render(T puzzle, List<AspectSlot> puzzleSlots, List<Slot> puzzleItemSlots, int screenWidth, int screenHeight, int mouseX, int mouseY, EntityPlayer player);
	
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