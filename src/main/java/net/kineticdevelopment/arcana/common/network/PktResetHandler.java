package net.kineticdevelopment.arcana.common.network;

import net.kineticdevelopment.arcana.core.research.ResearchEntry;
import net.kineticdevelopment.arcana.core.research.Researcher;
import net.kineticdevelopment.arcana.core.research.ResearchBooks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PktResetHandler implements IMessageHandler<PktResetHandler.PktResetResearch, IMessage>{
	
	public IMessage onMessage(PktResetResearch message, MessageContext ctx){
		ResearchEntry entry = ResearchBooks.getEntry(message.getKey());
		if(entry != null)
			Researcher.getFrom(Minecraft.getMinecraft().player).resetEntry(entry);
		return null;
	}
	
	public static class PktResetResearch extends StringPacket{
		
		public PktResetResearch(){}
		
		public PktResetResearch(ResourceLocation entryKey){
			this.entryKey = entryKey.toString();
		}
		
		public ResourceLocation getKey(){
			return new ResourceLocation(entryKey);
		}
	}
}