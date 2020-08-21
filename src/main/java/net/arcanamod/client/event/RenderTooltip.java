package net.arcanamod.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.aspects.*;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

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
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRenderTooltipPost(@Nonnull RenderTooltipEvent.PostText event){
		if(Screen.hasShiftDown() && !ItemAspectRegistry.isProcessing()){
			List<AspectStack> stacks = ItemAspectRegistry.get(event.getStack());
			if(stacks.size() > 0){
				RenderSystem.pushMatrix();
				RenderSystem.translatef(0, 0, 500);
				RenderSystem.color3f(1F, 1F, 1F);
				Minecraft mc = Minecraft.getInstance();
				RenderSystem.translatef(0F, 0F, mc.getItemRenderer().zLevel);
				
				int x = event.getX();
				int y = 10 * (event.getLines().size() - 3) + 14 + event.getY();
				for(AspectStack stack : stacks){
					UiUtil.renderAspectStack(stack, x, y);
					x += 20;
				}
				RenderSystem.popMatrix();
			}else{
				for(String line : event.getLines()){
					if(line.indexOf(((char)20)) != -1){
						RenderSystem.pushMatrix();
						RenderSystem.translatef(0, 0, 500);
						RenderSystem.color3f(1F, 1F, 1F);
						Minecraft mc = Minecraft.getInstance();
						RenderSystem.translatef(0, 0, mc.getItemRenderer().zLevel);
						
						int x = event.getX() - 3;
						int y = 10 * (event.getLines().size()) + event.getY() + 4;
						Aspect aspectFromDesc = AspectUtils.getAspectByDisplayName(line.replace(((char)20) + "", ""));
						if(aspectFromDesc != null){
							Pair<Aspect, Aspect> combinationPairs = Aspects.COMBINATIONS.inverse().get(aspectFromDesc);
							if(combinationPairs != null){
								int color = 0xa0222222;
								GuiUtils.drawGradientRect(300, x, y - 2, x + 39, y + 19, color, color);
								UiUtil.renderAspectStack(new AspectStack(combinationPairs.getFirst()), x, y);
								x += 20;
								UiUtil.renderAspectStack(new AspectStack(combinationPairs.getSecond()), x, y);
							}
						}
						RenderSystem.popMatrix();
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void makeTooltip(@Nonnull ItemTooltipEvent event){
		if(Screen.hasShiftDown() && !ItemAspectRegistry.isProcessing()){
			List<AspectStack> stacks = ItemAspectRegistry.get(event.getItemStack());
			if(stacks.size() > 0){
				// amount of spaces that need inserting
				int filler = stacks.size() * 5;
				// repeat " " *filler
				StringBuilder sb = new StringBuilder();
				for(int __ = 0; __ < filler; __++){
					String s = " ";
					sb.append(s);
				}
				String collect = sb.toString();
				event.getToolTip().add(new StringTextComponent(collect));
				event.getToolTip().add(new StringTextComponent(collect));
			}
		}
	}
}
