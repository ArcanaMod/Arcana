package net.arcanamod.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.ItemAspectRegistry;
import net.arcanamod.client.gui.UiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
				int y = 10 * (event.getLines().size() - 3) + 14 + event.getY();//shiftTextByLines(event.getLines(), event.getY() + 13);
				for(AspectStack stack : stacks){
					UiUtil.renderAspectStack(stack, x, y);
					/*ItemStack aspect = AspectUtils.getItemStackForAspect(stack.getAspect());
					mc.getItemRenderer().renderItemAndEffectIntoGUI(aspect, x, y);
					// render text
					MatrixStack matrixstack = new MatrixStack();
					String s = String.valueOf(stack.getAmount());
					matrixstack.translate(0, 0, mc.getItemRenderer().zLevel + 200.0F);
					IRenderTypeBuffer.Impl impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
					mc.fontRenderer.renderString(s, x + 19 - mc.fontRenderer.getStringWidth(s), y + 10, blendToWhite(stack.getAspect().getColorRange().get()[3]), true, matrixstack.getLast().getMatrix(), impl, false, 0, 0xf000f0);
					impl.finish();*/
					x += 20;
				}
				RenderSystem.popMatrix();
			}
		}
	}
	
	public static int blendToWhite(int a){
		int aR = (a & 0xff0000) >> 16;
		int aG = (a & 0xff00) >> 8;
		int aB = a & 0xff;
		int finR = (int)(aR * .5f + 127.5f);
		int finG = (int)(aG * .5f + 127.5f);
		int finB = (int)(aB * .5f + 127.5f);
		return finR << 16 | finG << 8 | finB;
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
