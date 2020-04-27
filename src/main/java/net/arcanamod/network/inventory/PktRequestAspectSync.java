package net.arcanamod.network.inventory;

import io.netty.buffer.ByteBuf;
import net.arcanamod.containers.AspectContainer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Packet handling has never been so easy!
public class PktRequestAspectSync implements IMessage, IMessageHandler<PktRequestAspectSync, PktSyncAspectContainerHandler.PktSyncAspectContainer>{
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	public void fromBytes(ByteBuf buf){
	}
	
	public void toBytes(ByteBuf buf){
	}
	
	public PktSyncAspectContainerHandler.PktSyncAspectContainer onMessage(PktRequestAspectSync message, MessageContext ctx){
		// client -> server
		if(ctx.getServerHandler().player.openContainer instanceof AspectContainer)
			return new PktSyncAspectContainerHandler.PktSyncAspectContainer((AspectContainer)ctx.getServerHandler().player.openContainer);
		else{
			LOGGER.error("Requested sync packet for aspect handling container, but the open GUI is not an aspect handling container GUI!");
			return null;
		}
	}
}