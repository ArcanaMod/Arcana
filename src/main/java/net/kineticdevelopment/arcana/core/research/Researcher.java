package net.kineticdevelopment.arcana.core.research;

import net.kineticdevelopment.arcana.common.event.ResearchEvent;
import net.kineticdevelopment.arcana.core.research.impls.ResearcherCapability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public interface Researcher{
	
	/**
	 * Returns the last index of entry section unlocked for that research.
	 * Returns 0 for entries that have not been unlocked yet, or have no progress.
	 *
	 * @param entry
	 * 		The research entry to check the status of.
	 * @return The last index of entry section unlocked, or 0 if it hasn't been unlocked or progressed.
	 */
	int stage(ResearchEntry entry);
	
	/**
	 * Increments the stage for an entry.
	 *
	 * <p>If the new section has no requirements, this continues to increment the stage
	 * until it reaches either a section with requirements, or the end of the entry.
	 *
	 * <p>Fires {@link ResearchEvent} if the page is not already complete.
	 *
	 * <p>Has no effect if the page is already complete.
	 *
	 * <p>TODO: addenda.
	 *
	 * @param entry
	 * 		The research page to advance.
	 */
	void advance(ResearchEntry entry);
	
	/**
	 * Sets this researchers progress for an entry to its maximum progress
	 *
	 * <p>Has no effect if this entry is already complete.
	 * Fires {@link ResearchEvent} if the page is not already complete.
	 *
	 * @param entry
	 * 		The research entry to complete.
	 */
	void complete(ResearchEntry entry);
	
	/**
	 * Removes all progress on the given entry.
	 *
	 * <p>Fires {@link ResearchEvent} if the page is not already incomplete.
	 *
	 * @param entry The research entry to reset.
	 */
	void reset(ResearchEntry entry);
	
	void markDirty();
	
	void setPlayer(EntityPlayer player);
	EntityPlayer getPlayer();
	
	/**
	 * Returns a map containing this researcher's data, where the keys are the keys of all sections
	 * that have a stage greater than 0 (possibly including some that don't), and the values are the current stage of that entry.
	 *
	 * @return A Map containing the data of this researcher.
	 */
	Map<ResourceLocation, Integer> getData();
	
	void setData(Map<ResourceLocation, Integer> data);
	
	default NBTBase serialize(){
		NBTTagCompound compound = new NBTTagCompound();
		getData().forEach((key, value) -> compound.setInteger(key.toString(), value));
		return compound;
	}
	
	default void deserialize(NBTTagCompound data){
		Map<ResourceLocation, Integer> dat = new HashMap<>();
		for(String s : data.getKeySet())
			dat.put(new ResourceLocation(s), data.getInteger(s));
		setData(dat);
	}
	
	static boolean canAdvance(Researcher r, ResearchEntry entry, EntityPlayer player){
		return entry.sections().get(r.stage(entry)).getRequirements().stream().allMatch(x -> x.satisfied(player));
	}
	
	static void takeAndAdvance(Researcher r, ResearchEntry entry, EntityPlayer player){
		if(canAdvance(r, entry, player))
			entry.sections().get(r.stage(entry)).getRequirements().forEach(requirement -> requirement.take(player));
	}
	
	/**
	 * Returns a player's researcher capability, or null if there is no attached researcher capability.
	 *
	 * @param p
	 * 		The player to get a capability from.
	 * @return The player's researcher capability.
	 */
	static Researcher getFrom(EntityPlayer p){
		// An exception should *not* be thrown, since every player gets one, but just in case.
		return p.getCapability(ResearcherCapability.RESEARCHER_CAPABILITY, null);
	}
}