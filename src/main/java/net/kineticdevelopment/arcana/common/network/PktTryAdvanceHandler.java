package net.kineticdevelopment.arcana.common.network;

import net.kineticdevelopment.arcana.core.research.ResearchEntry;
import net.kineticdevelopment.arcana.core.research.Researcher;
import net.kineticdevelopment.arcana.core.research.ServerBooks;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PktTryAdvanceHandler implements IMessageHandler<PktTryAdvance, PktAdvanceResearch>{
	
	public PktAdvanceResearch onMessage(PktTryAdvance message, MessageContext ctx){
		// null is returned if the player *cannot* advance. a valid PktAdvanceResearch is sent if they *are* allowed to.
		// this is being handled on the server.
		ResearchEntry entry = ServerBooks.getEntry(message.getKey());
		if(entry != null){
			EntityPlayerMP sender = ctx.getServerHandler().player;
			if(Researcher.canAdvance(Researcher.getFrom(sender), entry, sender)){
				Researcher.takeAndAdvance(Researcher.getFrom(sender), entry, sender);
				return new PktAdvanceResearch(message.getKey());
			}
		}
		return null;
	}
}