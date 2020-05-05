package net.arcanamod.client.event;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TextureStitch{
	
	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event){
		//Sprites.NORMAL_NODE = event.getMap().registerSprite(new ResourceLocation(Arcana.MODID, "nodes/normal_node"));
		//event.getMap().registerSprite(new ResourceLocation(Arcana.MODID, "gui/container/unknown_slot"));
	}
}