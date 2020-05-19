package net.arcanamod.client.research.impls;

import net.arcanamod.client.gui.ResearchEntryGUI;
import net.arcanamod.research.impls.SmeltingSection;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;

import static net.arcanamod.client.gui.ResearchEntryGUI.drawTexturedModalRect;

public class SmeltingSectionRenderer extends AbstractCraftingSectionRenderer<SmeltingSection>{
	
	void renderRecipe(IRecipe<?> recipe, SmeltingSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		if(recipe instanceof AbstractCookingRecipe){
			AbstractCookingRecipe cookingRecipe = (AbstractCookingRecipe)recipe;
			int x = right ? ResearchEntryGUI.PAGE_X + ResearchEntryGUI.RIGHT_X_OFFSET : ResearchEntryGUI.PAGE_X, y = ResearchEntryGUI.PAGE_Y;
			int inputX = x + (screenWidth - 256 + ResearchEntryGUI.PAGE_WIDTH) / 2 - 8, inputY = y + (screenHeight - 181 + ResearchEntryGUI.PAGE_HEIGHT) / 2 + 8;
			mc().getTextureManager().bindTexture(textures);
			drawTexturedModalRect(inputX - 9, inputY - 9, 219, 1, 34, 48);
			ItemStack[] stacks = cookingRecipe.getIngredients().get(0).getMatchingStacks();
			item(stacks[dispIndex(stacks.length, player)], inputX, inputY);
		}else
			error();
	}
	
	void renderRecipeTooltips(IRecipe<?> recipe, SmeltingSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		if(recipe instanceof AbstractCookingRecipe){
			AbstractCookingRecipe cookingRecipe = (AbstractCookingRecipe)recipe;
			int x = right ? ResearchEntryGUI.PAGE_X + ResearchEntryGUI.RIGHT_X_OFFSET : ResearchEntryGUI.PAGE_X, y = ResearchEntryGUI.PAGE_Y;
			int inputX = x + (screenWidth - 256 + ResearchEntryGUI.PAGE_WIDTH) / 2 - 8, inputY = y + (screenHeight - 181 + ResearchEntryGUI.PAGE_HEIGHT) / 2 + 8;
			ItemStack[] stacks = cookingRecipe.getIngredients().get(0).getMatchingStacks();
			tooltipArea(stacks[dispIndex(stacks.length, player)], mouseX, mouseY, screenWidth, screenHeight, inputX, inputY);
		}
	}
}
