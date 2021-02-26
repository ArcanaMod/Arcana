package net.arcanamod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.Collections;

import static java.lang.Math.max;
import static java.lang.Math.min;

public final class UiUtil{
	
	private UiUtil(){
	}
	
	public static int blend(int a, int b, float progress){
		int aR = red(a);
		int aG = green(a);
		int aB = blue(a);
		int bR = red(b);
		int bG = green(b);
		int bB = blue(b);
		float inv = 1 - progress;
		int finR = (int)(aR * progress + bR * inv);
		int finG = (int)(aG * progress + bG * inv);
		int finB = (int)(aB * progress + bB * inv);
		return combine(finR, finG, finB);
	}
	
	public static int invert(int colour){
		int red = red(colour);
		int green = green(colour);
		int blue = blue(colour);
		int newRed = 255 - red;
		int newGreen = 255 - green;
		int newBlue = 255 - blue;
		return combine(newRed, newGreen, newBlue);
	}
	
	public static int red(int colour){
		return (colour & 0xff0000) >> 16;
	}
	
	public static int green(int colour){
		return (colour & 0xff00) >> 8;
	}
	
	public static int blue(int colour){
		return colour & 0xff;
	}
	
	public static int combine(int red, int green, int blue){
		return red << 16 | green << 8 | blue;
	}
	
	// Adapted from Color#HSBtoRGB
	public static int hsbToRgb(int hsb){
		float hue = red(hsb) / 255f, saturation = green(hsb) / 255f, brightness = blue(hsb) / 255f;
		int r = 0, g = 0, b = 0;
		if(saturation == 0)
			r = g = b = (int)(brightness * 255f + .5f);
		else{
			float h = (hue - (float)Math.floor(hue)) * 6f;
			float f = h - (float)Math.floor(h);
			float p = brightness * (1 - saturation);
			float q = brightness * (1 - saturation * f);
			float t = brightness * (1 - (saturation * (1 - f)));
			switch((int)h){
				case 0:
					r = (int)(brightness * 255f + .5f);
					g = (int)(t * 255 + .5);
					b = (int)(p * 255 + .5);
					break;
				case 1:
					r = (int)(q * 255 + .5);
					g = (int)(brightness * 255f + .5);
					b = (int)(p * 255 + .5);
					break;
				case 2:
					r = (int)(p * 255 + .5);
					g = (int)(brightness * 255f + .5);
					b = (int)(t * 255 + .5);
					break;
				case 3:
					r = (int)(p * 255 + .5);
					g = (int)(q * 255 + .5);
					b = (int)(brightness * 255f + .5);
					break;
				case 4:
					r = (int)(t * 255 + .5);
					g = (int)(p * 255 + .5);
					b = (int)(brightness * 255f + .5);
					break;
				case 5:
					r = (int)(brightness * 255f + .5);
					g = (int)(p * 255 + .5);
					b = (int)(q * 255 + .5);
					break;
			}
		}
		return combine(r, g, b);
	}
	
	public static int rgbToHsb(int color){
		return rgbToHsb(red(color), green(color), blue(color));
	}
	
	// Adapted from Color#RGBtoHSB
	public static int rgbToHsb(int red, int green, int blue){
		float hue, saturation, brightness;
		int cMax = max(red, green);
		if(blue > cMax)
			cMax = blue;
		int cMin = min(red, green);
		if(blue < cMin)
			cMin = blue;
		
		brightness = (float)cMax;
		if(cMax != 0)
			saturation = (cMax - cMin) / (float)(cMax);
		else
			saturation = 0;
		if(saturation == 0)
			hue = 0;
		else{
			float redC = (float)(cMax - red) / (float)(cMax - cMin);
			float greenC = (float)(cMax - green) / (float)(cMax - cMin);
			float blueC = (float)(cMax - blue) / (float)(cMax - cMin);
			if(red == cMax)
				hue = blueC - greenC;
			else if(green == cMax)
				hue = 2 + redC - blueC;
			else
				hue = 4 + greenC - redC;
			hue = hue / 6f;
			if(hue < 0)
				hue++;
		}
		return combine((int)(hue * 255), (int)(saturation * 255), (int)brightness);
	}
	
	public static int tooltipColour(Aspect aspect){
		// hueshift by 30, increase brightness by 120, reduce saturation by 20
		int hsb = rgbToHsb(aspect.getColorRange().get(1));
		return hsbToRgb(combine((red(hsb) + 30) % 256, max(green(hsb) - 20, 0), min(blue(hsb) + 120, 255)));
	}
	
