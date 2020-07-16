package net.arcanamod.client.gui;

import net.arcanamod.Arcana;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ArcaneCraftingTableScreen extends ContainerScreen {
	private static final ResourceLocation BG = new ResourceLocation(Arcana.MODID, "textures/gui/container/arcaneworkbench.png");

	public ArcaneCraftingTableScreen(Container screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 *
	 * @param partialTicks
	 * @param mouseX
	 * @param mouseY
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		renderBackground();
		minecraft.getTextureManager().bindTexture(BG);
	}
}
