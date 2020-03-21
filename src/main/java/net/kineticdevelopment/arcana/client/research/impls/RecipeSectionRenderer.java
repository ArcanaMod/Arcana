package net.kineticdevelopment.arcana.client.research.impls;

import net.kineticdevelopment.arcana.client.research.ClientBooks;
import net.kineticdevelopment.arcana.client.research.EntrySectionRenderer;
import net.kineticdevelopment.arcana.core.research.ResearchBook;
import net.kineticdevelopment.arcana.core.research.impls.RecipeSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

import static net.kineticdevelopment.arcana.client.gui.ResearchEntryGUI.*;

@SuppressWarnings({"ConstantConditions", "SameParameterValue"})
public class RecipeSectionRenderer extends Gui implements EntrySectionRenderer<RecipeSection>{
	
	// don't want to be making an ItemStack every frame...
	private static Map<ResourceLocation, ItemStack> ITEM_CACHE = new HashMap<>();
	private ResourceLocation textures;
	
	public void render(RecipeSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player){
		ResearchBook book = ClientBooks.getEntry(section.getEntry()).category().getBook();
		textures = new ResourceLocation(book.getKey().getResourceDomain(), "textures/gui/research/" + book.getPrefix() + OVERLAY_SUFFIX);
		
		mc().getTextureManager().bindTexture(textures);
		GlStateManager.disableLighting();
		GlStateManager.color(1f, 1f, 1f, 1f);
		/*
		 now, to complain about 1.12 again
		 in 1.14+, all recipes (furnace, crafting, smoking, blasting, loom-ing, and eventually our own) are all IRecipe<?>s
		 and can all be accessed in the same way (mc.player.connection.getRecipeManager().getRecipe(x).orElse(...)//ifPresent(...))
		 HERE, THOUGH
		 crafting recipes are easier: CraftingManager...
		 but smelting is FurnaceRecipes...
		 and those don't have IDs
		 and (AFAIK) we can't easily use JSONs for our own recipes without basically rewriting the system (think ResearchLoader, but worse)
		 so that will need to be handled probably though some convoluted code
		 
		 for now, I'll say that recipes with the "arcana" domain & with a path beginning with "__furnace__" are furnace recipes
		 and are in the format "input.output", where input is the id of the input item w/ ":" replaced with "-", and output is similar
		 like "arcana:__furnace__minecraft-coal.minecraft-coal_block" for coal -> coal block (makes no sense but whatever)
		 I know I can make it look better by not using ResourceLocation, but I can't bother tbh
		 TODO: READ ABOVE & DOCUMENT
		*/
		
		if(section.getRecipe().getResourceDomain().equals("arcana") && section.getRecipe().getResourcePath().startsWith("__furnace__")){
			String base = section.getRecipe().getResourcePath().substring(11);
			String[] parts = base.split("\\.");
			ResourceLocation input = new ResourceLocation(parts[0].replace("-", ":"));
			ResourceLocation output = new ResourceLocation(parts[1].replace("-", ":"));
			ItemStack inStack = ITEM_CACHE.computeIfAbsent(input, location -> new ItemStack(ForgeRegistries.ITEMS.getValue(location)));
			ItemStack outStack = ITEM_CACHE.computeIfAbsent(output, location -> new ItemStack(ForgeRegistries.ITEMS.getValue(location)));
			renderSmeltingRecipe(right? PAGE_X + RIGHT_X_OFFSET : PAGE_X, PAGE_Y, inStack, outStack, screenWidth, screenHeight);
		}else{
			IRecipe recipe = CraftingManager.getRecipe(section.getRecipe());
			renderCraftingRecipe(right? PAGE_X + RIGHT_X_OFFSET : PAGE_X, PAGE_Y, recipe, screenWidth, screenHeight);
		}
	}
	
	private void renderCraftingRecipe(int x, int y, IRecipe recipe, int screenWidth, int screenHeight){
		renderResult(x, y, recipe.getRecipeOutput(), screenWidth, screenHeight);
		
		int ulX = x + (screenWidth - 256 + PAGE_WIDTH) / 2 - 32, ulY = y + (screenHeight - 181 + PAGE_HEIGHT) / 2 + 8;
		mc().getTextureManager().bindTexture(textures);
		drawTexturedModalRect(ulX - 4, ulY - 4, 145, 1, 72, 72);
		
		for(int xx = 0; xx < 3; xx++)
			for(int yy = 0; yy < 3; yy++){
				int index = xx + yy * 3;
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
		renderResult(x, y, output, screenWidth, screenHeight);
		
		int inputX = x + (screenWidth - 256 + PAGE_WIDTH) / 2 - 8, inputY = y + (screenHeight - 181 + PAGE_HEIGHT) / 2 - 8;
		mc().getTextureManager().bindTexture(textures);
		drawTexturedModalRect(inputX - 9, inputY - 9, 219, 1, 34, 48);
		Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(input, inputX, inputY);
	}
	
	private void renderResult(int x, int y, ItemStack result, int screenWidth, int screenHeight){
		// render result
		// texture is @ 1, 167 & has a size of 58x20
		int rX = x + (screenWidth - 256) / 2 + (PAGE_WIDTH - 58) / 2, rY = y + (screenHeight - 181) / 2 + 10;
		drawTexturedModalRect(rX, rY, 1, 167, 58, 20);
		Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(result, rX + 29 - 8, rY + 10 - 8);
	}
	
	public void renderAfter(RecipeSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player){
		// tooltips
	}
	
	private int dispIndex(int max){
		return (mc().player.ticksExisted / 30) % max;
	}
	
	public int span(RecipeSection section, EntityPlayer player){
		return 1;
	}
}