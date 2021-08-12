package net.arcanamod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.systems.research.ResearchEntry;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CompletePuzzleToast implements IToast {
	// good thing we don't have any other research books that work }:D
	private static final ItemStack ICON = new ItemStack(ArcanaItems.ARCANUM.get());
	
	private ResearchEntry entry;
	
	public CompletePuzzleToast(@Nullable ResearchEntry entry){
		this.entry = entry;
	}
	
	// draw
	public Visibility func_230444_a_(MatrixStack matrixStack, ToastGui toastGui, long delta){
		toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
		RenderSystem.color3f(1, 1, 1);
		toastGui.blit(matrixStack, 0, 0, 0, 32, 160, 32);
		// Puzzle Complete!
		// <Research Name>
		boolean present = entry != null;
		toastGui.getMinecraft().fontRenderer.drawString(matrixStack, I18n.format("puzzle.toast.title"), 30, present ? 7 : 12, 0xff500050);
		if(present)
			toastGui.getMinecraft().fontRenderer.drawString(matrixStack, I18n.format(entry.name()), 30, 18, 0xff000000);
		toastGui.getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(null, ICON, 8, 8);
		return delta >= 5000 ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
	}
}