package net.arcanamod.network;

import net.arcanamod.Arcana;
import net.arcanamod.systems.research.ResearchBooks;
import net.arcanamod.systems.research.ResearchEntry;
import net.arcanamod.capabilities.Researcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PkModifyResearch{
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	Diff diff;
	ResourceLocation key;
	
	public PkModifyResearch(Diff diff, ResourceLocation key){
		this.diff = diff;
		this.key = key;
	}
	
	public static void encode(PkModifyResearch msg, PacketBuffer buffer){
		buffer.writeEnumValue(msg.diff);
		buffer.writeResourceLocation(msg.key);
	}
	
	public static PkModifyResearch decode(PacketBuffer buffer){
		return new PkModifyResearch(buffer.readEnumValue(Diff.class), buffer.readResourceLocation());
	}
	
	public static void handle(PkModifyResearch msg, Supplier<NetworkEvent.Context> supplier){
		supplier.get().enqueueWork(() -> {
			PlayerEntity pe = Arcana.proxy.getPlayerOnClient();
			Researcher researcher = Researcher.getFrom(pe);
			ResearchEntry entry = ResearchBooks.streamEntries().filter(e -> e.key().equals(msg.key)).findFirst().orElseGet(() -> {
				LOGGER.error("An error occurred modifying player research progress on client: invalid research entry.");
				return null;
			});
			if(entry != null)
				if(msg.diff == Diff.complete)
					researcher.completeEntry(entry);
				else if(msg.diff == Diff.advance)
					researcher.advanceEntry(entry);
				else
					researcher.resetEntry(entry);
		});
		supplier.get().setPacketHandled(true);
	}
	
	public enum Diff{
		complete, advance, reset
	}
}