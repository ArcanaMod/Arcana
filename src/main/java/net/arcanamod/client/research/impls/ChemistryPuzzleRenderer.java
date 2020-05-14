package net.arcanamod.client.research.impls;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.client.gui.ResearchEntryGUI;
import net.arcanamod.research.impls.Chemistry;
import net.arcanamod.Arcana;
import net.arcanamod.client.research.PuzzleRenderer;
import net.arcanamod.containers.AspectSlot;
import net.arcanamod.aspects.Aspects;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;

import java.util.List;

import static net.arcanamod.aspects.Aspects.areAspectsConnected;
import static net.arcanamod.client.gui.ResearchEntryGUI.drawTexturedModalRect;

public class ChemistryPuzzleRenderer extends AbstractGui implements PuzzleRenderer<Chemistry>{
	
	private static final ResourceLocation TEX = new ResourceLocation(Arcana.MODID, "textures/gui/research/chemistry_overlay.png");
	
	public void render(Chemistry puzzle, List<AspectSlot> puzzleSlots, List<Slot> puzzleItemSlots, int screenWidth, int screenHeight, int mouseX, int mouseY, PlayerEntity player){
		drawPaper(screenWidth, screenHeight);
		// how to generate a hexagonal grid?
		// 20x20 per hex
		// +23x, +-18y moving right
		// +11x on odd rows
		mc().getTextureManager().bindTexture(TEX);
		int gridWidth = 8, gridHeight = 6;
		for(int y = 0; y < gridHeight; y++){
			for(int x = 0; x < gridWidth; x++){
				int xx = x * 22 + (y % 2 == 0 ? 11 : 0);
				int yy = y * 19;
				int scX = xx + paperLeft(screenWidth) + (214 - (23 * gridWidth - 2)) / 2;
				int scY = yy + paperTop(screenHeight) + (134 - (19 * gridHeight + 1)) / 2;
				int index = x + y * gridWidth;
				Aspect slot = puzzle.getAspectInSlot(index);
				if(slot != null){
					mc().getItemRenderer().renderItemAndEffectIntoGUI(Aspects.getItemStackForAspect(slot), scX + 2, scY + 2);
					mc().getTextureManager().bindTexture(TEX);
				}else{
					mc().getTextureManager().bindTexture(TEX);
					drawTexturedModalRect(scX, scY, 0, 0, 20, 20);
					if(puzzleSlots != null && puzzleSlots.size() > index)
						slot = puzzleSlots.get(index).getAspect();
				}
				if(puzzleSlots != null && puzzleSlots.size() > 0){
					mc().getTextureManager().bindTexture(TEX);
					// a slot is connected to:
					//    a slot next to it (+/-1 X)
					//    a slot right below or right above it (+/-1 Y)
					//    a slot also below or also above it (+/-1 Y, - (if odd)/+1 (if even) X)
					if(x > 0 && areAspectsConnected(slot, getAt(index - 1, puzzle, puzzleSlots) /*puzzleSlots.get(index - 1).getAspect()*/)){
						// render 20,12 8x3
						drawTexturedModalRect(scX - 5, scY + 9, 20, 12, 8, 3);
					}
					if(x < gridWidth - 1 && areAspectsConnected(slot, getAt(index + 1, puzzle, puzzleSlots) /*puzzleSlots.get(index + 1).getAspect()*/)){
						// render 42,12 8x3
						drawTexturedModalRect(scX + 17, scY + 9, 42, 12, 8, 3);
					}
					if(y < gridHeight - 1){
						int belowInd = x + (y + 1) * gridWidth;
						if(y % 2 == 0){
							if(areAspectsConnected(slot, getAt(belowInd, puzzle, puzzleSlots) /*puzzleSlots.get(belowInd).getAspect()*/)){
								// render 26,0 7x7
								drawTexturedModalRect(scX + 1, scY + 16, 26, 18, 7, 7);
							}
							if(belowInd + 1 < gridWidth * gridHeight && areAspectsConnected(slot, getAt(belowInd + 1, puzzle, puzzleSlots) /*puzzleSlots.get(belowInd + 1).getAspect()*/)){
								// render 37,0 7x7
								drawTexturedModalRect(scX + 12, scY + 16, 37, 18, 7, 7);
							}
						}else{
							if(areAspectsConnected(slot, getAt(belowInd, puzzle, puzzleSlots) /*puzzleSlots.get(belowInd).getAspect()*/)){
								// render 37,0 7x7
								drawTexturedModalRect(scX + 12, scY + 16, 37, 18, 7, 7);
							}
							if(x > 0 && areAspectsConnected(slot, getAt(belowInd - 1, puzzle, puzzleSlots) /*puzzleSlots.get(belowInd - 1).getAspect()*/)){
								// render 26,0 7x7
								drawTexturedModalRect(scX + 1, scY + 16, 26, 18, 7, 7);
							}
						}
					}
					if(y > 0){
						int aboveInd = x + (y - 1) * gridWidth;
						if(y % 2 == 0){
							if(areAspectsConnected(slot, getAt(aboveInd, puzzle, puzzleSlots) /*puzzleSlots.get(aboveInd).getAspect()*/)){
								// render 26,17 7x7
								drawTexturedModalRect(scX + 1, scY - 3, 26, 0, 7, 7);
							}
							if(x > 0 && areAspectsConnected(slot, getAt(aboveInd + 1, puzzle, puzzleSlots)/*puzzleSlots.get(aboveInd + 1).getAspect())*/)){
								// render 37,17 7x7
								drawTexturedModalRect(scX + 12, scY - 3, 37, 0, 7, 7);
							}
						}else{
							if(areAspectsConnected(slot, getAt(aboveInd, puzzle, puzzleSlots) /*puzzleSlots.get(aboveInd).getAspect()*/)){
								// render 37,17 7x7
								drawTexturedModalRect(scX + 12, scY - 3, 37, 0, 7, 7);
							}
							if(x > 0 && areAspectsConnected(slot, getAt(aboveInd - 1, puzzle, puzzleSlots) /*puzzleSlots.get(aboveInd - 1).getAspect()*/)){
								// render 26,17 7x7
								drawTexturedModalRect(scX + 1, scY - 3, 26, 0, 7, 7);
							}
						}
					}
				}
			}
		}
	}
	
	private Aspect getAt(int index, Chemistry puzzle, List<AspectSlot> puzzleSlots){
		Aspect slot = puzzle.getAspectInSlot(index);
		if(slot != null)
			return slot;
		else
			return puzzleSlots.get(index).getAspect();
	}
}