package net.kineticdevelopment.arcana.client.research.impls;

import net.kineticdevelopment.arcana.client.research.ClientBooks;
import net.kineticdevelopment.arcana.client.research.RequirementRenderer;
import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.kineticdevelopment.arcana.core.research.impls.PuzzleRequirement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Collections;
import java.util.List;

public class PuzzleRequirementRenderer implements RequirementRenderer<PuzzleRequirement>{
	
	public void render(int x, int y, PuzzleRequirement requirement, int ticks, float partialTicks, EntityPlayer player){
		Minecraft.getMinecraft().getTextureManager().bindTexture(getFrom(requirement).getIcon());
		GlStateManager.color(1f, 1f, 1f, 1f);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
	}
	
	public List<String> tooltip(PuzzleRequirement requirement, EntityPlayer player){
		return Collections.singletonList(getFrom(requirement).getDesc());
	}
	
	private Puzzle getFrom(PuzzleRequirement pr){
		return ClientBooks.puzzles.get(pr.getPuzzleId());
	}
}