package net.kineticdevelopment.arcana.common.objects.items.armor;

import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.GogglePriority;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class GoggleBase extends ItemArmor {

    public GogglePriority priority;

    public GoggleBase(String name, ArmorMaterial materialIn, int renderIndexIn, GogglePriority priority) {

        super(materialIn, renderIndexIn, EntityEquipmentSlot.HEAD);

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(Main.TAB_ARCANA);
        this.priority = priority;
        ItemInit.ITEMS.add(this);
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }



}
