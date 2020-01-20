package net.kineticdevelopment.arcana.core;

import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.common.items.ItemWand;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab {

    public static CreativeTabs ARCANA = (new CreativeTabs("tabArcana") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemInit.WAND);
        }
    });



}
