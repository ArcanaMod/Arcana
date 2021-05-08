package net.arcanamod.client.research.impls;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.client.gui.ResearchEntryScreen;
import net.arcanamod.systems.research.impls.SmeltingSection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;

import static net.arcanamod.client.gui.ClientUiUtil.drawTexturedModalRect;
import static net.arcanamod.client.gui.ResearchEntryScreen.HEIGHT_OFFSET;

public class SmeltingSectionRenderer extends AbstractCraftingSectionRenderer<SmeltingSection>{
	
	void renderRecipe(MatrixStack matrices, IRecipe<?> recipe, SmeltingSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		if(recipe instanceof AbstractCookingRecipe){
			AbstractCookingRecipe cookingRecipe = (AbstractCookingRecipe)recipe;
			int x = right ? ResearchEntryScreen.PAGE_X + ResearchEntryScreen.RIGHT_X_OFFSET : ResearchEntryScreen.PAGE_X, y = ResearchEntryScreen.PAGE_Y;
			int inputX = x + (screenWidth - 256 + ResearchEntryScreen.PAGE_WIDTH) / 2 - 8, inputY = y + (screenHeight - 181 + ResearchEntryScreen.PAGE_HEIGHT) / 2 + 8 + HEIGHT_OFFSET;
			mc().getTextureManager().bindTexture(textures);
			drawTexturedModalRect(matrices, inputX - 9, inputY - 9, 219, 1, 34, 48);
			ItemStack[] stacks = cookingRecipe.getIngredients().get(0).getMatchingStacks();
			item(stacks[displayIndex(stacks.length, player)], inputX, inputY);
		}else
			error();
	}
	
	void renderRecipeTooltips(MatrixStack matrices, IRecipe<?> recipe, SmeltingSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		if(recipe instanceof AbstractCookingRecipe){
			AbstractCookingRecipe cookingRecipe = (AbstractCookingRecipe)recipe;
			int x = right ? ResearchEntryScreen.PAGE_X + ResearchEntryScreen.RIGHT_X_OFFSET : ResearchEntryScreen.PAGE_X, y = ResearchEntryScreen.PAGE_Y;
			int inputX = x + (screenWidth - 256 + ResearchEntryScreen.PAGE_WIDTH) / 2 - 8, inputY = y + (screenHeight - 181 + ResearchEntryScreen.PAGE_HEIGHT) / 2 + 8 + HEIGHT_OFFSET;
			ItemStack[] stacks = cookingRecipe.getIngredients().get(0).getMatchingStacks();
			tooltipArea(matrices, stacks[displayIndex(stacks.length, player)], mouseX, mouseY, screenWidth, screenHeight, inputX, inputY);
		}
	}
}
