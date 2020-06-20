package net.arcanamod.event;

import net.arcanamod.Arcana;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.commands.FillPhialCommand;
import net.arcanamod.commands.NodeCommand;
import net.arcanamod.commands.ResearchCommand;
import net.arcanamod.commands.TaintCommand;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.network.Connection;
import net.arcanamod.network.PkSyncResearch;
import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.ResearchLoader;
import net.arcanamod.research.Researcher;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import static net.arcanamod.Arcana.arcLoc;

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
		
		// If the player should get a one-time scribbled notes,
		if(ArcanaConfig.SPAWN_WITH_NOTES.get()){
			ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
			Advancement hasNote = player.world.getServer().getAdvancementManager().getAdvancement(arcLoc("obtained_note"));
			// and they haven't already got them this way,
			if(!player.getAdvancements().getProgress(hasNote).isDone()){
				// give them the notes,
				player.addItemStackToInventory(new ItemStack(ArcanaItems.SCRIBBLED_NOTES.get()));
				// and grant the advancement, so they never get it again.
				player.getAdvancements().getProgress(hasNote).grantCriterion("impossible");
			}
		}
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
		TaintCommand.register(event.getCommandDispatcher());
	}
}