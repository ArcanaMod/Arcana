package net.arcanamod.network;

import net.arcanamod.blocks.tiles.FociForgeTileEntity;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PkWriteSpellToFoci {

	ItemStack focus;
	CompoundNBT focusNBT;

	public PkWriteSpellToFoci(ItemStack focus, CompoundNBT focusNBT){
		this.focus = focus;
		this.focusNBT = focusNBT;
	}

	public static void encode(PkWriteSpellToFoci msg, PacketBuffer buffer){
		buffer.writeCompoundTag(msg.focusNBT);
		buffer.writeItemStack(msg.focus);
	}

	public static PkWriteSpellToFoci decode(PacketBuffer buffer){
		return new PkWriteSpellToFoci(buffer.readItemStack(), buffer.readCompoundTag());
	}

	public static void handle(PkWriteSpellToFoci msg, Supplier<NetworkEvent.Context> supplier){
		supplier.get().enqueueWork(() -> {
			msg.focus.getOrCreateTag().put("spell", msg.focusNBT);
		});
		supplier.get().setPacketHandled(true);
	}
}
