package net.arcanamod.client.event;

import net.arcanamod.client.render.particles.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ParticleFactoryEvent{
	@SubscribeEvent
	public static void onParticleFactoryRegister(final ParticleFactoryRegisterEvent event){
		Minecraft.getInstance().particles.registerFactory(ArcanaParticles.NODE_PARTICLE.get(), new NodeParticle.Factory());
		Minecraft.getInstance().particles.registerFactory(ArcanaParticles.ASPECT_PARTICLE.get(), new AspectParticle.Factory());
		Minecraft.getInstance().particles.registerFactory(ArcanaParticles.NUMBER_PARTICLE.get(), new NumberParticle.Factory());
		Minecraft.getInstance().particles.registerFactory(ArcanaParticles.ASPECT_HELIX_PARTICLE.get(), AspectHelixParticle.Factory::new);
		Minecraft.getInstance().particles.registerFactory(ArcanaParticles.HUNGRY_NODE_BLOCK_PARTICLE.get(), new HungryNodeBlockParticle.Factory());
		Minecraft.getInstance().particles.registerFactory(ArcanaParticles.HUNGRY_NODE_DISC_PARTICLE.get(), new HungryNodeDiscParticle.Factory());
	}
}