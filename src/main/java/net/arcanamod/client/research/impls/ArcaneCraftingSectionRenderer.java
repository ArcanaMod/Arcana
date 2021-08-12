package net.arcanamod.client.research.impls;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.UndecidedAspectStack;
import net.arcanamod.client.gui.ClientUiUtil;
import net.arcanamod.client.gui.ResearchEntryScreen;
import net.arcanamod.items.recipes.IArcaneCraftingRecipe;
import net.arcanamod.systems.research.impls.ArcaneCraftingSection;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.crafting.IShapedRecipe;

import java.util.Collections;

import static net.arcanamod.client.gui.ClientUiUtil.drawTexturedModalRect;
import static net.arcanamod.client.gui.ResearchEntryScreen.HEIGHT_OFFSET;

public class ArcaneCraftingSectionRenderer extends AbstractCraftingSectionRenderer<ArcaneCraftingSection>{
	
	void renderRecipe(MatrixStack matrices, IRecipe<?> recipe, ArcaneCraftingSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		if(recipe instanceof IArcaneCraftingRecipe){
			IArcaneCraftingRecipe craftingRecipe = (IArcaneCraftingRecipe)recipe;
			int x = right ? ResearchEntryScreen.PAGE_X + ResearchEntryScreen.RIGHT_X_OFFSET : ResearchEntryScreen.PAGE_X, y = ResearchEntryScreen.PAGE_Y;
			int ulX = x + (screenWidth - 256 + ResearchEntryScreen.PAGE_WIDTH) / 2 - 32;
			int ulY = y + (screenHeight - 181 + ResearchEntryScreen.PAGE_HEIGHT) / 2 - 10 + HEIGHT_OFFSET;
			if(craftingRecipe.getAspectStacks().length > 0)
				ulY -= 15;
			mc().getTextureManager().bindTexture(textures);
			drawTexturedModalRect(matrices, ulX - 10, ulY - 10, 73, 75, 84, 84);
			
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
							item(stacks[displayIndex(stacks.length, player)], itemX, itemY);
					}
				}
			// Display aspects
			UndecidedAspectStack[] stacks = craftingRecipe.getAspectStacks();
			// 1 aspect -> 0, 2-3 aspects -> 3 spacing, 4-5 aspects -> 2 spacing, 6 aspects -> 1 spacing
			int spacing = (stacks.length == 1) ? 0 : (stacks.length >= 6) ? 1 : (stacks.length < 4) ? 3 : 2;
			int aspectX = ulX + 73 / 2 - (craftingRecipe.getAspectStacks().length * (16 + spacing * 2)) / 2 - 4;
			int aspectY = ulY + 82;
			// Shadow behind the aspects for readability
			for(int i = 0, length = stacks.length; i < length; i++){
				UndecidedAspectStack stack = stacks[i];
				Aspect display = stack.any ? Aspects.EXCHANGE : stack.stack.getAspect();
				float amount = stack.stack.getAmount();
				ClientUiUtil.renderAspectStack(matrices, display, amount, aspectX + i * (16 + 2 * spacing) + spacing, aspectY);
			}
		}else
			error();
	}
	
	void renderRecipeTooltips(MatrixStack matrices, IRecipe<?> recipe, ArcaneCraftingSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		if(recipe instanceof IArcaneCraftingRecipe){
			IArcaneCraftingRecipe craftingRecipe = (IArcaneCraftingRecipe)recipe;
			int x = right ? ResearchEntryScreen.PAGE_X + ResearchEntryScreen.RIGHT_X_OFFSET : ResearchEntryScreen.PAGE_X, y = ResearchEntryScreen.PAGE_Y;
			int ulX = x + (screenWidth - 256 + ResearchEntryScreen.PAGE_WIDTH) / 2 - 32;
			int ulY = y + (screenHeight - 181 + ResearchEntryScreen.PAGE_HEIGHT) / 2 - 10 + HEIGHT_OFFSET;
			if(craftingRecipe.getAspectStacks().length > 0)
				ulY -= 15;
			
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
							tooltipArea(matrices, stacks[displayIndex(stacks.length, player)], mouseX, mouseY, screenWidth, screenHeight, itemX, itemY);
					}
				}
			
			// Display aspect tooltips
			UndecidedAspectStack[] stacks = craftingRecipe.getAspectStacks();
			int spacing = (stacks.length == 1) ? 0 : (stacks.length >= 6) ? 1 : (stacks.length < 4) ? 3 : 2;
			int aspectX = ulX + 73 / 2 - (craftingRecipe.getAspectStacks().length * (16 + spacing * 2)) / 2 - 4;
			int aspectY = ulY + 82;
			for(int i = 0, length = stacks.length; i < length; i++){
				UndecidedAspectStack stack = stacks[i];
				String displayed = stack.any ? I18n.format("aspect.any") : I18n.format("aspect." + stack.stack.getAspect().name().toLowerCase());
				int areaX = aspectX + i * (16 + 2 * spacing) + spacing;
				if(mouseX >= areaX && mouseX < areaX + 16 && mouseY >= aspectY && mouseY < aspectY + 16)
					ClientUiUtil.drawAspectStyleTooltip(matrices, Collections.singletonList(new StringTextComponent(displayed)), mouseX, mouseY, screenWidth, screenHeight);
			}
		}
	}
}
