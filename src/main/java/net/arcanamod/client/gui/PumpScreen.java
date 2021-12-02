package net.arcanamod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.containers.PumpContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.arcanamod.Arcana.arcLoc;
import static net.arcanamod.client.gui.ClientUiUtil.drawTexturedModalRect;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PumpScreen extends ContainerScreen<PumpContainer>{
	
	private static final ResourceLocation BG = arcLoc("textures/gui/container/aspect_pump.png");
	
	public PumpScreen(PumpContainer screenContainer, PlayerInventory inv, ITextComponent title){
		super(screenContainer, inv, title);
	}
	
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrices, float partialTicks, int x, int y){
		renderBackground(matrices);
		getMinecraft().getTextureManager().bindTexture(BG);
		drawTexturedModalRect(matrices, guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks){
		super.render(matrices, mouseX, mouseY, partialTicks);
		renderHoveredTooltip(matrices, mouseX, mouseY);
	}
	
	protected void drawGuiContainerForegroundLayer(MatrixStack matricies, int mouseX, int mouseY){
		String s = title.getString();
		font.drawString(matricies, s, (float)(xSize / 2 - font.getStringWidth(s) / 2), 6, 0x404040);
		font.drawString(matricies, playerInventory.getDisplayName().getString(), 8, (float)(ySize - 96 + 2), 0x404040);
	}
}
