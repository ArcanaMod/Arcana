package net.arcanamod.client.research.impls;

import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.client.gui.ClientUiUtil;
import net.arcanamod.client.gui.ResearchEntryScreen;
import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.systems.research.ResearchBook;
import net.arcanamod.systems.research.ResearchBooks;
import net.arcanamod.systems.research.impls.AbstractCraftingSection;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.arcanamod.client.gui.ClientUiUtil.drawTexturedModalRect;
import static net.arcanamod.client.gui.ResearchEntryScreen.HEIGHT_OFFSET;

public abstract class AbstractCraftingSectionRenderer<T extends AbstractCraftingSection> implements EntrySectionRenderer<T>{
	
	protected ResourceLocation textures = null;
	
	public void render(T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		// if recipe exists: render result at specified position, defer drawing recipe
		// otherwise: render error message
		ResearchBook book = ResearchBooks.getEntry(section.getEntry()).category().book();
		// don't make a new RLoc every frame
		if(textures == null || !textures.getNamespace().equals(book.getKey().getNamespace()))
			textures = new ResourceLocation(book.getKey().getNamespace(), "textures/gui/research/" + book.getPrefix() + ResearchEntryScreen.OVERLAY_SUFFIX);
		Optional<? extends IRecipe<?>> optRecipe = player.world.getRecipeManager().getRecipe(section.getRecipe());
		optRecipe.ifPresent(recipe -> {
			// draw result
			ItemStack result = recipe.getRecipeOutput();
			renderResult(right ? ResearchEntryScreen.PAGE_X + ResearchEntryScreen.RIGHT_X_OFFSET : ResearchEntryScreen.PAGE_X, resultOffset(recipe, section, pageIndex, screenWidth, screenHeight, mouseX, mouseY, right, player), result, screenWidth, screenHeight);
			renderRecipe(recipe, section, pageIndex, screenWidth, screenHeight, mouseX, mouseY, right, player);
		});
		// else display error
		if(!optRecipe.isPresent())
			error();
	}
	
	public void renderAfter(T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		Optional<? extends IRecipe<?>> optRecipe = player.world.getRecipeManager().getRecipe(section.getRecipe());
		optRecipe.ifPresent(recipe -> {
			// draw result
			ItemStack result = recipe.getRecipeOutput();
			renderResultTooltips(right ? ResearchEntryScreen.PAGE_X + ResearchEntryScreen.RIGHT_X_OFFSET : ResearchEntryScreen.PAGE_X, resultOffset(recipe, section, pageIndex, screenWidth, screenHeight, mouseX, mouseY, right, player), result, screenWidth, screenHeight, mouseX, mouseY);
			renderRecipeTooltips(recipe, section, pageIndex, screenWidth, screenHeight, mouseX, mouseY, right, player);
		});
		if(!optRecipe.isPresent())
			error();
	}
	
	abstract void renderRecipe(IRecipe<?> recipe, T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player);
	
	abstract void renderRecipeTooltips(IRecipe<?> recipe, T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player);
	
	int resultOffset(IRecipe<?> recipe, T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		return ResearchEntryScreen.PAGE_Y;
	}
	
	private void renderResult(int x, int y, ItemStack result, int screenWidth, int screenHeight){
		mc().getTextureManager().bindTexture(textures);
		int rX = x + (screenWidth - 256) / 2 + (ResearchEntryScreen.PAGE_WIDTH - 58) / 2;
		int rY = y + (screenHeight - 181) / 2 + 16 + HEIGHT_OFFSET;
		drawTexturedModalRect(rX, rY, 1, 167, 58, 20);
		item(result, rX + 29 - 8, rY + 10 - 8);
		int stX = x + (screenWidth - 256) / 2 + (ResearchEntryScreen.PAGE_WIDTH - fr().getStringWidth(result.getTextComponent().getFormattedText())) / 2 + 5;
		int stY = y + (screenHeight - 181) / 2 + 11 - fr().FONT_HEIGHT + HEIGHT_OFFSET;
		fr().drawString(result.getDisplayName().getFormattedText(), stX, stY, 0);
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		RenderSystem.enableBlend();
		RenderSystem.disableLighting();
	}
	
	protected void renderResultTooltips(int x, int y, ItemStack result, int screenWidth, int screenHeight, int mouseX, int mouseY){
		int rX = x + (screenWidth - 256) / 2 + (ResearchEntryScreen.PAGE_WIDTH - 58) / 2 + 21;
		int rY = y + (screenHeight - 181) / 2 + 18 + HEIGHT_OFFSET;
		tooltipArea(result, mouseX, mouseY, screenWidth, screenHeight, rX, rY);
	}
	
	protected void item(ItemStack stack, int x, int y){
		mc().getItemRenderer().renderItemAndEffectIntoGUI(stack, x, y);
		mc().getItemRenderer().renderItemOverlayIntoGUI(mc().fontRenderer, stack, x, y, null);
	}
	
	protected void tooltipArea(ItemStack stack, int mouseX, int mouseY, int screenWidth, int screenHeight, int areaX, int areaY){
		if(mouseX >= areaX && mouseX < areaX + 16 && mouseY >= areaY && mouseY < areaY + 16)
			tooltip(stack, mouseX, mouseY, screenWidth, screenHeight);
	}
	
	protected void aspectTooltipArea(Aspect aspect, int mouseX, int mouseY, int screenWidth, int screenHeight, int areaX, int areaY){
		if(mouseX >= areaX && mouseX < areaX + 16 && mouseY >= areaY && mouseY < areaY + 16)
			ClientUiUtil.drawAspectTooltip(aspect, mouseX, mouseY, screenWidth, screenHeight);
	}
	
	protected void tooltip(ItemStack stack, int mouseX, int mouseY, int screenWidth, int screenHeight){
		GuiUtils.drawHoveringText(stack, stack.getTooltip(mc().player, mc().gameSettings.advancedItemTooltips? ITooltipFlag.TooltipFlags.ADVANCED:ITooltipFlag.TooltipFlags.NORMAL).stream().map(ITextComponent::getFormattedText).collect(Collectors.toList()), mouseX, mouseY, screenWidth, screenHeight, -1, fr());
	}
	
	public int span(T section, PlayerEntity player){
		return 1;
	}
	
	protected void error(){
		// display error
	}
	
	protected int displayIndex(int max, @Nonnull Entity player){
		return (player.ticksExisted / 30) % max;
	}
}