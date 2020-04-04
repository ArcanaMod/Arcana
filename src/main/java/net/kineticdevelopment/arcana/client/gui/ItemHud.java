package net.kineticdevelopment.arcana.client.gui;

import net.kineticdevelopment.arcana.common.items.ItemWand;
import net.kineticdevelopment.arcana.core.aspects.Aspect;
import net.kineticdevelopment.arcana.core.aspects.AspectHandler;
import net.kineticdevelopment.arcana.core.aspects.Aspects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Renders item HUDs. Currently, this handles the temporary wand HUD.
 */
@Mod.EventBusSubscriber
public final class ItemHud{
	
	@SubscribeEvent
	public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event){
			if(Minecraft.getMinecraft().player != null){
				if(Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() instanceof ItemWand){
					AspectHandler aspects = AspectHandler.getFrom(Minecraft.getMinecraft().player.getHeldItemMainhand());
					for(int i = 0; i < Aspects.primalAspects.length; i++){
						Aspect primal = Aspects.primalAspects[i];
						int x = 4 + 19 * i;
						int y = 4;
						if(i % 2 == 0)
							y += 5;
						Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(Aspects.getItemStackForAspect(primal), x, y);
						Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Aspects.getItemStackForAspect(primal), x - 1, y + 3, String.valueOf(aspects.getCurrentVis(primal)));
						GlStateManager.disableLighting();
					}
				}
			}
		
	}
}