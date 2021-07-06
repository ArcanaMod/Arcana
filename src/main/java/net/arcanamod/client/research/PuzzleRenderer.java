package net.arcanamod.client.research;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.client.gui.ResearchTableScreen;
import net.arcanamod.client.research.impls.ChemistryPuzzleRenderer;
import net.arcanamod.client.research.impls.ThaumaturgyPuzzleRenderer;
import net.arcanamod.systems.research.impls.Chemistry;
import net.arcanamod.Arcana;
import net.arcanamod.client.research.impls.GuessworkPuzzleRenderer;
import net.arcanamod.containers.slots.AspectSlot;
import net.arcanamod.systems.research.Puzzle;
import net.arcanamod.systems.research.impls.Guesswork;
import net.arcanamod.systems.research.impls.Thaumaturgy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.client.gui.AbstractGui.blit;

public interface PuzzleRenderer<T extends Puzzle> {
	ResourceLocation PAPER = new ResourceLocation(Arcana.MODID, "textures/gui/research/temp_puzzle_overlay.png");
	
	Map<String, PuzzleRenderer<?>> map = new HashMap<>();
	
	static void init(){
		// could easily be if/else, but I do want to avoid hardcoding
		map.put(Guesswork.TYPE, new GuessworkPuzzleRenderer());
		map.put(Chemistry.TYPE, new ChemistryPuzzleRenderer());
		map.put(Thaumaturgy.TYPE, new ThaumaturgyPuzzleRenderer());
	}
	
	static <T extends Puzzle> PuzzleRenderer<T> get(String type){
		return (PuzzleRenderer<T>)map.get(type);
	}
	
	static <T extends Puzzle> PuzzleRenderer<T> get(Puzzle puzzle){
		return get(puzzle.type());
	}
	
	void render(MatrixStack stack, T puzzle, List<AspectSlot> puzzleSlots, List<Slot> puzzleItemSlots, int screenWidth, int screenHeight, int mouseX, int mouseY, PlayerEntity player);
	
	default Minecraft mc(){
		return Minecraft.getInstance();
	}
	
	default FontRenderer fr(){
		return mc().fontRenderer;
	}
	
	default void drawPaper(MatrixStack stack, int screenWidth, int screenHeight){
		mc().getTextureManager().bindTexture(PAPER);
		blit(stack, guiLeft(screenWidth) + 141, guiTop(screenHeight) + 35, 0, 0, 214, 134, 214, 134);
	}
	
	default void renderAfter(MatrixStack stack, T puzzle, List<AspectSlot> puzzleSlots, List<Slot> puzzleItemSlots, int screenWidth, int screenHeight, int mouseX, int mouseY, PlayerEntity player) {}
	
	default int guiLeft(int screenWidth){
		return (screenWidth - ResearchTableScreen.WIDTH) / 2;
	}
	
	default int guiTop(int screenHeight){
		return (screenHeight - ResearchTableScreen.HEIGHT) / 2;
	}
	
	default int paperLeft(int screenWidth){
		return guiLeft(screenWidth) + 141;
	}
	
	default int paperTop(int screenHeight){
		return guiTop(screenHeight) + 35;
	}
}