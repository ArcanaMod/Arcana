package net.arcanamod.client.event;

import net.arcanamod.Arcana;
import net.arcanamod.client.render.aspects.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ParticleFactoryEvent{
	@SubscribeEvent
	public static void onParticleFactoryRegister(final ParticleFactoryRegisterEvent event){
		Minecraft.getInstance().particles.registerFactory(ArcanaParticles.NODE_PARTICLE.get(), new NodeParticle.Factory());
		Minecraft.getInstance().particles.registerFactory(ArcanaParticles.ASPECT_PARTICLE.get(), new AspectParticle.Factory());
		Minecraft.getInstance().particles.registerFactory(ArcanaParticles.NUMBER_PARTICLE.get(), new NumberParticle.Factory());
		Minecraft.getInstance().particles.registerFactory(ArcanaParticles.ASPECT_HELIX_PARTICLE.get(), AspectHelixParticle.Factory::new);
	}
}