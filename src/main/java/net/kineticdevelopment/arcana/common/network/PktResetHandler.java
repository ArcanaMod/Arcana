package net.kineticdevelopment.arcana.common.network;

import net.kineticdevelopment.arcana.client.research.ClientBooks;
import net.kineticdevelopment.arcana.core.research.ResearchEntry;
import net.kineticdevelopment.arcana.core.research.Researcher;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PktResetHandler implements IMessageHandler<PktResetResearch, IMessage>{
	
	public IMessage onMessage(PktResetResearch message, MessageContext ctx){
		ResearchEntry entry = ClientBooks.getEntry(message.getKey());
		if(entry != null)
			Researcher.getFrom(Minecraft.getMinecraft().player).reset(entry);
		return null;
	}
}