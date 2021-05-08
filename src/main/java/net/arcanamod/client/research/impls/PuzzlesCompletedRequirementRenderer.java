package net.arcanamod.client.research.impls;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.Arcana;
import net.arcanamod.capabilities.Researcher;
import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.systems.research.impls.PuzzlesCompletedRequirement;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.List;

import static net.arcanamod.client.gui.ClientUiUtil.drawModalRectWithCustomSizedTexture;

public class PuzzlesCompletedRequirementRenderer implements RequirementRenderer<PuzzlesCompletedRequirement>{
	
	private static final ResourceLocation ICON = Arcana.arcLoc("textures/item/research_note_complete.png");
	
	public void render(MatrixStack matrices, int x, int y, PuzzlesCompletedRequirement requirement, int ticks, float partialTicks, PlayerEntity player){
		Minecraft.getInstance().getTextureManager().bindTexture(ICON);
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		drawModalRectWithCustomSizedTexture(matrices, x, y, 0, 0, 16, 16, 16, 16);
	}
	
	public List<ITextComponent> tooltip(PuzzlesCompletedRequirement requirement, PlayerEntity player){
		return Arrays.asList(new TranslationTextComponent("requirement.puzzles_completed", requirement.getAmount()), new TranslationTextComponent("requirement.puzzles_completed.progress", Researcher.getFrom(player).getPuzzlesCompleted(), requirement.getAmount()));
	}
}