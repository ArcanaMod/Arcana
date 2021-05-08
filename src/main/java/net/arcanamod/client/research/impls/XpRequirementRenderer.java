package net.arcanamod.client.research.impls;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.systems.research.impls.XpRequirement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;
import java.util.List;

public class XpRequirementRenderer implements RequirementRenderer<XpRequirement>{
	
	private static final ResourceLocation EXPERIENCE_ORB_TEXTURES = new ResourceLocation("textures/entity/experience_orb.png");
	
	public void render(MatrixStack matrices, int x, int y, XpRequirement requirement, int ticks, float partialTicks, PlayerEntity player){
		doXPRender(matrices, ticks, x, y, partialTicks);
	}
	
	public List<ITextComponent> tooltip(XpRequirement requirement, PlayerEntity player){
		return Collections.singletonList(new TranslationTextComponent("requirement.experience", requirement.getAmount()));
	}
	
	public static void doXPRender(MatrixStack stack, int ticks, double x, double y, float partialTicks){
		final int u = 0, v = 16;
		float f8 = (ticks + partialTicks) / 2f;
		final float i1 = (MathHelper.sin(f8 + 0.0F) + 1.0F) * 0.5F;
		final float k1 = (MathHelper.sin(f8 + 4.1887903F) + 1.0F) * 0.1F;
		RenderSystem.pushMatrix();
		RenderSystem.color4f(i1, 1.0F, k1, 1.0F);
		Minecraft.getInstance().getTextureManager().bindTexture(EXPERIENCE_ORB_TEXTURES);
		AbstractGui.blit(stack, (int)x, (int)y, 16, 16, u, v, 16, 16, 64, 64);
		RenderSystem.disableBlend();
		RenderSystem.disableRescaleNormal();
		RenderSystem.popMatrix();
	}
}