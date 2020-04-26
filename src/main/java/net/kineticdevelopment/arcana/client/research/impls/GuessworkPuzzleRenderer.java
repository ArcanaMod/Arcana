package net.kineticdevelopment.arcana.client.research.impls;

import net.kineticdevelopment.arcana.client.research.PuzzleRenderer;
import net.kineticdevelopment.arcana.common.containers.AspectSlot;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.research.impls.Guesswork;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.ResourceLocation;

import java.util.List;

import static net.kineticdevelopment.arcana.client.gui.ResearchEntryGUI.PAGE_WIDTH;

public class GuessworkPuzzleRenderer extends Gui implements PuzzleRenderer<Guesswork>{
	
	private static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/gui/research/arcanum_gui_overlay.png");
	
	public void render(Guesswork puzzle, List<AspectSlot> puzzleSlots, int screenWidth, int screenHeight, int mouseX, int mouseY, EntityPlayer player){
		drawPaper(screenWidth, screenHeight);
		mc().getTextureManager().bindTexture(texture);
		// render result
		int rX = paperLeft(screenWidth) + 78;
		int rY = paperTop(screenHeight) + 13;
		drawTexturedModalRect(rX, rY, 1, 167, 58, 20);
		int ulX = paperLeft(screenWidth) + 71;
		int ulY = paperTop(screenHeight) + 49;
		drawTexturedModalRect(ulX, ulY, 145, 1, 72, 72);
		RenderHelper.enableGUIStandardItemLighting();
		mc().getRenderItem().renderItemAndEffectIntoGUI(CraftingManager.getRecipe(puzzle.getRecipe()).getRecipeOutput(), rX + 29 - 8, rY + 10 - 8);
	}
}