package net.arcanamod.client.event;

import net.arcanamod.aspects.ItemAspectRegistry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.awt.*;

@Mod.EventBusSubscriber
public class RenderTooltip{
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRenderTooltipColor(@Nonnull RenderTooltipEvent.Color event){
		for(String line : event.getLines()){
			if(line.indexOf(((char)20)) != -1){
				event.setBorderStart(new Color(0, 80, 95).getRGB());
				event.setBorderEnd(new Color(0, 40, 47).getRGB());
			}
		}
	}
	
	@SubscribeEvent
	public static void onRenderTooltip(@Nonnull ItemTooltipEvent event){
		event.getToolTip().add(new StringTextComponent(ItemAspectRegistry.get(event.getItemStack()).toString()));
	}
}
