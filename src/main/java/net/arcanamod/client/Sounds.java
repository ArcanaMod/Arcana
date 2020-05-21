package net.arcanamod.client;

import net.arcanamod.Arcana;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber
public class Sounds{
	
	public static SoundEvent SPELL_CAST;
	
	public static void registerSounds(){
		// can't be static
		//SPELL_CAST = registerSound("spell_cast");
	}
	
	private static SoundEvent registerSound(String name){
		ResourceLocation location = new ResourceLocation(Arcana.MODID, name);
		SoundEvent event = new SoundEvent(location);
		event.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(event);
		return event;
	}
}