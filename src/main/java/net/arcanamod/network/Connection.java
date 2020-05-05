package net.arcanamod.network;

import net.arcanamod.Arcana;
import net.arcanamod.network.inventory.PktAspectClickHandler;
import net.arcanamod.network.inventory.PktGetNoteHandler;
import net.arcanamod.network.inventory.PktRequestAspectSync;
import net.arcanamod.network.inventory.PktSyncAspectContainerHandler;
import net.arcanamod.research.ResearchEntry;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class Connection{
	
	public static SimpleNetworkWrapper network;
	private static int id = 0;
	
	public static void init(){
		network = NetworkRegistry.INSTANCE.newSimpleChannel(Arcana.MODID);
		
		network.registerMessage(PktSyncBooksHandler.class, PktSyncBooksHandler.PktSyncBooks.class, id++, Side.CLIENT);
		network.registerMessage(PktResetHandler.class, PktResetHandler.PktResetResearch.class, id++, Side.CLIENT);
		network.registerMessage(PktAdvanceHandler.class, PktAdvanceHandler.PktAdvanceResearch.class, id++, Side.CLIENT);
		network.registerMessage(PktCompleteHandler.class, PktCompleteHandler.PktCompleteResearch.class, id++, Side.CLIENT);
		network.registerMessage(PktSyncAspectContainerHandler.class, PktSyncAspectContainerHandler.PktSyncAspectContainer.class, id++, Side.CLIENT);
		network.registerMessage(PktSyncClientResearchHandler.class, PktSyncClientResearchHandler.PktSyncClientResearch.class, id++, Side.CLIENT);
		
		network.registerMessage(PktTryAdvanceHandler.class, PktTryAdvanceHandler.PktTryAdvance.class, id++, Side.SERVER);
		network.registerMessage(PktAspectClickHandler.class, PktAspectClickHandler.PktAspectClick.class, id++, Side.SERVER);
		network.registerMessage(PktRequestAspectSync.class, PktRequestAspectSync.class, id++, Side.SERVER);
		network.registerMessage(PktGetNoteHandler.class, PktGetNoteHandler.PktGetNote.class, id++, Side.SERVER);
	}
	
	public static void sendTryAdvance(ResearchEntry entry){
		network.sendToServer(new PktTryAdvanceHandler.PktTryAdvance(entry.key()));
	}
	
	public static void sendAdvance(ResearchEntry entry, ServerPlayerEntity player){
		network.sendTo(new PktAdvanceHandler.PktAdvanceResearch(entry.key()), player);
	}
	
	public static void sendReset(ResearchEntry entry, ServerPlayerEntity player){
		network.sendTo(new PktResetHandler.PktResetResearch(entry.key()), player);
	}
	
	public static void sendComplete(ResearchEntry entry, ServerPlayerEntity player){
		network.sendTo(new PktCompleteHandler.PktCompleteResearch(entry.key()), player);
	}
}