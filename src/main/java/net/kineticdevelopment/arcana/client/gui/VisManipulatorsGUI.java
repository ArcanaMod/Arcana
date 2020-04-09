package net.kineticdevelopment.arcana.client.gui;

import net.kineticdevelopment.arcana.common.containers.AspectContainer;
import net.kineticdevelopment.arcana.common.network.Connection;
import net.kineticdevelopment.arcana.common.network.inventory.PktRequestAspectSync;
import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class VisManipulatorsGUI extends GuiAspectContainer{
	
	public static final int WIDTH = 176;
	public static final int HEIGHT = 256;
	
	private static final ResourceLocation bg = new ResourceLocation(Main.MODID, "textures/gui/container/vis_manipulators_temp.png");
	
	public VisManipulatorsGUI(AspectContainer inventorySlots){
		super(inventorySlots);
		xSize = WIDTH;
		ySize = HEIGHT;
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		mc.getTextureManager().bindTexture(bg);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		String s = I18n.format("item.vis_manipulation_tools.name");
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 0x404040);
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	public void initGui(){
		super.initGui();
		Connection.network.sendToServer(new PktRequestAspectSync());
	}
}