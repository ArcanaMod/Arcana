package net.kineticdevelopment.arcana.common.network;

import net.minecraft.util.ResourceLocation;

public class PktCompleteResearch extends StringPacket{
	
	public PktCompleteResearch(){}
	
	public PktCompleteResearch(ResourceLocation entryKey){
		this.entryKey = entryKey.toString();
	}
	
	public ResourceLocation getKey(){
		return new ResourceLocation(entryKey);
	}
}
