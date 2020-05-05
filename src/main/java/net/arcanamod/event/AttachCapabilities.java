package net.arcanamod.event;

import net.arcanamod.research.Researcher;
import net.arcanamod.research.impls.ResearcherCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class AttachCapabilities{
	
	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event){
		if(event.getObject() instanceof PlayerEntity){
			ResearcherCapability.Provider cap = new ResearcherCapability.Provider();
			event.addCapability(ResearcherCapability.KEY, cap);
			cap.getCapability(ResearcherCapability.RESEARCHER_CAPABILITY, null).setPlayer((PlayerEntity)event.getObject());
		}
	}
	
	@SubscribeEvent
	public static void playerClone(PlayerEvent.Clone event){
		if(!event.isWasDeath())
			Researcher.getFrom(event.getEntityPlayer()).setEntryData(Researcher.getFrom(event.getOriginal()).getEntryData());
	}
}