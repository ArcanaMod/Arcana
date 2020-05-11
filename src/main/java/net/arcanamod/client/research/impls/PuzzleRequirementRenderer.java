package net.arcanamod.client.research.impls;

import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.client.gui.ResearchEntryGUI;
import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.research.Puzzle;
import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.impls.Fieldwork;
import net.arcanamod.research.impls.PuzzleRequirement;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PuzzleRequirementRenderer implements RequirementRenderer<PuzzleRequirement>{
	
	public void render(int x, int y, PuzzleRequirement requirement, int ticks, float partialTicks, PlayerEntity player){
		ResourceLocation icon = getFrom(requirement).getIcon();
		Minecraft.getInstance().getTextureManager().bindTexture(icon != null ? icon : getFrom(requirement).getDefaultIcon());
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		ResearchEntryGUI.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
	}
	
	public List<ITextComponent> tooltip(PuzzleRequirement requirement, PlayerEntity player){
		if(!(getFrom(requirement) instanceof Fieldwork)){
			String desc = getFrom(requirement).getDesc();
			return Arrays.asList(new TranslationTextComponent(desc != null ? desc : getFrom(requirement).getDefaultDesc()), new TranslationTextComponent("requirement.puzzle.get_note.1"), new TranslationTextComponent("requirement.puzzle.get_note.2"));
		}else
			return Collections.singletonList(new StringTextComponent(getFrom(requirement).getDesc()));
	}
	
	private Puzzle getFrom(PuzzleRequirement pr){
		return ResearchBooks.puzzles.get(pr.getPuzzleId());
	}
}