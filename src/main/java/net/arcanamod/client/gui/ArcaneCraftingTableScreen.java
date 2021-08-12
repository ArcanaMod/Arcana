package net.arcanamod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.aspects.IAspectHolder;
import net.arcanamod.aspects.UndecidedAspectStack;
import net.arcanamod.client.ClientUtils;
import net.arcanamod.containers.ArcaneCraftingTableContainer;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.recipes.ArcanaRecipes;
import net.arcanamod.items.recipes.AspectCraftingInventory;
import net.arcanamod.items.recipes.IArcaneCraftingRecipe;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.arcanamod.Arcana.arcLoc;

public class ArcaneCraftingTableScreen extends ContainerScreen<ArcaneCraftingTableContainer> {
	private static final ResourceLocation BG = arcLoc("textures/gui/container/arcaneworkbench.png");
	
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
				ClientUtils.openResearchBookUI(arcLoc("arcanum"), this, null);
		return super.mouseClicked(mouseX, mouseY, buttonId);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matricies, float partialTicks, int mouseX, int mouseY){
		renderBackground(matricies);
		getMinecraft().getTextureManager().bindTexture(BG);
		ClientUiUtil.drawTexturedModalRect(matricies, guiLeft, guiTop, 0, 0, xSize, ySize);

		// draw "show Arcanum" button if player it has in inventory
		int arcanumButtonLeft = guiLeft + 158, arcanumButtonTop = guiTop + 109;
		if (isPlayerHavingArcanum()) {
			ClientUiUtil.drawTexturedModalRect(matricies, arcanumButtonLeft, arcanumButtonTop, 213, 17, 20, 20);
			if (mouseX >= arcanumButtonLeft && mouseX < arcanumButtonLeft + 20 && mouseY >= arcanumButtonTop && mouseY < arcanumButtonTop + 20)
				ClientUiUtil.drawTexturedModalRect(matricies, arcanumButtonLeft, arcanumButtonTop, 213, 38, 20, 20);
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
					float amount = stack.stack.getAmount();
					if(!stack.any){
						if(stack.stack.getAspect() == Aspects.AIR)
							ClientUiUtil.renderAspectStack(matricies, stack.stack, guiLeft + 65, guiTop + 15, colour);
						else if(stack.stack.getAspect() == Aspects.WATER)
							ClientUiUtil.renderAspectStack(matricies, stack.stack, guiLeft + 108, guiTop + 39, colour);
						else if(stack.stack.getAspect() == Aspects.FIRE)
							ClientUiUtil.renderAspectStack(matricies, stack.stack, guiLeft + 22, guiTop + 39, colour);
						else if(stack.stack.getAspect() == Aspects.EARTH)
							ClientUiUtil.renderAspectStack(matricies, stack.stack, guiLeft + 22, guiTop + 89, colour);
						else if(stack.stack.getAspect() == Aspects.ORDER)
							ClientUiUtil.renderAspectStack(matricies, stack.stack, guiLeft + 108, guiTop + 89, colour);
						else if(stack.stack.getAspect() == Aspects.CHAOS)
							ClientUiUtil.renderAspectStack(matricies, stack.stack, guiLeft + 65, guiTop + 113, colour);
					}else
						ClientUiUtil.renderAspectStack(matricies, Aspects.EXCHANGE, amount, guiLeft + 108, guiTop + 113, colour);
				}
			}else{
				// check if there's a match, but the wand isn't present
				Optional<IArcaneCraftingRecipe> optionalWithoutWand = getRecipeIgnoringWands(player.getRecipeBook().getRecipes(), container.craftMatrix, player.world);
				if(optionalWithoutWand.isPresent()){
					IArcaneCraftingRecipe possible = optionalWithoutWand.get();
					// check which aspects are missing
					for(UndecidedAspectStack stack : possible.getAspectStacks()){
						boolean satisfied = true;
						boolean anySatisfied = false;
						boolean hasAny = false;
						float amount = stack.stack.getAmount();
						IAspectHandler handler = IAspectHandler.getFrom(container.craftMatrix.getWandSlot().getStack());
						if(handler == null || handler.getHoldersAmount() == 0)
							satisfied = false;
						else for (IAspectHolder holder : handler.getHolders()){
							if (stack.any){
								hasAny = true;
								if (holder.getCurrentVis() >= stack.stack.getAmount())
									anySatisfied = true;
							}
							else if (holder.getContainedAspect() == stack.stack.getAspect()){
								if (holder.getCurrentVis() < stack.stack.getAmount())
									satisfied = false;
							}
						}
						int colour = satisfied && (!hasAny || anySatisfied) ? 0xffffff : 0xff0000;
						RenderSystem.pushMatrix();
						if(!(satisfied && (!hasAny || anySatisfied))){
							float col = (float)(Math.abs(Math.sin((player.world.getGameTime() + partialTicks) / 4d)) * .5f + .5f);
							RenderSystem.color4f(col, col, col, 1);
						}
						if(!stack.any){
							if(stack.stack.getAspect() == Aspects.AIR)
								ClientUiUtil.renderAspectStack(matricies, stack.stack, guiLeft + 65, guiTop + 15, colour);
							else if(stack.stack.getAspect() == Aspects.WATER)
								ClientUiUtil.renderAspectStack(matricies, stack.stack, guiLeft + 108, guiTop + 39, colour);
							else if(stack.stack.getAspect() == Aspects.FIRE)
								ClientUiUtil.renderAspectStack(matricies, stack.stack, guiLeft + 22, guiTop + 39, colour);
							else if(stack.stack.getAspect() == Aspects.EARTH)
								ClientUiUtil.renderAspectStack(matricies, stack.stack, guiLeft + 22, guiTop + 89, colour);
							else if(stack.stack.getAspect() == Aspects.ORDER)
								ClientUiUtil.renderAspectStack(matricies, stack.stack, guiLeft + 108, guiTop + 89, colour);
							else if(stack.stack.getAspect() == Aspects.CHAOS)
								ClientUiUtil.renderAspectStack(matricies, stack.stack, guiLeft + 65, guiTop + 113, colour);
						}else
							ClientUiUtil.renderAspectStack(matricies, Aspects.EXCHANGE, amount, guiLeft + 108, guiTop + 113, colour);
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
	
	protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY){
		font.drawString(stack, title.getString(), 10, -5, 0xA0A0A0);
	}
	
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks){
		super.render(stack, mouseX, mouseY, partialTicks);
		renderHoveredTooltip(stack, mouseX, mouseY);
	}
}