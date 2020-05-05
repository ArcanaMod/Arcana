package net.arcanamod.network.inventory;

import io.netty.buffer.ByteBuf;
import net.arcanamod.containers.AspectContainer;
import net.arcanamod.containers.AspectSlot;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.arcanamod.network.inventory.PktAspectClickHandler.PktAspectClick;

public class PktAspectClickHandler implements IMessageHandler<PktAspectClick, PktSyncAspectContainerHandler.PktSyncAspectContainer>{
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	public PktSyncAspectContainerHandler.PktSyncAspectContainer onMessage(PktAspectClick message, MessageContext ctx){
		// on server
		ServerPlayerEntity epm = ctx.getServerHandler().player;
		if(epm.openContainer.windowId == message.windowId){
			// decrease/increase whats held on the server
			// send back a PktAspectClickConfirmed with new heldAspect and new heldCount for client
			AspectContainer container = (AspectContainer)epm.openContainer;
			if(container.getAspectSlots().size() > message.slotId){
				AspectSlot slot = container.getAspectSlots().get(message.slotId);
				if((message.type == ClickType.TAKE || message.type == ClickType.TAKE_ALL) && (container.getHeldAspect() == null || container.getHeldAspect() == slot.getAspect()) && slot.getAmount() > 0){
					container.setHeldAspect(slot.getAspect());
					int drain = message.type == ClickType.TAKE_ALL ? slot.getAmount() : 1;
					container.setHeldCount(container.getHeldCount() + slot.drain(slot.getAspect(), drain, false));
					if(slot.getAmount() <= 0 && slot.storeSlot)
						slot.setAspect(null);
					slot.onChange();
				}else if((message.type == ClickType.PUT || message.type == ClickType.PUT_ALL) && container.getHeldAspect() != null && container.getHeldCount() > 0 && (slot.getAspect() == container.getHeldAspect() || slot.getAspect() == null)){
					int drain = message.type == ClickType.PUT_ALL ? container.getHeldCount() : 1;
					if(slot.getAspect() == null && slot.storeSlot)
						slot.setAspect(container.getHeldAspect());
					container.setHeldCount(container.getHeldCount() - (drain - slot.insert(slot.getAspect(), drain, false)));
					if(container.getHeldCount() <= 0){
						container.setHeldCount(0);
						container.setHeldAspect(null);
					}
					slot.onChange();
				}
				container.onAspectSlotChange();
				return new PktSyncAspectContainerHandler.PktSyncAspectContainer(container);
			}else{
				LOGGER.error(String.format("Tried to click on invalid aspect slot; out of bounds! (size: %d, slot index: %d).", container.getAspectSlots().size(), message.slotId));
			}
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
		
		public PktAspectClick(){
		}
		
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