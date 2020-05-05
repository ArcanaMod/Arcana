package net.arcanamod.util;

import net.arcanamod.items.armor.GoggleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class NodeHelper{
	
	public static GogglePriority getGogglePriority(){
		ClientPlayerEntity player = Minecraft.getInstance().player;
		return !player.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty() && player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() instanceof GoggleBase ? ((GoggleBase)player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem()).priority : GogglePriority.SHOW_NONE;
	}
}
