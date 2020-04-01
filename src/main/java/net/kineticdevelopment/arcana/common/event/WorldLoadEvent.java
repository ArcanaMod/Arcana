package net.kineticdevelopment.arcana.common.event;

import net.kineticdevelopment.arcana.common.network.Connection;
import net.kineticdevelopment.arcana.common.network.PktSyncBooksHandler.PktSyncBooks;
import net.kineticdevelopment.arcana.common.network.PktSyncClientResearchHandler.PktSyncClientResearch;
import net.kineticdevelopment.arcana.core.research.Researcher;
import net.kineticdevelopment.arcana.core.research.ServerBooks;
import net.kineticdevelopment.arcana.utilities.taint.TaintLevelHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

/**
 * Class for handling any events that occur upon world load
 * @author Atlas
 */
@EventBusSubscriber
public class WorldLoadEvent{
	
	@SubscribeEvent
	public static void onWorldLoad(PlayerLoggedInEvent event) {
		TaintLevelHandler.createTaintLevelFile(event.player.world);
		// Its definitely an EntityPlayerMP
		Connection.network.sendTo(new PktSyncBooks(ServerBooks.books, ServerBooks.puzzles), (EntityPlayerMP)event.player);
		// may need to delay this somehow...
		Connection.network.sendTo(new PktSyncClientResearch(Researcher.getFrom(event.player).getData()), (EntityPlayerMP)event.player);
	}
}