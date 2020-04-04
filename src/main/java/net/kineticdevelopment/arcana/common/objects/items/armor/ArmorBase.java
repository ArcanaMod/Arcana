package net.kineticdevelopment.arcana.common.objects.items.armor;

import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

/**
 * Basic Armor, all Armor should either use this, or extend it
 *
 * I am screaming because it is Armour WITH A U!
 *
 * @author Wilkon
 */

public class ArmorBase extends ItemArmor implements IHasModel {
    public ArmorBase(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
        super(materialIn, renderIndexIn,equipmentSlotIn);
        setUnlocalizedName(name);
        setRegistryName(name);

    ItemInit.ITEMS.add(this);
}

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
