package net.kineticdevelopment.arcana.core.research.impls;

import net.kineticdevelopment.arcana.core.research.ResearchEntry;
import net.kineticdevelopment.arcana.core.research.Researcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ResearcherImpl implements Researcher{
	
	private Map<ResourceLocation, Integer> data = new HashMap<>();
	private EntityPlayer player;
	
	public int stage(ResearchEntry entry){
		return data.getOrDefault(entry.key(), 0);
	}
	
	public void advance(ResearchEntry entry){
		// if we can advance
		if(Researcher.canAdvance(this, entry, player))
			do{
				// increment stage
				data.put(entry.key(), stage(entry) + 1);
				// as long as there are more stages
				// and the current stage has no requirements
				// this ends up on entry.sections().size(); an entry with 1 section is on stage 0 by default and can be incremented to 1.
				// TODO: don't skip through addenda
			}while(stage(entry) < entry.sections().size() && entry.sections().get(stage(entry)).getRequirements().size() == 0);
	}
	
	public void complete(ResearchEntry entry){
		data.put(entry.key(), entry.sections().size());
	}
	
	public void markDirty(){}
	
	public Map<ResourceLocation, Integer> getData(){
		return Collections.unmodifiableMap(data);
	}
	
	public void setData(Map<ResourceLocation, Integer> data){
		this.data.clear();
		this.data.putAll(data);
	}
	
	public void setPlayer(EntityPlayer player){
		this.player = player;
	}
	
	public EntityPlayer getPlayer(){
		return player;
	}
}
