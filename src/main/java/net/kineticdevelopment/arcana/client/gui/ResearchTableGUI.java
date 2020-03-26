package net.kineticdevelopment.arcana.client.gui;

import net.kineticdevelopment.arcana.common.objects.tile.ResearchTableTileEntity;
import net.minecraft.client.gui.GuiScreen;

public class ResearchTableGUI extends GuiScreen{
	
	ResearchTableTileEntity te;
	
	public ResearchTableGUI(ResearchTableTileEntity te){
		this.te = te;
	}
}