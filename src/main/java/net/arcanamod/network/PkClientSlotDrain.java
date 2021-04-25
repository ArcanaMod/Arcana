package net.arcanamod.network;

import net.arcanamod.Arcana;
import net.arcanamod.containers.AspectContainer;
import net.arcanamod.containers.slots.AspectSlot;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PkClientSlotDrain {

	public static final Logger LOGGER = LogManager.getLogger();

	int windowId;
	int slotId;
	PkAspectClick.ClickType type;

	public PkClientSlotDrain(int windowId, int slotId, PkAspectClick.ClickType type){
		this.windowId = windowId;
		this.slotId = slotId;
		this.type = type;
	}

	public static void encode(PkClientSlotDrain msg, PacketBuffer buffer){
		buffer.writeInt(msg.windowId);
		buffer.writeInt(msg.slotId);
		buffer.writeEnumValue(msg.type);
	}

	public static PkClientSlotDrain decode(PacketBuffer buffer){
		return new PkClientSlotDrain(buffer.readInt(),buffer.readInt(), buffer.readEnumValue(PkAspectClick.ClickType.class));
	}

	public static void handle(PkClientSlotDrain msg, Supplier<NetworkEvent.Context> supplier){
		// on server
		supplier.get().enqueueWork(() -> {
			ClientPlayerEntity epm = (ClientPlayerEntity)Arcana.proxy.getPlayerOnClient();
			if(epm.openContainer.windowId == msg.windowId){
				// decrease/increase whats held on the client
				// rename to PktAspectClickConfirmed
				AspectContainer container = (AspectContainer)epm.openContainer;
				if(container.getAspectSlots().size() > msg.slotId){
					AspectSlot slot = container.getAspectSlots().get(msg.slotId);
					if((msg.type == PkAspectClick.ClickType.TAKE || msg.type == PkAspectClick.ClickType.TAKE_ALL) && (container.getHeldAspect() == null || container.getHeldAspect() == slot.getAspect()) && slot.getAmount() > 0){
						float drain = msg.type == PkAspectClick.ClickType.TAKE_ALL ? slot.getAmount() : 1;
						slot.drain(slot.getAspect(), drain, false);
					}else if((msg.type == PkAspectClick.ClickType.PUT || msg.type == PkAspectClick.ClickType.PUT_ALL) && container.getHeldAspect() != null && container.getHeldCount() > 0 && (slot.getAspect() == container.getHeldAspect() || slot.getAspect() == null)){
						float drain = msg.type == PkAspectClick.ClickType.PUT_ALL ? container.getHeldCount() : 1;
						slot.insert(slot.getAspect(), drain, false);
					}
				}else{
					LOGGER.error(String.format("Tried to click on invalid aspect slot; out of bounds! (size: %d, slot index: %d).", container.getAspectSlots().size(), msg.slotId));
				}
			}
		});
		supplier.get().setPacketHandled(true);
	}
}
