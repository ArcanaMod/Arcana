package net.arcanamod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.AspectUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;

public final class UiUtil{
	
	private UiUtil(){}
	
	public static int blend(int a, int b, float progress){
		int aR = (a & 0xff0000) >> 16;
		int aG = (a & 0xff00) >> 8;
		int aB = a & 0xff;
		int bR = (b & 0xff0000) >> 16;
		int bG = (b & 0xff00) >> 8;
		int bB = b & 0xff;
		float inv = 1 - progress;
		int finR = (int)(aR * progress + bR * inv);
		int finG = (int)(aG * progress + bG * inv);
		int finB = (int)(aB * progress + bB * inv);
		return finR << 16 | finG << 8 | finB;
	}
	
	public static int invert(int colour){
		int red = (colour & 0xff0000) >> 16;
		int green = (colour & 0xff00) >> 8;
		int blue = colour & 0xff;
		int newRed = 255 - red;
		int newGreen = 255 - green;
		int newBlue = 255 - blue;
		return newRed << 16 | newGreen << 8 | newBlue;
	}
	
	public static void renderAspectStack(AspectStack stack, int x, int y){
		renderAspectStack(stack, x, y, blend(0xffffff, invert(stack.getAspect().getColorRange().get(1)), .5f));
	}
	
	public static void renderAspectStack(AspectStack stack, int x, int y, int colour){
		Minecraft mc = Minecraft.getInstance();
		// render aspect
		renderAspect(stack.getAspect(), x, y);
		// render amount
		if(stack.getAmount() > 1){
			MatrixStack matrixstack = new MatrixStack();
			String s = String.valueOf(stack.getAmount());
			matrixstack.translate(0, 0, mc.getItemRenderer().zLevel + 200.0F);
			IRenderTypeBuffer.Impl impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
			mc.fontRenderer.renderString(s, x + 19 - mc.fontRenderer.getStringWidth(s), y + 10, colour, true, matrixstack.getLast().getMatrix(), impl, false, 0, 0xf000f0);
			impl.finish();
		}
	}
	
	public static void renderAspect(Aspect aspect, int x, int y){
		Minecraft mc = Minecraft.getInstance();
		mc.getTextureManager().bindTexture(AspectUtils.getAspectTextureLocation(aspect));
		drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
	}
	
	public static void drawModalRectWithCustomSizedTexture(int x, int y, float texX, float texY, int width, int height, int textureWidth, int textureHeight){
		int z = Minecraft.getInstance().currentScreen != null ? Minecraft.getInstance().currentScreen.getBlitOffset() : 1;
		AbstractGui.blit(x, y, z, texX, texY, width, height, textureWidth, textureHeight);
	}
	
	public static void drawTexturedModalRect(int x, int y, float texX, float texY, int width, int height){
		drawModalRectWithCustomSizedTexture(x, y, texX, texY, width, height, 256, 256);
	}
}