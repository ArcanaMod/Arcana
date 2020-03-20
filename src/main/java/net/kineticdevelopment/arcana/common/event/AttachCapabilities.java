package net.kineticdevelopment.arcana.common.event;

import net.kineticdevelopment.arcana.core.research.Researcher;
import net.kineticdevelopment.arcana.core.research.impls.ResearcherCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class AttachCapabilities{
	
	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event){
		if(event.getObject() instanceof EntityPlayer){
			ResearcherCapability.Provider cap = new ResearcherCapability.Provider();
			event.addCapability(ResearcherCapability.KEY, cap);
			cap.getCapability(ResearcherCapability.RESEARCHER_CAPABILITY, null).setPlayer((EntityPlayer)event.getObject());
		}
	}
	
	@SubscribeEvent
	public static void playerClone(PlayerEvent.Clone event){
		if(!event.isWasDeath())
			Researcher.getFrom(event.getEntityPlayer()).setData(Researcher.getFrom(event.getOriginal()).getData());
	}
}