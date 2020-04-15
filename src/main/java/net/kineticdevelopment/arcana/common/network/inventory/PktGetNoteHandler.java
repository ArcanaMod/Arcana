package net.kineticdevelopment.arcana.common.network.inventory;

import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.common.network.StringPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PktGetNoteHandler implements IMessageHandler<PktGetNoteHandler.PktGetNote, IMessage>{
	
	public IMessage onMessage(PktGetNote message, MessageContext ctx){
		// on server
		Minecraft.getMinecraft().addScheduledTask(() -> {
			EntityPlayerMP epm = ctx.getServerHandler().player;
			if(epm.inventory.clearMatchingItems(ItemInit.INK, -1, 0, null) > 0
				&& epm.inventory.clearMatchingItems(Items.PAPER, -1, 0, null) > 0){
				// if I can give research note
				ItemStack in = new ItemStack(ItemInit.RESEARCH_NOTE, 1);
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("puzzle", message.getKey().toString());
				in.setTagCompound(nbt);
				if(epm.inventory.addItemStackToInventory(in)){
					// give it -- done
					// damage ink
					for(int i = 0; i < epm.inventory.getSizeInventory(); i++){
						ItemStack stack = epm.inventory.getStackInSlot(i);
						if(stack.getItem() == ItemInit.INK){
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
	
	public static class PktGetNote extends StringPacket{
		
		public PktGetNote(){}
		
		public PktGetNote(ResourceLocation entryKey){
			this.entryKey = entryKey.toString();
		}
		
		public ResourceLocation getKey(){
			return new ResourceLocation(entryKey);
		}
	}
}