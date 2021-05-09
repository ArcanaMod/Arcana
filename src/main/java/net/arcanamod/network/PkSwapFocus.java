package net.arcanamod.network;

import net.arcanamod.items.MagicDeviceItem;
import net.arcanamod.items.WandItem;
import net.arcanamod.items.attachment.FocusItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PkSwapFocus{
	
	Hand wandHand;
	int newFocusIndex;
	
	public PkSwapFocus(Hand wandHand, int newFocusIndex){
		this.wandHand = wandHand;
		this.newFocusIndex = newFocusIndex;
	}
	
	public static void encode(PkSwapFocus msg, PacketBuffer buffer){
		buffer.writeEnumValue(msg.wandHand);
		buffer.writeVarInt(msg.newFocusIndex);
	}
	
	public static PkSwapFocus decode(PacketBuffer buffer){
		return new PkSwapFocus(buffer.readEnumValue(Hand.class), buffer.readVarInt());
	}
	
	public static void handle(PkSwapFocus msg, Supplier<NetworkEvent.Context> supplier){
		supplier.get().enqueueWork(() -> {
			ServerPlayerEntity spe = supplier.get().getSender();
			ItemStack wandStack = spe.getHeldItem(msg.wandHand);
			// Give player the old focus
			if(msg.newFocusIndex >= 0){
				// Set the wand focus of the wand
				List<ItemStack> foci = getAllFociStacks(spe);
				MagicDeviceItem.getFocusStack(wandStack).ifPresent(spe.inventory::addItemStackToInventory);
				ItemStack focus = foci.get(msg.newFocusIndex);
				MagicDeviceItem.setFocusFromStack(wandStack, focus);
				// Remove the stack from the inventory
				spe.inventory.func_234564_a_(stack -> stack == focus, 1, spe.container.func_234641_j_());
			}else{
				MagicDeviceItem.getFocusStack(wandStack).ifPresent(spe.inventory::addItemStackToInventory);
				MagicDeviceItem.setFocusFromStack(wandStack, ItemStack.EMPTY);
			}
		});
		supplier.get().setPacketHandled(true);
	}
	
	private static List<ItemStack> getAllFociStacks(ServerPlayerEntity spe){
		//TODO: focus pouch?
		List<ItemStack> foci = new ArrayList<>();
		for(int i = 0; i < spe.inventory.getSizeInventory(); i++){
			ItemStack stack = spe.inventory.getStackInSlot(i);
			if(stack.getItem() instanceof FocusItem)
				foci.add(stack);
		}
		return foci;
	}
}