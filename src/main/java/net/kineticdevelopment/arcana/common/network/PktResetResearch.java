package net.kineticdevelopment.arcana.common.network;

import net.minecraft.util.ResourceLocation;

public class PktResetResearch extends StringPacket{
	
	public PktResetResearch(){}
	
	public PktResetResearch(ResourceLocation entryKey){
		this.entryKey = entryKey.toString();
	}
	
	public ResourceLocation getKey(){
		return new ResourceLocation(entryKey);
	}
}
