package net.arcanamod.client.research.impls;

import net.arcanamod.client.gui.ResearchEntryGUI;
import net.arcanamod.research.impls.CraftingSection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.crafting.IShapedRecipe;

import static net.arcanamod.client.gui.ResearchEntryGUI.drawTexturedModalRect;

public class CraftingSectionRenderer extends AbstractCraftingSectionRenderer<CraftingSection>{
	
	void renderRecipe(IRecipe<?> recipe, CraftingSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		if(recipe instanceof ICraftingRecipe){
			int x = right ? ResearchEntryGUI.PAGE_X + ResearchEntryGUI.RIGHT_X_OFFSET : ResearchEntryGUI.PAGE_X, y = ResearchEntryGUI.PAGE_Y;
			int ulX = x + (screenWidth - 256 + ResearchEntryGUI.PAGE_WIDTH) / 2 - 32, ulY = y + (screenHeight - 181 + ResearchEntryGUI.PAGE_HEIGHT) / 2 - 10;
			ICraftingRecipe craftingRecipe = (ICraftingRecipe)recipe;
			mc().getTextureManager().bindTexture(textures);
			drawTexturedModalRect(ulX - 4, ulY - 4, 145, 1, 72, 72);
			
			int width = recipe instanceof IShapedRecipe ? ((IShapedRecipe<?>)craftingRecipe).getRecipeWidth() : 3;
			int height = recipe instanceof IShapedRecipe ? ((IShapedRecipe<?>)craftingRecipe).getRecipeHeight() : 3;
			
			for(int xx = 0; xx < width; xx++)
				for(int yy = 0; yy < height; yy++){
					int index = xx + yy * width;
					if(index < recipe.getIngredients().size()){
						int itemX = ulX + xx * 24;
						int itemY = ulY + yy * 24;
						ItemStack[] stacks = recipe.getIngredients().get(index).getMatchingStacks();
						if(stacks.length > 0)
							item(stacks[dispIndex(stacks.length, player)], itemX, itemY);
					}
				}
		}else
			error();
	}
	
	void renderRecipeTooltips(IRecipe<?> recipe, CraftingSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		if(recipe instanceof ICraftingRecipe){
			int x = right ? ResearchEntryGUI.PAGE_X + ResearchEntryGUI.RIGHT_X_OFFSET : ResearchEntryGUI.PAGE_X, y = ResearchEntryGUI.PAGE_Y;
			int ulX = x + (screenWidth - 256 + ResearchEntryGUI.PAGE_WIDTH) / 2 - 32, ulY = y + (screenHeight - 181 + ResearchEntryGUI.PAGE_HEIGHT) / 2 - 10;
			ICraftingRecipe craftingRecipe = (ICraftingRecipe)recipe;
			
			int width = recipe instanceof IShapedRecipe ? ((IShapedRecipe<?>)craftingRecipe).getRecipeWidth() : 3;
			int height = recipe instanceof IShapedRecipe ? ((IShapedRecipe<?>)craftingRecipe).getRecipeHeight() : 3;
			
			for(int xx = 0; xx < width; xx++)
				for(int yy = 0; yy < height; yy++){
					int index = xx + yy * width;
					if(index < recipe.getIngredients().size()){
						int itemX = ulX + xx * 24;
						int itemY = ulY + yy * 24;
						ItemStack[] stacks = recipe.getIngredients().get(index).getMatchingStacks();
						if(stacks.length > 0)
							tooltipArea(stacks[dispIndex(stacks.length, player)], mouseX, mouseY, screenWidth, screenHeight, itemX, itemY);
					}
				}
		}
	}
}
