package net.arcanamod.network;

import net.arcanamod.items.ArcanaItems;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PkGetNote{
	
	String entryName;
	ResourceLocation researchLocation;
	
	public PkGetNote(ResourceLocation id, String entryName){
		this.researchLocation = id;
		this.entryName = entryName;
	}
	
	public static void encode(PkGetNote msg, PacketBuffer buffer){
		buffer.writeResourceLocation(msg.researchLocation);
		buffer.writeString(msg.entryName);
	}
	
	public static PkGetNote decode(PacketBuffer buffer){
		return new PkGetNote(buffer.readResourceLocation(), buffer.readString());
	}
	
	public static void handle(PkGetNote msg, Supplier<NetworkEvent.Context> supplier){
		supplier.get().enqueueWork(() -> {
			ServerPlayerEntity epm = supplier.get().getSender();
			if(epm.inventory.count(ArcanaItems.SCRIBING_TOOLS.get()) > 0 && epm.inventory.count(Items.PAPER) > 0){
				// if I can give research note
				ItemStack in = new ItemStack(ArcanaItems.RESEARCH_NOTE.get(), 1);
				CompoundNBT nbt = new CompoundNBT();
				nbt.putString("puzzle", msg.researchLocation.toString());
				nbt.putString("research", msg.entryName);
				in.setTag(nbt);
				if(epm.inventory.addItemStackToInventory(in)){
					// give it -- done
					// damage ink
					for(int i = 0; i < epm.inventory.getSizeInventory(); i++){
						ItemStack stack = epm.inventory.getStackInSlot(i);
						if(stack.getItem() == ArcanaItems.SCRIBING_TOOLS.get()){
							stack.damageItem(1, epm, null);
							break;
						}
					}
					// remove paper -- done
					
					epm.inventory.func_234564_a_(e -> e.getItem() == Items.PAPER, 1, epm.container.func_234641_j_());
				}
				// tell client?
			}
		});
		supplier.get().setPacketHandled(true);
	}
}
