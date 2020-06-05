package net.arcanamod.network;

import net.arcanamod.items.ArcanaItems;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class PkGetNoteHandler {

	String entryName;
	ResourceLocation researchLocation;

	public PkGetNoteHandler(ResourceLocation id, String entryName) {
		this.researchLocation = id;
		this.entryName = entryName;
	}

	public static void encode(PkGetNoteHandler msg, PacketBuffer buffer){
		buffer.writeResourceLocation(msg.researchLocation);
		buffer.writeString(msg.entryName);
	}

	public static PkGetNoteHandler decode(PacketBuffer buffer){
		return new PkGetNoteHandler(buffer.readResourceLocation(),buffer.readString());
	}

	public static void handle(PkGetNoteHandler msg, Supplier<NetworkEvent.Context> supplier)
	{
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
							stack.damageItem(1, epm,null);
							break;
						}
					}
					// remove paper -- done

					epm.inventory.clearMatchingItems(e -> e.getItem()==Items.PAPER, 1);
				}
				// tell client?
			}
		});
	}
}
