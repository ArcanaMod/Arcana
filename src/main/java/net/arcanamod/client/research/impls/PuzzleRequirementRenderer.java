package net.arcanamod.client.research.impls;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.systems.research.Puzzle;
import net.arcanamod.systems.research.ResearchBooks;
import net.arcanamod.systems.research.impls.Fieldwork;
import net.arcanamod.systems.research.impls.PuzzleRequirement;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.arcanamod.client.gui.ClientUiUtil.drawModalRectWithCustomSizedTexture;

public class PuzzleRequirementRenderer implements RequirementRenderer<PuzzleRequirement>{
	
	public void render(MatrixStack matrices, int x, int y, PuzzleRequirement requirement, int ticks, float partialTicks, PlayerEntity player){
		ResourceLocation icon = getFrom(requirement).getIcon();
		Minecraft.getInstance().getTextureManager().bindTexture(icon != null ? icon : getFrom(requirement).getDefaultIcon());
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		drawModalRectWithCustomSizedTexture(matrices, x, y, 0, 0, 16, 16, 16, 16);
	}
	
	public List<ITextComponent> tooltip(PuzzleRequirement requirement, PlayerEntity player){
		if(!(getFrom(requirement) instanceof Fieldwork)){
			String desc = getFrom(requirement).getDesc();
			String puzzleDesc = desc != null ? desc : getFrom(requirement).getDefaultDesc();
			if(requirement.satisfied(player))
				return Arrays.asList(new TranslationTextComponent(puzzleDesc), new TranslationTextComponent("requirement.puzzle.complete"));
			return Arrays.asList(new TranslationTextComponent(puzzleDesc), new TranslationTextComponent("requirement.puzzle.get_note.1"), new TranslationTextComponent("requirement.puzzle.get_note.2"));
		}else
			return Collections.singletonList(new TranslationTextComponent(getFrom(requirement).getDesc()));
	}
	
	private Puzzle getFrom(PuzzleRequirement pr){
		return ResearchBooks.puzzles.get(pr.getPuzzleId());
	}
}