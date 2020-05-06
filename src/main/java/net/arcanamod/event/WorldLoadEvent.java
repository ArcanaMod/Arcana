package net.arcanamod.event;

import net.arcanamod.research.Researcher;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * Class for handling any events that occur upon world load
 *
 * @author Atlas
 */
@EventBusSubscriber
public class WorldLoadEvent{
	
	@SubscribeEvent
	public static void onWorldLoad(PlayerEvent.PlayerLoggedInEvent event){
		//		TaintLevelHandler.createTaintLevelFile(event.player.world);
		// Its definitely an EntityPlayerMP
		//Connection.network.sendTo(new PktSyncBooks(ResearchBooks.books, ResearchBooks.puzzles), (ServerPlayerEntity)event.player);
		// may need to delay this somehow...
		Researcher researcher = Researcher.getFrom(event.getPlayer());
		//Connection.network.sendTo(new PktSyncClientResearch(researcher.getEntryData(), researcher.getPuzzleData()), (ServerPlayerEntity)event.player);
	}
}