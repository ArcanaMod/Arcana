package net.arcanamod.util;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

public class Embargo {
	public static class EmbargoEvents {
		@SubscribeEvent
		public static void DEV_WARNING(RenderGameOverlayEvent.Post event) {
			if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;
			if (Embargo.EmbargoSettings.enabled)
				if (Minecraft.getInstance()!=null) {
					EmbargoOverlay embargoOverlay = new EmbargoOverlay(Minecraft.getInstance());
				}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class EmbargoOverlay extends Screen {
		public EmbargoOverlay(Minecraft mc)
		{
			super(new StringTextComponent("embargo_overlay"));
			String text0 = "You are using Arcana development build.";
			String text1 = "Please don\'t give this build to anyone outside the Arcana dev team.";
			if (mc.fontRenderer!=null) {
				int w = Minecraft.getInstance().getMainWindow().getWidth();
				mc.fontRenderer.drawString(text0, 10, 10, Color.decode("#FF4444").getRGB());
				mc.fontRenderer.drawString(text1, 10, 20, Color.decode("#FF4444").getRGB());
			}
		}
	}

	private static class EmbargoSettings {
		public static final boolean enabled = false;
	}
}
