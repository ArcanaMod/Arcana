package net.kineticdevelopment.arcana.core.research;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public interface Researcher{
	
	int stage(ResearchEntry entry);
	void advance(ResearchEntry entry);
	void complete(ResearchEntry entry);
	void markDirty();
	
	void setPlayer(EntityPlayer player);
	EntityPlayer getPlayer();
	
	Map<ResourceLocation, Integer> getData();
	
	void setData(Map<ResourceLocation, Integer> data);
	
	default NBTBase serialize(){
		return null;
	}
	
	default void deserialize(NBTTagCompound data){
	
	}
	
	static boolean canAdvance(Researcher r, ResearchEntry entry, EntityPlayer player){
		return entry.sections().get(r.stage(entry)).getRequirements().stream().allMatch(x -> x.satisfied(player));
	}
}