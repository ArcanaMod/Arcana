package net.arcanamod.client.research.impls;

import net.arcanamod.client.gui.ResearchEntryGUI;
import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.research.ResearchBook;
import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.impls.RecipeSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"ConstantConditions", "SameParameterValue"})
public class RecipeSectionRenderer extends Gui implements EntrySectionRenderer<RecipeSection>{
	
	// don't want to be making an ItemStack every frame...
	private static Map<ResourceLocation, ItemStack> ITEM_CACHE = new HashMap<>();
	static private final int slotColor = 0x80ffffff; // white tint
	
	private ResourceLocation textures;
	
	public void render(RecipeSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player){
		ResearchBook book = ResearchBooks.getEntry(section.getEntry()).category().book();
		textures = new ResourceLocation(book.getKey().getResourceDomain(), "textures/gui/research/" + book.getPrefix() + ResearchEntryGUI.OVERLAY_SUFFIX);
		
		mc().getTextureManager().bindTexture(textures);
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.enableDepth();
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.disableLighting();
		
		if(section.getRecipe().getResourceDomain().equals("__furnace__")){
			String[] parts = section.getRecipe().getResourcePath().split("\\.");
			ResourceLocation input = new ResourceLocation(parts[0].replace("-", ":"));
			ResourceLocation output = new ResourceLocation(parts[1].replace("-", ":"));
			ItemStack inStack = ITEM_CACHE.computeIfAbsent(input, location -> new ItemStack(ForgeRegistries.ITEMS.getValue(location)));
			ItemStack outStack = ITEM_CACHE.computeIfAbsent(output, location -> new ItemStack(ForgeRegistries.ITEMS.getValue(location)));
			renderSmeltingRecipe(right ? ResearchEntryGUI.PAGE_X + ResearchEntryGUI.RIGHT_X_OFFSET : ResearchEntryGUI.PAGE_X, ResearchEntryGUI.PAGE_Y, inStack, outStack, screenWidth, screenHeight);
		}else{
			IRecipe recipe = CraftingManager.getRecipe(section.getRecipe());
			renderCraftingRecipe(right ? ResearchEntryGUI.PAGE_X + ResearchEntryGUI.RIGHT_X_OFFSET : ResearchEntryGUI.PAGE_X, ResearchEntryGUI.PAGE_Y, recipe, screenWidth, screenHeight);
		}
	}
	
	private void renderCraftingRecipe(int x, int y, IRecipe recipe, int screenWidth, int screenHeight){
		renderResult(recipe.getRecipeOutput().getDisplayName(), x, y, recipe.getRecipeOutput(), screenWidth, screenHeight);
		
		int ulX = x + (screenWidth - 256 + ResearchEntryGUI.PAGE_WIDTH) / 2 - 32, ulY = y + (screenHeight - 181 + ResearchEntryGUI.PAGE_HEIGHT) / 2 - 10;
		mc().getTextureManager().bindTexture(textures);
		drawTexturedModalRect(ulX - 4, ulY - 4, 145, 1, 72, 72);
		
		int width = recipe instanceof IShapedRecipe ? ((IShapedRecipe)recipe).getRecipeWidth() : 3;
		int height = recipe instanceof IShapedRecipe ? ((IShapedRecipe)recipe).getRecipeHeight() : 3;
		
		for(int xx = 0; xx < width; xx++)
			for(int yy = 0; yy < height; yy++){
				int index = xx + yy * width;
				if(index < recipe.getIngredients().size()){
					int itemX = ulX + xx * 24;
					int itemY = ulY + yy * 24;
					ItemStack[] stacks = recipe.getIngredients().get(index).getMatchingStacks();
					if(stacks.length > 0)
						mc().getRenderItem().renderItemAndEffectIntoGUI(stacks[dispIndex(stacks.length)], itemX, itemY);
				}
			}
	}
	
	private void renderSmeltingRecipe(int x, int y, ItemStack input, ItemStack output, int screenWidth, int screenHeight){
		renderResult(output.getDisplayName(), x, y, output, screenWidth, screenHeight);
		
		int inputX = x + (screenWidth - 256 + ResearchEntryGUI.PAGE_WIDTH) / 2 - 8, inputY = y + (screenHeight - 181 + ResearchEntryGUI.PAGE_HEIGHT) / 2 + 8;
		mc().getTextureManager().bindTexture(textures);
		drawTexturedModalRect(inputX - 9, inputY - 9, 219, 1, 34, 48);
		Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(input, inputX, inputY);
	}
	
	private void renderResult(String name, int x, int y, ItemStack result, int screenWidth, int screenHeight){
		// render result
		// texture is @ 1, 167 & has a size of 58x20
		int rX = x + (screenWidth - 256) / 2 + (ResearchEntryGUI.PAGE_WIDTH - 58) / 2;
		int rY = y + (screenHeight - 181) / 2 + 16;
		drawTexturedModalRect(rX, rY, 1, 167, 58, 20);
		mc().getRenderItem().renderItemAndEffectIntoGUI(result, rX + 29 - 8, rY + 10 - 8);
		int stX = x + (screenWidth - 256) / 2 + (ResearchEntryGUI.PAGE_WIDTH - fr().getStringWidth(name)) / 2;
		int stY = y + (screenHeight - 181) / 2 + 11 - fr().FONT_HEIGHT;
		fr().drawString(name, stX, stY, 0);
		GlStateManager.color(1f, 1f, 1f, 1f);
		//GlStateManager.enableBlend();
		//GlStateManager.disableLighting();
	}
	
