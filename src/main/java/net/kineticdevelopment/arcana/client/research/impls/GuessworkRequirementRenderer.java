package net.kineticdevelopment.arcana.client.research.impls;

import net.kineticdevelopment.arcana.client.research.RequirementRenderer;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.research.impls.GuessworkRequirement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.List;

import static java.util.Collections.singletonList;

public class GuessworkRequirementRenderer implements RequirementRenderer<GuessworkRequirement>{
	
	private static final ResourceLocation INCOMPLETE_TEXTURE = new ResourceLocation(Main.MODID, "textures/item/research_note.png");
	private static final ResourceLocation COMPLETE_TEXTURE = new ResourceLocation(Main.MODID, "textures/item/research_note_complete.png");
	
	public void render(int x, int y, GuessworkRequirement requirement, int ticks, float partialTicks, EntityPlayer player){
		if(requirement.satisfied(player))
			Minecraft.getMinecraft().getTextureManager().bindTexture(COMPLETE_TEXTURE);
		else
			Minecraft.getMinecraft().getTextureManager().bindTexture(INCOMPLETE_TEXTURE);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
	}
	
	public List<String> tooltip(GuessworkRequirement requirement, EntityPlayer player){
		return singletonList(I18n.format("requirement.guesswork"));
	}
}