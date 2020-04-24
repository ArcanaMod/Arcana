package net.kineticdevelopment.arcana.common.network;

import net.kineticdevelopment.arcana.core.research.ResearchEntry;
import net.kineticdevelopment.arcana.core.research.Researcher;
import net.kineticdevelopment.arcana.core.research.ResearchBooks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PktCompleteHandler implements IMessageHandler<PktCompleteHandler.PktCompleteResearch, IMessage>{
	
	public IMessage onMessage(PktCompleteResearch message, MessageContext ctx){
		// on client
		ResearchEntry entry = ResearchBooks.getEntry(message.getKey());
		if(entry != null)
			Researcher.getFrom(Minecraft.getMinecraft().player).completeEntry(entry);
		return null;
	}
	
	public static class PktCompleteResearch extends StringPacket{
		
		public PktCompleteResearch(){}
		
		public PktCompleteResearch(ResourceLocation entryKey){
			this.entryKey = entryKey.toString();
		}
		
		public ResourceLocation getKey(){
			return new ResourceLocation(entryKey);
		}
	}
}