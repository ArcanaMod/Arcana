package net.arcanamod.event;

import net.arcanamod.network.Connection;
import net.arcanamod.network.PktSyncBooksHandler.PktSyncBooks;
import net.arcanamod.network.PktSyncClientResearchHandler.PktSyncClientResearch;
import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.Researcher;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

/**
 * Class for handling any events that occur upon world load
 *
 * @author Atlas
 */
@EventBusSubscriber
public class WorldLoadEvent{
	
	@SubscribeEvent
	public static void onWorldLoad(PlayerLoggedInEvent event){
		//		TaintLevelHandler.createTaintLevelFile(event.player.world);
		// Its definitely an EntityPlayerMP
		Connection.network.sendTo(new PktSyncBooks(ResearchBooks.books, ResearchBooks.puzzles), (ServerPlayerEntity)event.player);
		// may need to delay this somehow...
		Researcher researcher = Researcher.getFrom(event.player);
		Connection.network.sendTo(new PktSyncClientResearch(researcher.getEntryData(), researcher.getPuzzleData()), (ServerPlayerEntity)event.player);
	}
}