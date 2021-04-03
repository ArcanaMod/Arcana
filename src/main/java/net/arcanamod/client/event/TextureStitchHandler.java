package net.arcanamod.client.event;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.client.render.tiles.AspectBookshelfTileEntityRenderer;
import net.arcanamod.client.render.tiles.AspectValveTileEntityRenderer;
import net.arcanamod.client.render.tiles.JarTileEntityRender;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.Core;
import net.arcanamod.items.attachment.Focus;
import net.arcanamod.world.NodeType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class TextureStitchHandler {
	
	@SuppressWarnings("deprecation")
	public static void onTextureStitch(@Nonnull TextureStitchEvent.Pre event){
		if(event.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)){
			event.addSprite(JarTileEntityRender.JAR_CONTENT_SIDE);
			event.addSprite(JarTileEntityRender.JAR_CONTENT_TOP);
			event.addSprite(JarTileEntityRender.JAR_CONTENT_BOTTOM);
			event.addSprite(JarTileEntityRender.JAR_LABEL);
			event.addSprite(AspectBookshelfTileEntityRenderer.PHIAL_LID);
			event.addSprite(AspectBookshelfTileEntityRenderer.PHIAL_BODY);
			event.addSprite(AspectBookshelfTileEntityRenderer.PHIAL_BASE);
			event.addSprite(AspectBookshelfTileEntityRenderer.PHIAL_TOP);
			event.addSprite(AspectBookshelfTileEntityRenderer.PHIAL_SIDE);
			event.addSprite(AspectBookshelfTileEntityRenderer.PHIAL_BOTTOM);
			event.addSprite(AspectBookshelfTileEntityRenderer.PHIAL_CAP);
			event.addSprite(ArcanaVariables.arcLoc("font/number_0"));
			event.addSprite(ArcanaVariables.arcLoc("font/number_1"));
			event.addSprite(ArcanaVariables.arcLoc("font/number_2"));
			event.addSprite(ArcanaVariables.arcLoc("font/number_3"));
			event.addSprite(ArcanaVariables.arcLoc("font/number_4"));
			event.addSprite(ArcanaVariables.arcLoc("font/number_5"));
			event.addSprite(ArcanaVariables.arcLoc("font/number_6"));
			event.addSprite(ArcanaVariables.arcLoc("font/number_7"));
			event.addSprite(ArcanaVariables.arcLoc("font/number_8"));
			event.addSprite(ArcanaVariables.arcLoc("font/number_9"));
			event.addSprite(ArcanaVariables.arcLoc("models/items/thaumonomicon_model"));
			event.addSprite(AspectValveTileEntityRenderer.GEAR_TEX);

			for (Aspect aspect : Aspects.getAll()){
				event.addSprite(ArcanaVariables.arcLoc("aspect/paper/paper_"+aspect.name()));
			}
			
			// add all of the wand related textures
			for(Cap cap : Cap.CAPS.values())
				event.addSprite(cap.getTextureLocation());
			for(Core core : Core.CORES.values())
				event.addSprite(core.getTextureLocation());
			for(Focus focus : Focus.FOCI)
				for(ResourceLocation location : focus.getAllModelLocations())
					event.addSprite(new ResourceLocation(location.getNamespace(), "models/wands/foci/" + location.getPath()));

			for(NodeType value : NodeType.TYPES.values()){
				event.addSprite(value.texture(null, null, null));
			}
		}
	}
}