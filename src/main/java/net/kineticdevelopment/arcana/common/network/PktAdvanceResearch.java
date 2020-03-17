package net.kineticdevelopment.arcana.common.network;

import net.minecraft.util.ResourceLocation;

public class PktAdvanceResearch extends StringPacket{
	
	public PktAdvanceResearch(){}
	
	public PktAdvanceResearch(ResourceLocation entryKey){
		this.entryKey = entryKey.toString();
	}
	
	public ResourceLocation getKey(){
		return new ResourceLocation(entryKey);
	}
}
