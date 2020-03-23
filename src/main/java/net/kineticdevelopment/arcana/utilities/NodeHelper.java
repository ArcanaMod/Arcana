package net.kineticdevelopment.arcana.utilities;

import net.kineticdevelopment.arcana.common.objects.items.armor.GoggleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.EntityEquipmentSlot;

public class NodeHelper {

    public static GogglePriority getGogglePriority() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        return !player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()
                ? ((GoggleBase)player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem()).priority
                : GogglePriority.SHOW_NONE;
    }
}
