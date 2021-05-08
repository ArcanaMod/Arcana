package net.arcanamod.client.research.impls;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.Arcana;
import net.arcanamod.client.research.PuzzleRenderer;
import net.arcanamod.containers.slots.AspectSlot;
import net.arcanamod.systems.research.impls.Guesswork;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.arcanamod.client.gui.ClientUiUtil.drawTexturedModalRect;

public class GuessworkPuzzleRenderer extends AbstractGui implements PuzzleRenderer<Guesswork>{
	
	private static final ResourceLocation texture = new ResourceLocation(Arcana.MODID, "textures/gui/research/arcanum_gui_overlay.png");
	
	private static final String[] hintSymbols = {"#", "A", "S", "D", "F", "&", "Q", "@", "~"};
	private static final int[] hintColours = {0x8a8a8a, 0x32a852, 0x58e8da, 0x7a58e8, 0xc458e8, 0xe858d5, 0xe85858, 0x136e13, 0x6e6613, 0x99431c, 0x43355e};
	
	public void render(MatrixStack stack, Guesswork puzzle, List<AspectSlot> puzzleSlots, List<Slot> puzzleItemSlots, int screenWidth, int screenHeight, int mouseX, int mouseY, PlayerEntity player){
		drawPaper(stack, screenWidth, screenHeight);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc().getTextureManager().bindTexture(texture);
		// render result
		int rX = paperLeft(screenWidth) + 78;
		int rY = paperTop(screenHeight) + 13;
		drawTexturedModalRect(stack, rX, rY, 1, 167, 58, 20);
		int ulX = paperLeft(screenWidth) + 70;
		int ulY = paperTop(screenHeight) + 49;
		drawTexturedModalRect(stack, ulX, ulY, 145, 1, 72, 72);
		RenderHelper.enableStandardItemLighting();
		mc().getItemRenderer().renderItemAndEffectIntoGUI(player.world.getRecipeManager().getRecipe(puzzle.getRecipe()).orElse(null).getRecipeOutput(), rX + 29 - 8, rY + 10 - 8);
		
		List<Map.Entry<ResourceLocation, String>> indexHelper = new ArrayList<>(puzzle.getHints().entrySet());
		IRecipe<?> recipe = player.world.getRecipeManager().getRecipe(puzzle.getRecipe()).orElse(null);
		
		if(recipe != null){
			for(int y = 0; y < 3; y++)
				for(int x = 0; x < 3; x++){
					int index = x + y * 3;
					if(recipe.getIngredients().size() > index && recipe.getIngredients().get(index).getMatchingStacks().length > 0){
						ResourceLocation name = recipe.getIngredients().get(index).getMatchingStacks()[0].getItem().getRegistryName();
						int hint = indexMatchingKey(indexHelper, name);
						if(hint != -1)
							fr().drawStringWithShadow(stack, hintSymbols[hint % hintSymbols.length], ulX + 20 + x * 23, ulY + y * 23, hintColours[hint % hintColours.length]);
					}
				}
			
			int hintY = ulY + 101;
			int hintBaseX = ulX - 70;
			String text = "Hints: ";
			fr().drawStringWithShadow(stack, text, hintBaseX, hintY, 0x8a8a8a);
			for(int i = 0; i < indexHelper.size(); i++){
				int hintX = hintBaseX + fr().getStringWidth(text) + i * 12;
				fr().drawStringWithShadow(stack, hintSymbols[i % hintSymbols.length], hintX, hintY, hintColours[i % hintColours.length]);
			}
			for(int i = 0; i < indexHelper.size(); i++){
				Map.Entry<ResourceLocation, String> entry = indexHelper.get(i);
				int hintX = hintBaseX + fr().getStringWidth(text) + i * 12;
				if(mouseX >= hintX - 1 && mouseX < hintX + 11 && mouseY >= hintY - 1 && mouseY < hintY + 11)
					GuiUtils.drawHoveringText(stack, Lists.newArrayList(new StringTextComponent(I18n.format(entry.getValue()))), mouseX, mouseY, screenWidth, screenHeight, -1, fr());
			}
		}
	}
	
	private static int indexMatchingKey(List<Map.Entry<ResourceLocation, String>> indexHelper, ResourceLocation key){
		for(int i = 0; i < indexHelper.size(); i++)
			if(indexHelper.get(i).getKey().equals(key))
				return i;
		return -1;
	}
}