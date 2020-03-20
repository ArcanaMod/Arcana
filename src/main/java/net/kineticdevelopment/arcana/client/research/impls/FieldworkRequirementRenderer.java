package net.kineticdevelopment.arcana.client.research.impls;

import net.kineticdevelopment.arcana.client.research.RequirementRenderer;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.research.impls.FieldworkRequirement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.List;

import static java.util.Collections.singletonList;

public class FieldworkRequirementRenderer implements RequirementRenderer<FieldworkRequirement>{
	
	private static final ResourceLocation FIELDWORK = new ResourceLocation(Main.MODID, "textures/gui/research/fieldwork.png");
	
	public void render(int x, int y, FieldworkRequirement requirement, int ticks, float partialTicks, EntityPlayer player){
		Minecraft.getMinecraft().getTextureManager().bindTexture(FIELDWORK);
		GlStateManager.color(1f, 1f, 1f, 1f);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
	}
	
	public List<String> tooltip(FieldworkRequirement requirement, EntityPlayer player){
		// TODO: fieldwork tooltips
		return singletonList(I18n.format("requirement.fieldwork"));
	}
}
