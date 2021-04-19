package net.arcanamod.items.settings;

import net.arcanamod.items.armor.GogglesItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;

public enum GogglePriority{
	SHOW_NONE,
	SHOW_NODE,
	SHOW_ASPECTS;
	
	public static GogglePriority getClientGogglePriority(){
		ClientPlayerEntity player = Minecraft.getInstance().player;
		return !(player == null) && !player.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty() && player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() instanceof GogglesItem ? ((GogglesItem)player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem()).priority : SHOW_NONE;
	}
}
