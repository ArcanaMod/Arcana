package net.kineticdevelopment.arcana.client.gui;

import net.kineticdevelopment.arcana.common.objects.containers.ResearchTableContainer;
import net.kineticdevelopment.arcana.common.objects.tile.ResearchTableTileEntity;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

public class ResearchTableGUI extends GuiContainer{
	
	ResearchTableTileEntity te;
	
	public ResearchTableGUI(ResearchTableTileEntity te, ResearchTableContainer container){
		super(container);
		this.te = te;
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		drawDefaultBackground();
	}
	
	public boolean doesGuiPauseGame(){
		return false;
	}
}