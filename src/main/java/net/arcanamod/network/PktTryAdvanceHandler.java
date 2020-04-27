package net.arcanamod.network;

import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.ResearchEntry;
import net.arcanamod.research.Researcher;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PktTryAdvanceHandler implements IMessageHandler<PktTryAdvanceHandler.PktTryAdvance, PktAdvanceHandler.PktAdvanceResearch>{
	
	public PktAdvanceHandler.PktAdvanceResearch onMessage(PktTryAdvance message, MessageContext ctx){
		// null is returned if the player *cannot* advance. a valid PktAdvanceResearch is sent if they *are* allowed to.
		// this is being handled on the server.
		ResearchEntry entry = ResearchBooks.getEntry(message.getKey());
		if(entry != null){
			EntityPlayerMP sender = ctx.getServerHandler().player;
			if(Researcher.canAdvanceEntry(Researcher.getFrom(sender), entry)){
				Researcher.takeRequirementsAndAdvanceEntry(Researcher.getFrom(sender), entry);
				return new PktAdvanceHandler.PktAdvanceResearch(message.getKey());
			}
		}
		return null;
	}
	
	public static class PktTryAdvance extends StringPacket{
		
		public PktTryAdvance(){
		}
		
		public PktTryAdvance(ResourceLocation entryKey){
			this.entryKey = entryKey.toString();
		}
		
		public ResourceLocation getKey(){
			return new ResourceLocation(entryKey);
		}
	}
}