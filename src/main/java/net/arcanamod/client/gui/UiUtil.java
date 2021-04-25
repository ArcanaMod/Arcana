package net.arcanamod.client.gui;

import net.arcanamod.aspects.Aspect;

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
	
	
}