package net.kineticdevelopment.arcana.common.network.inventory;

import io.netty.buffer.ByteBuf;
import net.kineticdevelopment.arcana.client.gui.AspectSlot;
import net.kineticdevelopment.arcana.common.containers.AspectContainer;
import net.kineticdevelopment.arcana.core.aspects.Aspect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.kineticdevelopment.arcana.common.network.inventory.PktAspectClickConfirmHandler.*;

public class PktAspectClickConfirmHandler implements IMessageHandler<PktAspectClickConfirm, IMessage>{
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	// so, what do I need to sync?
	// 1. held aspect & held count
	// 2. the contents of every slot
	// regular slots can be synced just by syncing the underlying AspectHandler
	// store slots need that + their own aspect
	
	public IMessage onMessage(PktAspectClickConfirm message, MessageContext ctx){
		Minecraft.getMinecraft().addScheduledTask(() -> {
			EntityPlayerSP eps = Minecraft.getMinecraft().player;
			AspectContainer container = (AspectContainer)eps.openContainer;
			container.setHeldAspect(message.heldAspect);
			container.setHeldCount(message.heldCount);
			for(Pair<Integer, Aspect> storeSlot : message.storeSlotAspects)
				container.getAspectSlots().get(storeSlot.getLeft()).setAspect(storeSlot.getRight());
			for(Pair<Integer, NBTTagCompound> handler : message.handlers)
				container.getOpenHandlers().get(handler.getLeft()).deserializeNBT(handler.getRight());
		});
		return null;
	}
	
	public static class PktAspectClickConfirm implements IMessage{
		
		int heldCount;
		Aspect heldAspect = null;
		List<Pair<Integer, NBTTagCompound>> handlers = new ArrayList<>();
		List<Pair<Integer, Aspect>> storeSlotAspects = new ArrayList<>();
		
		public PktAspectClickConfirm(){}
		
		public PktAspectClickConfirm(AspectContainer container){
			heldCount = container.getHeldCount();
			heldAspect = container.getHeldAspect();
			
			for(int i = 0; i < container.getOpenHandlers().size(); i++)
				handlers.add(Pair.of(i, container.getOpenHandlers().get(i).serializeNBT()));
			
			for(int i = 0; i < container.getAspectSlots().size(); i++){
				AspectSlot slot = container.getAspectSlots().get(i);
				if(slot.storeSlot)
					storeSlotAspects.add(Pair.of(i, slot.getAspect()));
			}
		}
		
		public void fromBytes(ByteBuf buf){
			PacketBuffer pb = new PacketBuffer(buf);
			heldCount = pb.readInt();
			heldAspect = readAspect(pb);
			
			int handlerCount = pb.readInt();
			for(int i = 0; i < handlerCount; i++){
				int index = pb.readInt();
				NBTTagCompound data = null;
				try{
					data = pb.readCompoundTag();
				}catch(IOException e){
					e.printStackTrace();
					LOGGER.error("Could not send aspect data from server to client!");
				}
				handlers.add(Pair.of(index, data));
			}
			
			int storeSlots = pb.readInt();
			for(int i = 0; i < storeSlots; i++){
				int index = pb.readInt();
				Aspect aspect = readAspect(pb);
				storeSlotAspects.add(Pair.of(index, aspect));
			}
		}
		
		public void toBytes(ByteBuf buf){
			PacketBuffer pb = new PacketBuffer(buf);
			pb.writeInt(heldCount);
			writeAspect(pb, heldAspect);
			
			pb.writeInt(handlers.size());
			for(Pair<Integer, NBTTagCompound> handler : handlers){
				pb.writeInt(handler.getLeft());
				pb.writeCompoundTag(handler.getRight());
			}
			
			pb.writeInt(storeSlotAspects.size());
			for(Pair<Integer, Aspect> storeSlot : storeSlotAspects){
				pb.writeInt(storeSlot.getLeft());
				writeAspect(pb, storeSlot.getRight());
			}
		}
		
		private void writeAspect(PacketBuffer pb, Aspect aspect){
			pb.writeBoolean(aspect != null);
			if(aspect != null)
				pb.writeEnumValue(aspect);
		}
		
		private Aspect readAspect(PacketBuffer pb){
			if(pb.readBoolean())
				return pb.readEnumValue(Aspect.class);
			else
				return null;
		}
	}
}