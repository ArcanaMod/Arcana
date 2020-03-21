package net.kineticdevelopment.arcana.common.objects.items.tools;

import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.minecraft.item.ItemHoe;

import static net.kineticdevelopment.arcana.core.Main.TAB_ARCANA;

/**
 * Basic Hoe, all hoes should either use this or extends this
 *
 * This is a basic hoe, get yourself a nice girl instead.
 *
 * @author Wilkon
 */

public class HoeBase extends ItemHoe implements IHasModel {
    public HoeBase(String name, ToolMaterial material){
        super(material);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(TAB_ARCANA);

        ItemInit.ITEMS.add(this);
    }

    @Override
    public void registerModels() {

        Main.proxy.registerItemRenderer(this,0,"inventory");
    }
}
