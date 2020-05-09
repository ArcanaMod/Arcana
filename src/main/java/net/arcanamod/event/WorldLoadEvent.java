package net.arcanamod.event;

import net.arcanamod.Arcana;
import net.arcanamod.network.Connection;
import net.arcanamod.network.PkSyncResearch;
import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.ResearchLoader;
import net.arcanamod.research.Researcher;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * Class for handling any events that occur upon world load
 *
 * @author Atlas
 */
@EventBusSubscriber
public class WorldLoadEvent{
	
	@SubscribeEvent
	public static void onWorldLoad(PlayerEvent.PlayerLoggedInEvent event){
		// Its definitely an EntityPlayerMP
		Connection.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)event.getPlayer()), new PkSyncResearch(ResearchBooks.books, ResearchBooks.puzzles));
		Researcher researcher = Researcher.getFrom(event.getPlayer());
		//Connection.network.sendTo(new PktSyncClientResearch(researcher.getEntryData(), researcher.getPuzzleData()), (ServerPlayerEntity)event.player);
	}
	
	@SubscribeEvent
	public static void serverAboutToStart(FMLServerAboutToStartEvent event){
		event.getServer().getResourceManager().addReloadListener(Arcana.researchManager = new ResearchLoader());
	}
}