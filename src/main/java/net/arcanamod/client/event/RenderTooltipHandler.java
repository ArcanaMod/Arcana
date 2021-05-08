package net.arcanamod.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.ItemAspectRegistry;
import net.arcanamod.client.gui.ClientUiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class RenderTooltipHandler {
	
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
					ClientUiUtil.renderAspectStack(event.getMatrixStack(), stack, x, y);
					x += 20;
				}
				RenderSystem.popMatrix();
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
