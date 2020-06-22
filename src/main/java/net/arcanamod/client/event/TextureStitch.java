package net.arcanamod.client.event;

import net.arcanamod.client.render.JarTileEntityRender;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.Core;
import net.arcanamod.items.attachment.Focus;
import net.arcanamod.world.NodeType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

import javax.annotation.Nonnull;

public class TextureStitch{
	
	public static void onTextureStitch(@Nonnull TextureStitchEvent.Pre event){
		if(event.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)){
			event.addSprite(JarTileEntityRender.JAR_CONTENT_SIDE);
			event.addSprite(JarTileEntityRender.JAR_CONTENT_TOP);
			
			// add all of the wand related textures
			for(Cap cap : Cap.CAPS.values())
				event.addSprite(cap.getTextureLocation());
			for(Core core : Core.CORES.values())
				event.addSprite(core.getTextureLocation());
			for(Focus focus : Focus.FOCI)
				event.addSprite(focus.getModelLocation());

			for(NodeType value : NodeType.TYPES.values()){
				event.addSprite(value.texture(null, null, null));
			}
		}
	}
}