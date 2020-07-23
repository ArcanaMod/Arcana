package net.arcanamod.event;

import net.arcanamod.capabilities.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AttachCapabilities{
	
	@SubscribeEvent
	public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event){
		// Add Researcher capability to players.
		if(event.getObject() instanceof PlayerEntity){
			ResearcherCapability.Provider cap = new ResearcherCapability.Provider();
			event.addCapability(ResearcherCapability.KEY, cap);
			// Tell the Researcher object who it's attached to.
			cap.getCapability(ResearcherCapability.RESEARCHER_CAPABILITY, null).ifPresent(x -> x.setPlayer((PlayerEntity)event.getObject()));
		}
		// Add TaintTrackable capability to all living entities.
		if(event.getObject() instanceof LivingEntity){
			TaintTrackableCapability.Provider cap = new TaintTrackableCapability.Provider();
			event.addCapability(TaintTrackableCapability.KEY, cap);
		}
	}
	
	@SubscribeEvent
	public static void attachChunkCapabilities(AttachCapabilitiesEvent<Chunk> event){
		AuraChunkCapability.Provider cap = new AuraChunkCapability.Provider();
		event.addCapability(AuraChunkCapability.KEY, cap);
	}
	
	@SuppressWarnings("ConstantConditions")
	@SubscribeEvent
	public static void playerClone(PlayerEvent.Clone event){
		if(!event.isWasDeath()){
			Researcher.getFrom(event.getPlayer()).setEntryData(Researcher.getFrom(event.getOriginal()).getEntryData());
			TaintTrackable.getFrom(event.getPlayer()).deserializeNBT(TaintTrackable.getFrom(event.getOriginal()).serializeNBT());
		}
	}
}