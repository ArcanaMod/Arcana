package net.arcanamod.network.inventory;

import io.netty.buffer.ByteBuf;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PktGetNoteHandler implements IMessageHandler<PktGetNoteHandler.PktGetNote, IMessage>{
	
	public IMessage onMessage(PktGetNote message, MessageContext ctx){
		// on server
		Minecraft.getMinecraft().addScheduledTask(() -> {
			EntityPlayerMP epm = ctx.getServerHandler().player;
			if(epm.inventory.clearMatchingItems(ArcanaItems.INK, -1, 0, null) > 0 && epm.inventory.clearMatchingItems(Items.PAPER, -1, 0, null) > 0){
				// if I can give research note
				ItemStack in = new ItemStack(ArcanaItems.RESEARCH_NOTE, 1);
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("puzzle", message.getKey().toString());
				nbt.setString("research", message.getResearchKey().toString());
				in.setTagCompound(nbt);
				if(epm.inventory.addItemStackToInventory(in)){
					// give it -- done
					// damage ink
					for(int i = 0; i < epm.inventory.getSizeInventory(); i++){
						ItemStack stack = epm.inventory.getStackInSlot(i);
						if(stack.getItem() == ArcanaItems.INK){
							stack.damageItem(1, epm);
							break;
						}
					}
					// remove paper -- done
					epm.inventory.clearMatchingItems(Items.PAPER, -1, 1, null);
				}
				// tell client?
			}
		});
		return null;
	}
	
	public static class PktGetNote implements IMessage{
		
		protected String entryKey;
		protected String researchKey;
		
		public PktGetNote(){
		}
		
		public PktGetNote(String entryKey, String researchKey){
			this.entryKey = entryKey;
			this.researchKey = researchKey;
		}
		
		public ResourceLocation getKey(){
			return new ResourceLocation(entryKey);
		}
		
		public ResourceLocation getResearchKey(){
			return new ResourceLocation(researchKey);
		}
		
		public void fromBytes(ByteBuf buf){
			PacketBuffer pb = new PacketBuffer(buf);
			entryKey = pb.readString(32767);
			researchKey = pb.readString(32767);
		}
		
		public void toBytes(ByteBuf buf){
			PacketBuffer pb = new PacketBuffer(buf);
			pb.writeString(entryKey);
			pb.writeString(researchKey);
		}
	}
}