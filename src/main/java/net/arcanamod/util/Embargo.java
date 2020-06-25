package net.arcanamod.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

public class Embargo{
	@Mod.EventBusSubscriber
	public static class EmbargoEvents{
		@SubscribeEvent
		public static void DEV_WARNING(RenderGameOverlayEvent.Post event){
			if(event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE)
				if(Embargo.EmbargoSettings.enabled){
					String text0 = "You are using Arcana development build.";
					String text1 = "Please don't give this build to anyone outside the Arcana dev team.";
					if(Minecraft.getInstance().fontRenderer != null){
						Minecraft.getInstance().fontRenderer.drawString(text0, 10, 10, Color.decode("#FF4444").getRGB());
						Minecraft.getInstance().fontRenderer.drawString(text1, 10, 20, Color.decode("#FF4444").getRGB());
					}
				}
		}
	}
	
	private static class EmbargoSettings{
		public static final boolean enabled = false;
	}
}
