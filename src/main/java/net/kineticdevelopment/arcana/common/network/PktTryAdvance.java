package net.kineticdevelopment.arcana.common.network;

import net.minecraft.util.ResourceLocation;

public class PktTryAdvance extends StringPacket{
	
	public PktTryAdvance(){}
	
	public PktTryAdvance(ResourceLocation entryKey){
		this.entryKey = entryKey.toString();
	}
	
	public ResourceLocation getKey(){
		return new ResourceLocation(entryKey);
	}
}