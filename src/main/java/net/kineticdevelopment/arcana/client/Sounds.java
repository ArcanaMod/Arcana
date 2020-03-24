package net.kineticdevelopment.arcana.client;

import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber
public class Sounds {

    public static SoundEvent SPELL_CAST;

    public static void registerSounds() {
        SPELL_CAST = registerSound("spell_cast");
    }

    private static SoundEvent registerSound(String name) {
        ResourceLocation location = new ResourceLocation(Main.MODID, name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }


}
