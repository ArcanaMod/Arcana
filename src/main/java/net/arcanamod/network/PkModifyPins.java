package net.arcanamod.network;

import net.arcanamod.capabilities.Researcher;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PkModifyPins{
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	Diff diff;
	ResourceLocation key;
	int stage;
	
	// we don't need to send a confirmation packet because pins are *essentially* client-side
	// being out of sync does absolutely nothing; this is just so you don't have to re-pin on every server start
	
	public PkModifyPins(Diff diff, ResourceLocation key, int stage){
		this.diff = diff;
		this.key = key;
		this.stage = stage;
	}
	
	public static void encode(PkModifyPins msg, PacketBuffer buffer){
		buffer.writeEnumValue(msg.diff);
		buffer.writeResourceLocation(msg.key);
		buffer.writeVarInt(msg.stage);
	}
	
	public static PkModifyPins decode(PacketBuffer buffer){
		return new PkModifyPins(buffer.readEnumValue(Diff.class), buffer.readResourceLocation(), buffer.readVarInt());
	}
	
	// from client to server
	public static void handle(PkModifyPins msg, Supplier<NetworkEvent.Context> supplier){
		supplier.get().enqueueWork(() -> {
			ServerPlayerEntity sender = supplier.get().getSender();
			Researcher researcher = Researcher.getFrom(sender);
			if(researcher != null)
				if(msg.diff == Diff.pin)
					researcher.addPinned(msg.key, msg.stage);
				else
					researcher.removePinned(msg.key, msg.stage);
		});
		supplier.get().setPacketHandled(true);
	}
	
	public enum Diff{
		pin, unpin
	}
}