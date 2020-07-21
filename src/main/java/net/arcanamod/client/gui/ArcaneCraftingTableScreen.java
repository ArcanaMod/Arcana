package net.arcanamod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.*;
import net.arcanamod.containers.ArcaneCraftingTableContainer;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.inventories.AspectCraftingInventory;
import net.arcanamod.util.recipes.ArcanaRecipes;
import net.arcanamod.util.recipes.IArcaneCraftingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.arcanamod.Arcana.MODID;

public class ArcaneCraftingTableScreen extends ContainerScreen<ArcaneCraftingTableContainer>{
	private static final ResourceLocation BG = new ResourceLocation(Arcana.MODID, "textures/gui/container/arcaneworkbench.png");
	
	public static final int WIDTH = 187;
	public static final int HEIGHT = 233;
	
	public ArcaneCraftingTableScreen(ArcaneCraftingTableContainer screenContainer, PlayerInventory inv, ITextComponent title){
		super(screenContainer, inv, title);
		xSize = WIDTH;
		ySize = HEIGHT;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int buttonId) {
		// if player clicked "show Arcanum" button and open Arcanum gui. If player doesn't have in inventory ignore.
		int arcanum_button_left = guiLeft+158, arcanum_button_top = guiTop+109;
		if (isPlayerHavingArcanum())
			if (mouseX >= arcanum_button_left && mouseX < arcanum_button_left+20 && mouseY >= arcanum_button_top && mouseY < arcanum_button_top+20)
				Arcana.proxy.openResearchBookUI(new ResourceLocation(MODID, "arcanum"));
		return super.mouseClicked(mouseX, mouseY, buttonId);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		renderBackground();
		getMinecraft().getTextureManager().bindTexture(BG);
		renderModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, xSize, ySize, 256, 256);

		// draw "show Arcanum" button if player it has in inventory
		int arcanum_button_left = guiLeft+158, arcanum_button_top = guiTop+109;
		if (isPlayerHavingArcanum()) {
			renderModalRectWithCustomSizedTexture(arcanum_button_left, arcanum_button_top, 193, 17, 20, 20, 256, 256);
			if (mouseX >= arcanum_button_left && mouseX < arcanum_button_left + 20 && mouseY >= arcanum_button_top && mouseY < arcanum_button_top + 20)
				renderModalRectWithCustomSizedTexture(arcanum_button_left, arcanum_button_top, 193, 38, 20, 20, 256, 256);
		}

