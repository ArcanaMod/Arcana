package net.kineticdevelopment.arcana.client.research.impls;

import net.kineticdevelopment.arcana.client.research.EntrySectionRenderer;
import net.kineticdevelopment.arcana.common.network.Connection;
import net.kineticdevelopment.arcana.common.network.inventory.PktGetNoteHandler;
import net.kineticdevelopment.arcana.core.research.impls.GuessworkSection;
import net.minecraft.entity.player.EntityPlayer;

public class GuessworkSectionRenderer implements EntrySectionRenderer<GuessworkSection>{
	
	public void render(GuessworkSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player){
	
	}
	
	public void renderAfter(GuessworkSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player){
	
	}
	
	public void onClick(GuessworkSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player){
		System.out.println("clicking");
		Connection.network.sendToServer(new PktGetNoteHandler.PktGetNote(section.getGuessworkId()));
	}
	
	public int span(GuessworkSection section, EntityPlayer player){
		return 1;
	}
}
