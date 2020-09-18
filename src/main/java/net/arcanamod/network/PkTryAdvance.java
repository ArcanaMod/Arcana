package net.arcanamod.network;

import net.arcanamod.systems.research.ResearchBooks;
import net.arcanamod.systems.research.ResearchEntry;
import net.arcanamod.capabilities.Researcher;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PkTryAdvance{
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	ResourceLocation key;
	
	public PkTryAdvance(ResourceLocation key){
		this.key = key;
	}
	
	public static void encode(PkTryAdvance msg, PacketBuffer buffer){
		buffer.writeResourceLocation(msg.key);
	}
	
	public static PkTryAdvance decode(PacketBuffer buffer){
		return new PkTryAdvance(buffer.readResourceLocation());
	}
	
	public static void handle(PkTryAdvance msg, Supplier<NetworkEvent.Context> supplier){
		supplier.get().enqueueWork(() -> {
			ServerPlayerEntity sender = supplier.get().getSender();
			Researcher researcher = Researcher.getFrom(sender);
			ResearchEntry entry = ResearchBooks.streamEntries().filter(e -> e.key().equals(msg.key)).findFirst().orElseGet(() -> {
				LOGGER.error("An error occurred trying to advance research progress on server: invalid research entry.");
				return null;
			});
			if(entry != null)
				if(Researcher.canAdvanceEntry(researcher, entry)){
					Researcher.takeRequirementsAndAdvanceEntry(researcher, entry);
					Connection.sendModifyResearch(PkModifyResearch.Diff.advance, msg.key, sender);
				}
		});
		supplier.get().setPacketHandled(true);
	}
}