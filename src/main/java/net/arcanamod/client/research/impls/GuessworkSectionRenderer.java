package net.arcanamod.client.research.impls;

import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.research.impls.GuessworkSection;
import net.minecraft.entity.player.PlayerEntity;

public class GuessworkSectionRenderer implements EntrySectionRenderer<GuessworkSection>{
	
	public void render(GuessworkSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
	
	}
	
	public void renderAfter(GuessworkSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
	
	}
	
	public void onClick(GuessworkSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		System.out.println("clicking");
		//Connection.network.sendToServer(new PktGetNoteHandler.PktGetNote(section.getGuessworkId()));
	}
	
	public int span(GuessworkSection section, PlayerEntity player){
		return 1;
	}
}
