package net.kineticdevelopment.arcana.common.network;

import net.kineticdevelopment.arcana.core.research.ResearchEntry;
import net.kineticdevelopment.arcana.core.research.Researcher;
import net.kineticdevelopment.arcana.core.research.ResearchBooks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PktAdvanceHandler implements IMessageHandler<PktAdvanceHandler.PktAdvanceResearch, IMessage>{
	
	public IMessage onMessage(PktAdvanceResearch message, MessageContext ctx){
		ResearchEntry entry = ResearchBooks.getEntry(message.getKey());
		if(entry != null)
			Researcher.getFrom(Minecraft.getMinecraft().player).advanceEntry(entry);
		// else print error
		return null;
	}
	
	public static class PktAdvanceResearch extends StringPacket{
		
		public PktAdvanceResearch(){}
		
		public PktAdvanceResearch(ResourceLocation entryKey){
			this.entryKey = entryKey.toString();
		}
		
		public ResourceLocation getKey(){
			return new ResourceLocation(entryKey);
		}
	}
}