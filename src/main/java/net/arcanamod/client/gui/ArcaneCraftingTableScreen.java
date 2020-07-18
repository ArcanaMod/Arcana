package net.arcanamod.client.gui;

import net.arcanamod.Arcana;
import net.arcanamod.containers.ArcaneCraftingTableContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ArcaneCraftingTableScreen extends ContainerScreen<ArcaneCraftingTableContainer> {
	private static final ResourceLocation BG = new ResourceLocation(Arcana.MODID, "textures/gui/container/arcaneworkbench.png");

	public static final int WIDTH = 187;
	public static final int HEIGHT = 233;

	public ArcaneCraftingTableScreen(ArcaneCraftingTableContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		xSize = WIDTH;
		ySize = HEIGHT;
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
		renderModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, xSize, ySize, 256, 256);
	}

	public static void renderModalRectWithCustomSizedTexture(int x, int y, float texX, float texY, int width, int height, int textureWidth, int textureHeight){
		int z = Minecraft.getInstance().currentScreen != null ? Minecraft.getInstance().currentScreen.getBlitOffset() : 1;
		AbstractGui.blit(x, y, z, texX, texY, width, height, textureWidth, textureHeight);
	}
}
