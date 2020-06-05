package net.arcanamod.network;

import io.netty.buffer.ByteBuf;
import net.arcanamod.containers.AspectContainer;
import net.arcanamod.containers.AspectSlot;
import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.ResearchEntry;
import net.arcanamod.research.Researcher;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PkAspectClick {

	public static final Logger LOGGER = LogManager.getLogger();

	int windowId;
	int slotId;
	ClickType type;

	public PkAspectClick(int windowId, int slotId, ClickType type){
		this.windowId = windowId;
		this.slotId = slotId;
		this.type = type;
	}

	public static void encode(PkAspectClick msg, PacketBuffer buffer){
		buffer.writeInt(msg.windowId);
		buffer.writeInt(msg.slotId);
		buffer.writeEnumValue(msg.type);
	}

	public static PkAspectClick decode(PacketBuffer buffer){
		return new PkAspectClick(buffer.readInt(),buffer.readInt(), buffer.readEnumValue(ClickType.class));
	}

	public static void handle(PkAspectClick msg, Supplier<NetworkEvent.Context> supplier){
		// on server
		supplier.get().enqueueWork(() -> {
			ServerPlayerEntity epm = supplier.get().getSender();
			if(epm.openContainer.windowId == msg.windowId){
				// decrease/increase whats held on the server
				// send back a PktAspectClickConfirmed with new heldAspect and new heldCount for client
				AspectContainer container = (AspectContainer)epm.openContainer;
				if(container.getAspectSlots().size() > msg.slotId){
					AspectSlot slot = container.getAspectSlots().get(msg.slotId);
					if((msg.type == ClickType.TAKE || msg.type == ClickType.TAKE_ALL) && (container.getHeldAspect() == null || container.getHeldAspect() == slot.getAspect()) && slot.getAmount() > 0){
						container.setHeldAspect(slot.getAspect());
						int drain = msg.type == ClickType.TAKE_ALL ? slot.getAmount() : 1;
						container.setHeldCount(container.getHeldCount() + slot.drain(slot.getAspect(), drain, false));
						if(slot.getAmount() <= 0 && slot.storeSlot)
							slot.setAspect(null);
						slot.onChange();
					}else if((msg.type == ClickType.PUT || msg.type == ClickType.PUT_ALL) && container.getHeldAspect() != null && container.getHeldCount() > 0 && (slot.getAspect() == container.getHeldAspect() || slot.getAspect() == null)){
						int drain = msg.type == ClickType.PUT_ALL ? container.getHeldCount() : 1;
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
					Connection.sendSyncAspectContainer(container, epm);
				}else{
					LOGGER.error(String.format("Tried to click on invalid aspect slot; out of bounds! (size: %d, slot index: %d).", container.getAspectSlots().size(), msg.slotId));
				}
			}
		});
		supplier.get().setPacketHandled(true);
	}

	public enum ClickType{
		TAKE,
		PUT,
		TAKE_ALL,
		PUT_ALL
	}
}
