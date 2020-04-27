package net.arcanamod.client.event;

import net.arcanamod.Arcana;
import net.arcanamod.client.Sprites;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class TextureStitch{
	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event){
		Sprites.NORMAL_NODE = event.getMap().registerSprite(new ResourceLocation(Arcana.MODID, "nodes/normal_node"));
		event.getMap().registerSprite(new ResourceLocation(Arcana.MODID, "gui/container/unknown_slot"));
	}
}