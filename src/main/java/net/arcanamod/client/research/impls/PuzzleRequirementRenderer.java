package net.arcanamod.client.research.impls;

import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.research.Puzzle;
import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.impls.Fieldwork;
import net.arcanamod.research.impls.PuzzleRequirement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PuzzleRequirementRenderer implements RequirementRenderer<PuzzleRequirement>{
	
	public void render(int x, int y, PuzzleRequirement requirement, int ticks, float partialTicks, PlayerEntity player){
		ResourceLocation icon = getFrom(requirement).getIcon();
		Minecraft.getMinecraft().getTextureManager().bindTexture(icon != null ? icon : getFrom(requirement).getDefaultIcon());
		GlStateManager.color(1f, 1f, 1f, 1f);
		AbstractGui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
	}
	
	public List<String> tooltip(PuzzleRequirement requirement, PlayerEntity player){
		if(!(getFrom(requirement) instanceof Fieldwork)){
			String desc = getFrom(requirement).getDesc();
			return Arrays.asList((desc != null ? desc : getFrom(requirement).getDefaultDesc()), "requirement.puzzle.get_note.1", "requirement.puzzle.get_note.2");
		}else
			return Collections.singletonList(getFrom(requirement).getDesc());
	}
	
	private Puzzle getFrom(PuzzleRequirement pr){
		return ResearchBooks.puzzles.get(pr.getPuzzleId());
	}
}