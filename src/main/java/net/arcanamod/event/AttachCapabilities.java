package net.arcanamod.event;

import net.arcanamod.capabilities.*;
import net.arcanamod.network.Connection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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
		Researcher.getFrom(event.getPlayer()).deserializeNBT(Researcher.getFrom(event.getOriginal()).serializeNBT());
		TaintTrackable.getFrom(event.getPlayer()).deserializeNBT(TaintTrackable.getFrom(event.getOriginal()).serializeNBT());
		
		// research gets desynced here
		// so we send a sync packet
		// yes its always a ServerPlayerEntity
		// we also need to delay by a tick so the new player can actually get the capability
		WorldTickHandler.onTick.add(world -> Connection.sendSyncPlayerResearch(Researcher.getFrom(event.getPlayer()), (ServerPlayerEntity)event.getPlayer()));
	}
}