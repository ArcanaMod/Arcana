package net.arcanamod.network;


import net.arcanamod.Arcana;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.capabilities.Researcher;
import net.arcanamod.client.ClientAuraHandler;
import net.arcanamod.systems.research.Puzzle;
import net.arcanamod.systems.research.ResearchBooks;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PkSyncPlayerFlux{
	
	int flux;
	
	public PkSyncPlayerFlux(int flux){
		this.flux = flux;
	}
	
	public static void encode(PkSyncPlayerFlux msg, PacketBuffer buffer){
		buffer.writeVarInt(msg.flux);
	}
	
	public static PkSyncPlayerFlux decode(PacketBuffer buffer){
		return new PkSyncPlayerFlux(buffer.readVarInt());
	}
	
	public static void handle(PkSyncPlayerFlux msg, Supplier<NetworkEvent.Context> supplier){
		supplier.get().enqueueWork(() -> {
			// I'm on client.
			// There's a separate ClientAuraHandler per player.
			ClientAuraHandler.currentFlux = msg.flux;
			// And check if there's enough flux around for research
			Puzzle puzzle = ResearchBooks.puzzles.get(Arcana.arcLoc("flux_build_research"));
			Researcher researcher = Researcher.getFrom(Arcana.proxy.getPlayerOnClient());
			if(msg.flux > ArcanaConfig.FLUX_RESEARCH_REQUIREMENT.get() && !researcher.isPuzzleCompleted(puzzle)){
				researcher.completePuzzle(puzzle);
				Arcana.proxy.displayPuzzleToast(ResearchBooks.getEntry(Arcana.arcLoc("flux")));
			}
		});
		supplier.get().setPacketHandled(true);
	}
}