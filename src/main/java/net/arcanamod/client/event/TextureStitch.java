package net.arcanamod.client.event;

import net.arcanamod.client.render.JarTileEntityRender;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TextureStitch{
	
	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event){
		if(event.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)){
			event.addSprite(JarTileEntityRender.JAR_CONTENT_SIDE);
			event.addSprite(JarTileEntityRender.JAR_CONTENT_TOP);
		}
		//Sprites.NORMAL_NODE = event.getMap().registerSprite(new ResourceLocation(Arcana.MODID, "nodes/normal_node"));
		//event.getMap().registerSprite(new ResourceLocation(Arcana.MODID, "gui/container/unknown_slot"));
	}
}