	public static void renderAspectStack(AspectStack stack, int x, int y){
		renderAspectStack(stack, x, y, tooltipColour(stack.getAspect()));
	}
	
	public static void renderAspectStack(AspectStack stack, int x, int y, int colour){
		renderAspectStack(stack.getAspect(), stack.getAmount(), x, y, colour);
	}
	
	public static void renderAspectStack(Aspect aspect, int amount, int x, int y){
		renderAspectStack(aspect, amount, x, y, tooltipColour(aspect));
	}
	
	public static void renderAspectStack(Aspect aspect, int amount, int x, int y, int colour){
		Minecraft mc = Minecraft.getInstance();
		// render aspect
		renderAspect(aspect, x, y);
		// render amount
		MatrixStack matrixstack = new MatrixStack();
		String s = String.valueOf(amount);
		matrixstack.translate(0, 0, mc.getItemRenderer().zLevel + 200.0F);
		IRenderTypeBuffer.Impl impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
		mc.fontRenderer.renderString(s, x + 19 - mc.fontRenderer.getStringWidth(s), y + 10, colour, true, matrixstack.getLast().getMatrix(), impl, false, 0, 0xf000f0);
		impl.finish();
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

	public static void drawModalRectWithScaledTexture(int x, int y, int width, int height, float texX, float texY, int textureWidth, int textureHeight){
		int z = Minecraft.getInstance().currentScreen != null ? Minecraft.getInstance().currentScreen.getBlitOffset() : 1;
		AbstractGui.blit(x, y, z, texX, texY, width, height, textureWidth, textureHeight);
	}
	
	public static void drawTexturedModalRect(int x, int y, float texX, float texY, int width, int height){
		drawModalRectWithCustomSizedTexture(x, y, texX, texY, width, height, 256, 256);
	}
	
	public static boolean shouldShowAspectIngredients(){
		// TODO: research gating?
		return true;
	}
	
	public static void drawAspectTooltip(Aspect aspect, int mouseX, int mouseY, int screenWidth, int screenHeight){
		String name = AspectUtils.getLocalizedAspectDisplayName(aspect);
		drawAspectStyleTooltip(name, mouseX, mouseY, screenWidth, screenHeight);
		
		if(shouldShowAspectIngredients() && Screen.hasShiftDown()){
			RenderSystem.pushMatrix();
			RenderSystem.translatef(0, 0, 500);
			RenderSystem.color3f(1, 1, 1);
			Minecraft mc = Minecraft.getInstance();
			RenderSystem.translatef(0, 0, mc.getItemRenderer().zLevel);
			
			// copied from GuiUtils#drawHoveringText but without text wrapping
			FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
			int tooltipTextWidth = fontRenderer.getStringWidth(name);
			int tooltipX = mouseX + 12;
			if(tooltipX + tooltipTextWidth + 4 > screenWidth)
				tooltipX = mouseX - 16 - tooltipTextWidth;
			int tooltipY = mouseY - 12;
			if (tooltipY < 4)
				tooltipY = 4;
			else if (tooltipY + 12 > screenHeight)
				tooltipY = screenHeight - 12;
			
			int x = tooltipX - 4;
			int y = 10 + tooltipY + 5;
			Pair<Aspect, Aspect> combinationPairs = Aspects.COMBINATIONS.inverse().get(aspect);
			if(combinationPairs != null){
				int color = 0xa0222222;
				// 2px padding horizontally, 2px padding vertically
				GuiUtils.drawGradientRect(0, x, y - 2, x + 40, y + 18, color, color);
				x += 2;
				UiUtil.renderAspect(combinationPairs.getFirst(), x, y);
				x += 20;
				UiUtil.renderAspect(combinationPairs.getSecond(), x, y);
			}
			RenderSystem.popMatrix();
		}
	}
	
	public static void drawAspectStyleTooltip(String text, int mouseX, int mouseY, int screenWidth, int screenHeight){
		GuiUtils.drawHoveringText(Collections.singletonList(text), mouseX, mouseY, screenWidth, screenHeight, -1, GuiUtils.DEFAULT_BACKGROUND_COLOR, 0xFF00505F, 0xFF00282F, Minecraft.getInstance().fontRenderer);
	}
}