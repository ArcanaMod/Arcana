package net.arcanamod.network;

import net.arcanamod.containers.AspectContainer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PkRequestAspectSync {

	public static final Logger LOGGER = LogManager.getLogger();

	public PkRequestAspectSync(){

	}

	/*public static void encode(PkTryAdvance msg, PacketBuffer buffer){

	}

	public static PkRequestAspectSync decode(PacketBuffer buffer){

	}

	public static void handle(PkTryAdvance msg, Supplier<NetworkEvent.Context> supplier){
		// client -> server
		if(supplier.get().getServerHandler().player.openContainer instanceof AspectContainer)
			return new PkSyncAspectContainer((AspectContainer)ctx.getServerHandler().player.openContainer);
		else{
			LOGGER.error("Requested sync packet for aspect handling container, but the open GUI is not an aspect handling container GUI!");
			return null;
		}
		supplier.get().setPacketHandled(true);
	}*/
}
