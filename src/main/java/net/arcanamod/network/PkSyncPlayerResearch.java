package net.arcanamod.network;

import net.arcanamod.Arcana;
import net.arcanamod.capabilities.Researcher;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

/**
 * Syncs the player's progress through research. Not to be confused with {@link PkSyncResearch}, which syncs all existing research.
 */
public class PkSyncPlayerResearch{
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	// how about I just change this to use regular serialization methods...
	
	CompoundNBT data;
	
	public PkSyncPlayerResearch(CompoundNBT nbt){
		data = nbt;
	}
	
	public static void encode(PkSyncPlayerResearch msg, PacketBuffer buffer){
		buffer.writeCompoundTag(msg.data);
	}
	
	public static PkSyncPlayerResearch decode(PacketBuffer buffer){
		return new PkSyncPlayerResearch(buffer.readCompoundTag());
	}
	
	public static void handle(PkSyncPlayerResearch msg, Supplier<NetworkEvent.Context> supplier){
		// from server to client
		supplier.get().enqueueWork(() -> {
			Researcher researcher = Researcher.getFrom(Arcana.proxy.getPlayerOnClient());
			researcher.deserializeNBT(msg.data);
		});
		supplier.get().setPacketHandled(true);
	}
}