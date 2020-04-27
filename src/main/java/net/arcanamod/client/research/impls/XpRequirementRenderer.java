package net.arcanamod.client.research.impls;

import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.research.impls.XpRequirement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.Collections;
import java.util.List;

public class XpRequirementRenderer implements RequirementRenderer<XpRequirement>{
	
	private static final ResourceLocation EXPERIENCE_ORB_TEXTURES = new ResourceLocation("textures/entity/experience_orb.png");
	
	public void render(int x, int y, XpRequirement requirement, int ticks, float partialTicks, EntityPlayer player){
		doXPRender(ticks, x, y, partialTicks);
	}
	
	public List<String> tooltip(XpRequirement requirement, EntityPlayer player){
		return Collections.singletonList(I18n.format("requirement.experience", requirement.getAmount()));
	}
	
	public static void doXPRender(int ticks, double x, double y, float partialTicks){
		final int u = 0, v = 16;
		float f8 = (ticks + partialTicks) / 2f;
		final float i1 = (MathHelper.sin(f8 + 0.0F) + 1.0F) * 0.5F;
		final float k1 = (MathHelper.sin(f8 + 4.1887903F) + 1.0F) * 0.1F;
		GlStateManager.pushMatrix();
		GlStateManager.color(i1, 1.0F, k1, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(EXPERIENCE_ORB_TEXTURES);
		// AbstractGui.blit((int)x, (int)y, 16, 16, u, v, 16, 16, 64, 64);
		Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, u, v, 16, 16, 64, 64);
		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}
}