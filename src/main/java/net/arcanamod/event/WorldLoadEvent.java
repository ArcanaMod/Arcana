package net.arcanamod.event;

import net.arcanamod.Arcana;
import net.arcanamod.commands.FillPhialCommand;
import net.arcanamod.commands.NodeCommand;
import net.arcanamod.commands.ResearchCommand;
import net.arcanamod.network.Connection;
import net.arcanamod.network.PkRequestNodeSync;
import net.arcanamod.network.PkSyncResearch;
import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.ResearchLoader;
import net.arcanamod.research.Researcher;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for handling any events that occur upon world load
 *
 * @author Atlas
 */
@EventBusSubscriber
public class WorldLoadEvent{
	
	@SubscribeEvent
	public static void onWorldLoad(PlayerEvent.PlayerLoggedInEvent event){
		// It's definitely an ServerPlayerEntity.
		Connection.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)event.getPlayer()), new PkSyncResearch(ResearchBooks.books, ResearchBooks.puzzles));
		Researcher researcher = Researcher.getFrom(event.getPlayer());
		Connection.sendSyncPlayerResearch(researcher, (ServerPlayerEntity)event.getPlayer());
	}
	
	@SubscribeEvent
	public static void serverAboutToStart(FMLServerAboutToStartEvent event){
		event.getServer().getResourceManager().addReloadListener(Arcana.researchManager = new ResearchLoader());
	}
	
	@SubscribeEvent
	public static void serverStarting(FMLServerStartingEvent event){
		ResearchCommand.register(event.getCommandDispatcher());
		FillPhialCommand.register(event.getCommandDispatcher());
		NodeCommand.register(event.getCommandDispatcher());
	}
}