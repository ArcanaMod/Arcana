package net.arcanamod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.aspects.IAspectHolder;
import net.arcanamod.aspects.UndecidedAspectStack;
import net.arcanamod.containers.ArcaneCraftingTableContainer;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.inventories.AspectCraftingInventory;
import net.arcanamod.util.recipes.ArcanaRecipes;
import net.arcanamod.util.recipes.IArcaneCraftingRecipe;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.arcanamod.Arcana.arcLoc;
import static net.arcanamod.client.gui.ResearchTableScreen.renderModalRectWithCustomSizedTexture;

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
		int arcanumButtonLeft = guiLeft + 158, arcanumButtonTop = guiTop + 109;
		if (isPlayerHavingArcanum())
			if(mouseX >= arcanumButtonLeft && mouseX < arcanumButtonLeft + 20 && mouseY >= arcanumButtonTop && mouseY < arcanumButtonTop + 20)
				Arcana.proxy.openResearchBookUI(arcLoc("arcanum"));
		return super.mouseClicked(mouseX, mouseY, buttonId);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		renderBackground();
		getMinecraft().getTextureManager().bindTexture(BG);
		renderModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, xSize, ySize, 256, 256);

		// draw "show Arcanum" button if player it has in inventory
		int arcanumButtonLeft = guiLeft + 158, arcanumButtonTop = guiTop + 109;
		if (isPlayerHavingArcanum()) {
			renderModalRectWithCustomSizedTexture(arcanumButtonLeft, arcanumButtonTop, 213, 17, 20, 20, 256, 256);
			if (mouseX >= arcanumButtonLeft && mouseX < arcanumButtonLeft + 20 && mouseY >= arcanumButtonTop && mouseY < arcanumButtonTop + 20)
				renderModalRectWithCustomSizedTexture(arcanumButtonLeft, arcanumButtonTop, 213, 38, 20, 20, 256, 256);
		}

		// draw necessary aspects
		ClientPlayerEntity player = getMinecraft().player;
		if(player != null){
			Optional<IArcaneCraftingRecipe> optional = getRecipe(player.getRecipeBook().getRecipes(), container.craftMatrix, player.world);
			if(optional.isPresent()){
				IArcaneCraftingRecipe recipe = optional.get();
				// display necessary aspects
				// the wand is present
				for(UndecidedAspectStack stack : recipe.getAspectStacks()){
					int colour = 0xffffff;
					int amount = stack.stack.getAmount();
					if(!stack.any){
						if(stack.stack.getAspect() == Aspects.AIR)
							UiUtil.renderAspectStack(stack.stack, guiLeft + 65, guiTop + 15, colour);
						else if(stack.stack.getAspect() == Aspects.WATER)
							UiUtil.renderAspectStack(stack.stack, guiLeft + 108, guiTop + 39, colour);
						else if(stack.stack.getAspect() == Aspects.FIRE)
							UiUtil.renderAspectStack(stack.stack, guiLeft + 22, guiTop + 39, colour);
						else if(stack.stack.getAspect() == Aspects.EARTH)
							UiUtil.renderAspectStack(stack.stack, guiLeft + 22, guiTop + 89, colour);
						else if(stack.stack.getAspect() == Aspects.ORDER)
							UiUtil.renderAspectStack(stack.stack, guiLeft + 108, guiTop + 89, colour);
						else if(stack.stack.getAspect() == Aspects.CHAOS)
							UiUtil.renderAspectStack(stack.stack, guiLeft + 65, guiTop + 117, colour);
					}else
						UiUtil.renderAspectStack(Aspects.EXCHANGE, amount, guiLeft + 65, guiTop + 117, colour);
				}
			}else{
				// check if there's a match, but the wand isn't present
				Optional<IArcaneCraftingRecipe> optionalWithoutWand = getRecipeIgnoringWands(player.getRecipeBook().getRecipes(), container.craftMatrix, player.world);
				if(optionalWithoutWand.isPresent()){
					IArcaneCraftingRecipe possible = optionalWithoutWand.get();
					// check which aspects are missing
					for(UndecidedAspectStack stack : possible.getAspectStacks()){
						boolean satisfied = true;
						int amount = stack.stack.getAmount();
						IAspectHandler handler = IAspectHandler.getFrom(container.craftMatrix.getWandSlot().getStack());
						if(handler == null || handler.getHoldersAmount() == 0)
							satisfied = false;
						else
							// TODO: add "any" aspect support
							for(IAspectHolder holder : handler.getHolders())
								if(holder.getContainedAspect() == stack.stack.getAspect())
									if(holder.getCurrentVis() < stack.stack.getAmount())
										satisfied = false;
						int colour = satisfied ? 0xffffff : 0xff0000;
						RenderSystem.pushMatrix();
						if(!satisfied){
							float col = (float)(Math.abs(Math.sin((player.world.getGameTime() + partialTicks) / 4d)) * .5f + .5f);
							RenderSystem.color4f(col, col, col, 1);
						}
						if(!stack.any){
							if(stack.stack.getAspect() == Aspects.AIR)
								UiUtil.renderAspectStack(stack.stack, guiLeft + 65, guiTop + 15, colour);
							else if(stack.stack.getAspect() == Aspects.WATER)
								UiUtil.renderAspectStack(stack.stack, guiLeft + 108, guiTop + 39, colour);
							else if(stack.stack.getAspect() == Aspects.FIRE)
								UiUtil.renderAspectStack(stack.stack, guiLeft + 22, guiTop + 39, colour);
							else if(stack.stack.getAspect() == Aspects.EARTH)
								UiUtil.renderAspectStack(stack.stack, guiLeft + 22, guiTop + 89, colour);
							else if(stack.stack.getAspect() == Aspects.ORDER)
								UiUtil.renderAspectStack(stack.stack, guiLeft + 108, guiTop + 89, colour);
							else if(stack.stack.getAspect() == Aspects.CHAOS)
								UiUtil.renderAspectStack(stack.stack, guiLeft + 65, guiTop + 117, colour);
						}else
							UiUtil.renderAspectStack(Aspects.EXCHANGE, amount, guiLeft + 65, guiTop + 117, colour);
						RenderSystem.popMatrix();
					}
				}
			}
		}
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
}