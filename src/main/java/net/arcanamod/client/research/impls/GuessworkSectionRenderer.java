package net.arcanamod.client.research.impls;

import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.systems.research.impls.GuessworkSection;
import net.minecraft.entity.player.PlayerEntity;

public class GuessworkSectionRenderer implements EntrySectionRenderer<GuessworkSection>{
	
	public void render(GuessworkSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
	
	}
	
	public void renderAfter(GuessworkSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
	
	}
	
	public boolean onClick(GuessworkSection section, int pageIndex, int screenWidth, int screenHeight, double mouseX, double mouseY, boolean right, PlayerEntity player){
		//Connection.network.sendToServer(new PktGetNoteHandler.PktGetNote(section.getGuessworkId()));
		return false;
	}
	
	public int span(GuessworkSection section, PlayerEntity player){
		return 1;
	}
}
