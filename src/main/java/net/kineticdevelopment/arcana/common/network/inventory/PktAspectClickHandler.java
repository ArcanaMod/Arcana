package net.kineticdevelopment.arcana.common.network.inventory;

import io.netty.buffer.ByteBuf;
import net.kineticdevelopment.arcana.common.containers.AspectSlot;
import net.kineticdevelopment.arcana.common.containers.AspectContainer;
import net.kineticdevelopment.arcana.common.network.inventory.PktSyncAspectContainerHandler.PktSyncAspectContainer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static net.kineticdevelopment.arcana.common.network.inventory.PktAspectClickHandler.PktAspectClick;

public class PktAspectClickHandler implements IMessageHandler<PktAspectClick, PktSyncAspectContainer>{
	
	public PktSyncAspectContainer onMessage(PktAspectClick message, MessageContext ctx){
		// on server
		EntityPlayerMP epm = ctx.getServerHandler().player;
		if(epm.openContainer.windowId == message.windowId){
			// decrease/increase whats held on the server
			// send back a PktAspectClickConfirmed with new heldAspect and new heldCount for client
			AspectContainer container = (AspectContainer)epm.openContainer;
			AspectSlot slot = container.getAspectSlots().get(message.slotId);
			
			if((message.type == ClickType.TAKE || message.type == ClickType.TAKE_ALL) && (container.getHeldAspect() == null || container.getHeldAspect() == slot.getAspect()) && slot.getAmount() > 0){
				container.setHeldAspect(slot.getAspect());
				int drain = message.type == ClickType.TAKE_ALL ? slot.getAmount() : 1;
				container.setHeldCount(container.getHeldCount() + slot.drain(slot.getAspect(), drain, false));
				if(slot.getAmount() <= 0 && slot.storeSlot)
					slot.setAspect(null);
				slot.sync();
			}else if((message.type == ClickType.PUT || message.type == ClickType.PUT_ALL) && container.getHeldAspect() != null && container.getHeldCount() > 0 && (slot.getAspect() == container.getHeldAspect() || slot.getAspect() == null)){
				int drain = message.type == ClickType.PUT_ALL ? container.getHeldCount() : 1;
				if(slot.getAspect() == null && slot.storeSlot)
					slot.setAspect(container.getHeldAspect());
				container.setHeldCount(container.getHeldCount() - (drain - slot.insert(slot.getAspect(), drain, false)));
				if(container.getHeldCount() <= 0){
					container.setHeldCount(0);
					container.setHeldAspect(null);
				}
				slot.sync();
			}
			return new PktSyncAspectContainer(container);
		}
		return null;
	}
	
	public static class PktAspectClick implements IMessage{
		
		int windowId;
		int slotId;
		ClickType type;
		
		public PktAspectClick(int windowId, int slotId, ClickType type){
			this.windowId = windowId;
			this.slotId = slotId;
			this.type = type;
		}
		
		public PktAspectClick(){}
		
		public void fromBytes(ByteBuf buf){
			PacketBuffer pb = new PacketBuffer(buf);
			windowId = pb.readInt();
			slotId = pb.readInt();
			type = pb.readEnumValue(ClickType.class);
		}
		
		public void toBytes(ByteBuf buf){
			PacketBuffer pb = new PacketBuffer(buf);
			pb.writeInt(windowId);
			pb.writeInt(slotId);
			pb.writeEnumValue(type);
		}
	}
	
	public enum ClickType{
		TAKE,
		PUT,
		TAKE_ALL,
		PUT_ALL
	}
}