package net.arcanamod.client.event;

import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.ItemAspectRegistry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

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
		if(!ItemAspectRegistry.isProcessing()){
			List<AspectStack> stacks = ItemAspectRegistry.get(event.getItemStack());
			// TODO: replace with something better
			if(stacks.size() > 0)
				event.getToolTip().add(new StringTextComponent(stacks.stream().map(stack -> stack.getAmount() + "x " + stack.getAspect().name()).collect(Collectors.joining(", "))));
		}
	}
}
