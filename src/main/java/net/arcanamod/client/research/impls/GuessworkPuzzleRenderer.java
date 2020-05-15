package net.arcanamod.client.research.impls;

import net.arcanamod.Arcana;
import net.arcanamod.client.research.PuzzleRenderer;
import net.arcanamod.containers.AspectSlot;
import net.arcanamod.research.impls.Guesswork;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.arcanamod.client.gui.ResearchEntryGUI.drawTexturedModalRect;

public class GuessworkPuzzleRenderer extends AbstractGui implements PuzzleRenderer<Guesswork>{
	
	private static final ResourceLocation texture = new ResourceLocation(Arcana.MODID, "textures/gui/research/arcanum_gui_overlay.png");
	
	private static final String[] hintSymbols = {"#", "A", "S", "D", "F", "&", "Q", "@", "~"};
	private static final int[] hintColours = {0x8a8a8a, 0x32a852, 0x58e8da, 0x7a58e8, 0xc458e8, 0xe858d5, 0xe85858, 0x136e13, 0x6e6613, 0x99431c, 0x43355e};
	
	public void render(Guesswork puzzle, List<AspectSlot> puzzleSlots, List<Slot> puzzleItemSlots, int screenWidth, int screenHeight, int mouseX, int mouseY, PlayerEntity player){
		drawPaper(screenWidth, screenHeight);
		mc().getTextureManager().bindTexture(texture);
		// render result
		int rX = paperLeft(screenWidth) + 78;
		int rY = paperTop(screenHeight) + 13;
		drawTexturedModalRect(rX, rY, 1, 167, 58, 20);
		int ulX = paperLeft(screenWidth) + 10;
		int ulY = paperTop(screenHeight) + 49;
		drawTexturedModalRect(ulX, ulY, 145, 1, 72, 72);
		RenderHelper.enableStandardItemLighting();
		//mc().getItemRenderer().renderItemAndEffectIntoGUI(CraftingManager.getRecipe(puzzle.getRecipe()).getRecipeOutput(), rX + 29 - 8, rY + 10 - 8);
		
		List<Map.Entry<ResourceLocation, String>> indexHelper = new ArrayList<>(puzzle.getHints().entrySet());
		//IRecipe recipe = CraftingManager.getRecipe(puzzle.getRecipe());
		
		//if(recipe == null){
		
		/*}else{
			for(int y = 0; y < 3; y++)
				for(int x = 0; x < 3; x++){
					int index = x + y * 3;
					if(recipe.getIngredients().size() > index && recipe.getIngredients().get(index).getMatchingStacks().length > 0){
						ResourceLocation name = recipe.getIngredients().get(index).getMatchingStacks()[0].getItem().getRegistryName();
						int hint = indexMatchingKey(indexHelper, name);
						if(hint != -1)
							fr().drawStringWithShadow(hintSymbols[hint % hintSymbols.length], ulX + 20 + x * 23, ulY + y * 23, hintColours[hint % hintColours.length]);
					}
				}
			
			for(int i = 0; i < indexHelper.size(); i++){
				Map.Entry<ResourceLocation, String> entry = indexHelper.get(i);
				int hintX = ulX + 23 * 3 + 15;
				int hintY = ulY + i * 12;
				fr().drawString(hintSymbols[i % hintSymbols.length] + " = " + I18n.format(entry.getValue()), hintX, hintY, hintColours[i % hintColours.length]);
			}
		}*/
	}
	
	private static int indexMatchingKey(List<Map.Entry<ResourceLocation, String>> indexHelper, ResourceLocation key){
		for(int i = 0; i < indexHelper.size(); i++)
			if(indexHelper.get(i).getKey().equals(key))
				return i;
		return -1;
	}
}