		// draw necessary aspects
		ClientPlayerEntity player = getMinecraft().player;
		Optional<IArcaneCraftingRecipe> optional = getRecipe(player.getRecipeBook().getRecipes(), container.craftMatrix, player.world);
		if(optional.isPresent()){
			IArcaneCraftingRecipe recipe = optional.get();
			// display necessary aspects
			// the wand is present
			for(UndecidedAspectStack stack : recipe.getAspectStacks()){
				int colour = 0xffffff;
				if(!stack.any){
					if(stack.stack.getAspect() == Aspects.AIR){
						getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(Aspects.AIR), guiLeft + 65, guiTop + 14);
						renderAspectOverlay(guiLeft + 65, guiTop + 14, Aspects.AIR, colour);
					}else if(stack.stack.getAspect() == Aspects.WATER){
						getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(Aspects.WATER), guiLeft + 108, guiTop + 38);
						renderAspectOverlay(guiLeft + 108, guiTop + 38, Aspects.WATER, colour);
					}else if(stack.stack.getAspect() == Aspects.FIRE){
						getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(Aspects.FIRE), guiLeft + 22, guiTop + 38);
						renderAspectOverlay(guiLeft + 22, guiTop + 38, Aspects.FIRE, colour);
					}else if(stack.stack.getAspect() == Aspects.EARTH){
						getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(Aspects.EARTH), guiLeft + 22, guiTop + 88);
						renderAspectOverlay(guiLeft + 22, guiTop + 88, Aspects.EARTH, colour);
					}else if(stack.stack.getAspect() == Aspects.ORDER){
						getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(Aspects.ORDER), guiLeft + 108, guiTop + 88);
						renderAspectOverlay(guiLeft + 108, guiTop + 88, Aspects.ORDER, colour);
					}else if(stack.stack.getAspect() == Aspects.CHAOS){
						getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(Aspects.CHAOS), guiLeft + 65, guiTop + 112);
						renderAspectOverlay(guiLeft + 65, guiTop + 116, Aspects.CHAOS, colour);
					}
				}else{
					getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(Aspects.EXCHANGE), guiLeft + 11, guiTop + 64);
					renderAspectOverlay(guiLeft + 11, guiTop + 64, Aspects.EXCHANGE, colour);
				}
			}
		}else{
			// check if there's a match, but the wand isn't present
			Optional<IArcaneCraftingRecipe> optionalWithoutWand = getRecipeIgnoringWands(player.getRecipeBook().getRecipes(), container.craftMatrix, player.world);
			if(optionalWithoutWand.isPresent()){
				IArcaneCraftingRecipe possible = optionalWithoutWand.get();
				// check which aspects are missing
				// TODO: display them flashing
				// the others are solid
				// modifying item rendering is hard so I'll just change text colour for now
				// in the future, make a util method to render an aspect's texture without going through the item renderer
				for(UndecidedAspectStack stack : possible.getAspectStacks()){
					int colour = 0xffffff;
					IAspectHandler handler = IAspectHandler.getFrom(container.craftMatrix.getWandSlot().getStack());
					if(handler == null || handler.getHoldersAmount() == 0)
						colour = 0xff0000;
					else
						// TODO: add "any" aspect support
						for(IAspectHolder holder : handler.getHolders())
							if(holder.getContainedAspect() == stack.stack.getAspect())
								if(holder.getCurrentVis() < stack.stack.getAmount())
									colour = 0xff0000;
					if(!stack.any){
						if(stack.stack.getAspect() == Aspects.AIR){
							getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(Aspects.AIR), guiLeft + 65, guiTop + 14);
							renderAspectOverlay(guiLeft + 65, guiTop + 14, Aspects.AIR, colour);
						}else if(stack.stack.getAspect() == Aspects.WATER){
							getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(Aspects.WATER), guiLeft + 108, guiTop + 38);
							renderAspectOverlay(guiLeft + 108, guiTop + 38, Aspects.WATER, colour);
						}else if(stack.stack.getAspect() == Aspects.FIRE){
							getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(Aspects.FIRE), guiLeft + 22, guiTop + 38);
							renderAspectOverlay(guiLeft + 22, guiTop + 38, Aspects.FIRE, colour);
						}else if(stack.stack.getAspect() == Aspects.EARTH){
							getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(Aspects.EARTH), guiLeft + 22, guiTop + 88);
							renderAspectOverlay(guiLeft + 22, guiTop + 88, Aspects.EARTH, colour);
						}else if(stack.stack.getAspect() == Aspects.ORDER){
							getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(Aspects.ORDER), guiLeft + 108, guiTop + 88);
							renderAspectOverlay(guiLeft + 108, guiTop + 88, Aspects.ORDER, colour);
						}else if(stack.stack.getAspect() == Aspects.CHAOS){
							getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(Aspects.CHAOS), guiLeft + 65, guiTop + 112);
							renderAspectOverlay(guiLeft + 65, guiTop + 116, Aspects.CHAOS, colour);
						}
					}else{
						getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(Aspects.EXCHANGE), guiLeft + 11, guiTop + 64);
						renderAspectOverlay(guiLeft + 11, guiTop + 64, Aspects.EXCHANGE, colour);
					}
				}
			}
		}
	}
	
	protected void renderAspectOverlay(int x, int y, Aspect aspect, int colour){
		ItemStack stack = AspectUtils.getItemStackForAspect(aspect);
		MatrixStack matrixstack = new MatrixStack();
		String s = String.valueOf(stack.getCount());
		matrixstack.translate(0, 0, getMinecraft().getItemRenderer().zLevel + 200.0F);
		IRenderTypeBuffer.Impl impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
		getMinecraft().fontRenderer.renderString(s, x - 1 - getMinecraft().fontRenderer.getStringWidth(s), y + 3, colour, true, matrixstack.getLast().getMatrix(), impl, false, 0, 0xf000f0);
		impl.finish();
	}
	
	protected Optional<IArcaneCraftingRecipe> getRecipe(List<RecipeList> recipeList, AspectCraftingInventory inventory, World world){
		return recipeList.stream()
				.flatMap(list -> list.getRecipes().stream())
				.filter(x -> x.getType() == ArcanaRecipes.Types.ARCANE_CRAFTING_SHAPED)
				.map(IArcaneCraftingRecipe.class::cast)
				.flatMap(recipe -> Util.streamOptional(recipe.matches(inventory, world) ? of(recipe) : empty()))
				.findFirst();
	}
	
	protected Optional<IArcaneCraftingRecipe> getRecipeIgnoringWands(List<RecipeList> recipeList, AspectCraftingInventory inventory, World world){
		return recipeList.stream()
				.flatMap(list -> list.getRecipes().stream())
				.filter(x -> x.getType() == ArcanaRecipes.Types.ARCANE_CRAFTING_SHAPED)
				.map(IArcaneCraftingRecipe.class::cast)
				.flatMap(recipe -> Util.streamOptional(recipe.matchesIgnoringAspects(inventory, world) ? of(recipe) : empty()))
				.findFirst();
	}
	private boolean isPlayerHavingArcanum() {
		return container.playerInventory.hasItemStack(new ItemStack(ArcanaItems.ARCANUM.get()));
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		font.drawString(title.getFormattedText(), 10, -5, 0xA0A0A0);
	}
	
	public void render(int mouseX, int mouseY, float partialTicks){
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	public static void renderModalRectWithCustomSizedTexture(int x, int y, float texX, float texY, int width, int height, int textureWidth, int textureHeight){
		int z = Minecraft.getInstance().currentScreen != null ? Minecraft.getInstance().currentScreen.getBlitOffset() : 1;
		AbstractGui.blit(x, y, z, texX, texY, width, height, textureWidth, textureHeight);
	}
}