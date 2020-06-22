package net.arcanamod.client.event;

import net.arcanamod.client.render.JarTileEntityRender;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.Core;
import net.arcanamod.items.attachment.Focus;
import net.arcanamod.world.NodeType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.awt.*;

@Mod.EventBusSubscriber
public class RenderTooltip {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRenderToolTip(@Nonnull RenderTooltipEvent.Color event) {
		for (String line : event.getLines()) {
			if (line.indexOf(((char) 20))!=-1) {
				event.setBorderStart(new Color(0, 80, 95).getRGB());
				event.setBorderEnd(new Color(0,40, 47).getRGB());
			}
		}
	}
}
