package net.arcanamod.client.research;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.systems.research.BackgroundLayer;
import net.arcanamod.systems.research.impls.ImageLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static net.arcanamod.client.gui.ClientUiUtil.drawModalRectWithCustomSizedTexture;
import static net.arcanamod.client.gui.ResearchBookScreen.MAX_PAN;

public class BackgroundLayerRenderers{
	
	public static final Map<ResourceLocation, BackgroundLayerRenderer<?>> RENDERERS = new HashMap<>();
	
	public static void init(){
		RENDERERS.put(ImageLayer.TYPE, (ImageLayer layer, MatrixStack stack, int x, int y, int width, int height, float xPan, float yPan, float parallax, float xOff, float yOff, float zoom) -> {
			float parallax1 = parallax * 2 * layer.speed();
			Minecraft.getInstance().getTextureManager().bindTexture(layer.image);
			if(layer.vanishZoom() == -1 || layer.vanishZoom() > zoom)
				drawModalRectWithCustomSizedTexture(stack, x, y, (-xPan + MAX_PAN) / parallax1 + xOff, (yPan + MAX_PAN) / parallax1 + yOff, width, height, 512, 512);
		});
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void render(BackgroundLayer layer, MatrixStack stack, int x, int y, int width, int height, float xPan, float yPan, float parallax, float xOff, float yOff, float zoom){
		BackgroundLayerRenderer renderer = RENDERERS.get(layer.type());
		renderer.render(layer, stack, x, y, width, height, xPan, yPan, parallax, xOff, yOff, zoom);
	}
	
	@FunctionalInterface
	public interface BackgroundLayerRenderer<T extends BackgroundLayer>{
		
		void render(T layer, MatrixStack stack, int x, int y, int width, int height, float xPan, float yPan, float parallax, float xOff, float yOff, float zoom);
	}
}