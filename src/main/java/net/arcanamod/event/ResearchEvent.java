package net.arcanamod.event;

import net.arcanamod.research.ResearchEntry;
import net.arcanamod.research.Researcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

/**
 * This event is fired on the {@link MinecraftForge#EVENT_BUS} when a Researcher changes the stage of a research.
 * It is fired on both the server and client side.
 */
public class ResearchEvent extends Event{
	
	private PlayerEntity player;
	private Researcher researcher;
	private ResearchEntry entry;
	private Type type;
	
	public PlayerEntity getPlayer(){
		return player;
	}
	
	public Researcher getResearcher(){
		return researcher;
	}
	
	public ResearchEntry getEntry(){
		return entry;
	}
	
	public Type getType(){
		return type;
	}
	
	public ResearchEvent(PlayerEntity player, Researcher researcher, ResearchEntry entry, Type type){
		this.player = player;
		this.researcher = researcher;
		this.entry = entry;
		this.type = type;
	}
	
	public enum Type{
		ADVANCE,
		RESET,
		COMPLETE
	}
}