package net.arcanamod.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.client.event.InitScreenHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.arcanamod.client.gui.ResearchEntryScreen.TEXT_SCALING;

public class Embargo{
	
	@Mod.EventBusSubscriber
	public static class EmbargoEvents{
		@SubscribeEvent
		public static void DEV_WARNING(RenderGameOverlayEvent.Post event){
			if(event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE)
				if(InitScreenHandler.DEBUG_MODE){
					String text0 = "You are using Arcana development build.";
					String text1 = "Please don't give this build to anyone outside the Arcana dev team.";
					if(Minecraft.getInstance().fontRenderer != null){
						RenderSystem.scalef(TEXT_SCALING, TEXT_SCALING, 1f);
						Minecraft.getInstance().fontRenderer.drawString(text0, 10, 10, 0xFFFF4444);
						Minecraft.getInstance().fontRenderer.drawString(text1, 10, 20, 0xFFFF4444);
						RenderSystem.scalef(1f / TEXT_SCALING, 1f / TEXT_SCALING, 1f);
					}
				}
		}
	}
}
