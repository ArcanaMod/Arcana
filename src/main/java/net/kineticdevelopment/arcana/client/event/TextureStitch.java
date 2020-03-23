package net.kineticdevelopment.arcana.client.event;

import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.Sprites;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import scala.Predef;

@Mod.EventBusSubscriber(Side.CLIENT)
public class TextureStitch {
    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        Sprites.NORMAL_NODE = event.getMap().registerSprite(new ResourceLocation(Main.MODID, "nodes/normal_node"));
    }
}