	public void renderAfter(RecipeSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player){
		// tooltips
		if(section.getRecipe().getResourceDomain().equals("__furnace__")){
			String[] parts = section.getRecipe().getResourcePath().split("\\.");
			ResourceLocation input = new ResourceLocation(parts[0].replace("-", ":"));
			ResourceLocation output = new ResourceLocation(parts[1].replace("-", ":"));
			// coooooould be replaced by get(), but why would I
			ItemStack inStack = ITEM_CACHE.computeIfAbsent(input, location -> new ItemStack(ForgeRegistries.ITEMS.getValue(location)));
			ItemStack outStack = ITEM_CACHE.computeIfAbsent(output, location -> new ItemStack(ForgeRegistries.ITEMS.getValue(location)));
			renderSmeltingRecipeTooltips(right ? ResearchEntryGUI.PAGE_X + ResearchEntryGUI.RIGHT_X_OFFSET : ResearchEntryGUI.PAGE_X, ResearchEntryGUI.PAGE_Y, inStack, outStack, screenWidth, screenHeight, mouseX, mouseY);
		}else{
			IRecipe recipe = CraftingManager.getRecipe(section.getRecipe());
			renderCraftingRecipeTooltips(right ? ResearchEntryGUI.PAGE_X + ResearchEntryGUI.RIGHT_X_OFFSET : ResearchEntryGUI.PAGE_X, ResearchEntryGUI.PAGE_Y, recipe, screenWidth, screenHeight, mouseX, mouseY);
		}
	}
	
	private void renderCraftingRecipeTooltips(int x, int y, IRecipe recipe, int screenWidth, int screenHeight, int mouseX, int mouseY){
		// Check result
		int rX = x + (screenWidth - 256) / 2 + 44;
		int rY = y + (screenHeight - 181) / 2 + 18;
		if(mouseX >= rX && mouseX <= rX + 16 && mouseY >= rY && mouseY <= rY + 16){
			GuiUtils.drawGradientRect(300, rX, rY, rX + 16, rY + 16, slotColor, slotColor);
			GuiUtils.drawHoveringText(recipe.getRecipeOutput(), getTooltipFromItem(recipe.getRecipeOutput()), mouseX, mouseY, screenWidth, screenHeight, -1, mc().fontRenderer);
			GlStateManager.disableLighting();
		}else{
			int ulX = x + (screenWidth - 256 + ResearchEntryGUI.PAGE_WIDTH) / 2 - 32, ulY = y + (screenHeight - 181 + ResearchEntryGUI.PAGE_HEIGHT) / 2 - 10;
			int width = recipe instanceof IShapedRecipe ? ((IShapedRecipe)recipe).getRecipeWidth() : 3;
			int height = recipe instanceof IShapedRecipe ? ((IShapedRecipe)recipe).getRecipeHeight() : 3;
			if(mouseX >= ulX && mouseX <= ulX + 24 * width && mouseY >= ulY && mouseY <= ulY + 24 * height){
				// get the item
				if((mouseX - ulX) % 24 < 16 && (mouseY - ulY) % 24 <= 16){
					int xx = (mouseX - ulX) / 24;
					int yy = (mouseY - ulY) / 24;
					int index = xx + width * yy;
					if(recipe.getIngredients().size() > index){
						ItemStack[] stacks = recipe.getIngredients().get(index).getMatchingStacks();
						if(stacks.length > 0){
							// display hover
							GuiUtils.drawGradientRect(300, ulX + xx * 24, ulY + yy * 24, ulX + xx * 24 + 16, ulY + yy * 24 + 16, slotColor, slotColor);
							// display tooltip
							GuiUtils.drawHoveringText(stacks[dispIndex(stacks.length)], getTooltipFromItem(stacks[dispIndex(stacks.length)]), mouseX, mouseY, screenWidth, screenHeight, -1, mc().fontRenderer);
							GlStateManager.disableLighting();
						}
					}
				}
			}
		}
	}
	
	private void renderSmeltingRecipeTooltips(int x, int y, ItemStack input, ItemStack output, int screenWidth, int screenHeight, int mouseX, int mouseY){
		// Check result
		int rX = x + (screenWidth - 256) / 2 + 44;
		int rY = y + (screenHeight - 181) / 2 + 18;
		if(mouseX >= rX && mouseX <= rX + 16 && mouseY >= rY && mouseY <= rY + 16){
			GuiUtils.drawGradientRect(299, rX, rY, rX + 16, rY + 16, slotColor, slotColor);
			GuiUtils.drawHoveringText(output, getTooltipFromItem(output), mouseX, mouseY, screenWidth, screenHeight, -1, mc().fontRenderer);
			GlStateManager.disableLighting();
		}
		// Check input
		else{
			int inputX = x + (screenWidth - 256 + ResearchEntryGUI.PAGE_WIDTH) / 2 - 8, inputY = y + (screenHeight - 181 + ResearchEntryGUI.PAGE_HEIGHT) / 2 + 8;
			if(mouseX >= inputX && mouseX <= inputX + 16 && mouseY >= inputY && mouseY <= inputY + 16){
				GuiUtils.drawGradientRect(299, inputX, inputY, inputX + 16, inputY + 16, slotColor, slotColor);
				GuiUtils.drawHoveringText(input, getTooltipFromItem(input), mouseX, mouseY, screenWidth, screenHeight, -1, mc().fontRenderer);
				GlStateManager.disableLighting();
			}
		}
	}
	
	private int dispIndex(int max){
		return (mc().player.ticksExisted / 30) % max;
	}
	
	private List<String> getTooltipFromItem(ItemStack item){
		return item.getTooltip(mc().player, mc().gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
	}
	
	public int span(RecipeSection section, EntityPlayer player){
		return 1;
	}
}