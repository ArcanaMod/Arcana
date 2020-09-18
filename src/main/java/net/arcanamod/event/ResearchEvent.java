package net.arcanamod.event;

import net.arcanamod.systems.research.ResearchEntry;
import net.arcanamod.capabilities.Researcher;
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
	
	/**
	 * Returns the player associated with the Researcher who's research is being modified.
	 *
	 * @return The player.
	 */
	public PlayerEntity getPlayer(){
		return player;
	}
	
	/**
	 * Returns the Researcher who's research is being modified.
	 *
	 * @return The Researcher.
	 */
	public Researcher getResearcher(){
		return researcher;
	}
	
	/**
	 * Returns the entry who's stage is being modified.
	 *
	 * @return The entry.
	 */
	public ResearchEntry getEntry(){
		return entry;
	}
	
	/**
	 * Returns how the entry is being modified - whether it is being reset to 0 progress, advanced to the next section with requirements,
	 * or being set to full progress.
	 *
	 * @return How the entry is being modified.
	 */
	public Type getType(){
		return type;
	}
	
	public ResearchEvent(PlayerEntity player, Researcher researcher, ResearchEntry entry, Type type){
		this.player = player;
		this.researcher = researcher;
		this.entry = entry;
		this.type = type;
	}
	
	/**
	 * Represents a way that a research entry's progress can be changed.
	 */
	public enum Type{
		ADVANCE,
		RESET,
		COMPLETE
	}
}