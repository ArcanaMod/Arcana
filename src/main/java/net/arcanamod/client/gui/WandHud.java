package net.arcanamod.client.gui;

import net.arcanamod.Arcana;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.WandItem;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Arcana.MODID)
public final class WandHud{
	
	@SubscribeEvent
	public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event){
		if(Minecraft.getInstance().player != null && event.getType().equals(RenderGameOverlayEvent.ElementType.ALL))
			if(Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof WandItem){
				IAspectHandler aspects = IAspectHandler.getFrom(Minecraft.getInstance().player.getHeldItemMainhand());
				if(aspects != null){
					float length = AspectUtils.primalAspects.length;
					int baseX = ArcanaConfig.WAND_HUD_LEFT.get() ? 24 : event.getWindow().getScaledWidth() - 24 - 16;
					int baseY = ArcanaConfig.WAND_HUD_TOP.get() ? 24 : event.getWindow().getScaledHeight() - 24 - 16 - 4;
					for(int i = 0; i < length; i++){
						Aspect primal = AspectUtils.primalAspects[i];
						int x = (int)(baseX + Math.sin((i / length * 2) * Math.PI) * 20);
						int y = (int)(baseY + Math.cos((i / length * 2) * Math.PI) * 20);
						Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(primal), x, y);
						Minecraft.getInstance().getItemRenderer().renderItemOverlayIntoGUI(Minecraft.getInstance().fontRenderer, AspectUtils.getItemStackForAspect(primal), x - 1, y + 3, String.valueOf(aspects.findAspectInHolders(primal) == null ? -1 : aspects.findAspectInHolders(primal).getCurrentVis()));
					}
					// if a focus is present
					Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(new ItemStack(ArcanaItems.FOCUS_PARTS.get()), baseX, baseY);
				}
			}
	}
}