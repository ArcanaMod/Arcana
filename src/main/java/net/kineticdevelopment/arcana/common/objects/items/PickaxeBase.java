package net.kineticdevelopment.arcana.common.objects.items.tools;

import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.minecraft.item.ItemPickaxe;

import static net.kineticdevelopment.arcana.core.Main.TAB_ARCANA;

/**
 * Basic Pickaxe, all Pickaxes should either use this or extends this
 *
 * @author Wilkon
 */

public class PickaxeBase extends ItemPickaxe implements IHasModel {
    public PickaxeBase(String name, ToolMaterial material){
        super(material);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(TAB_ARCANA);

        ItemInit.ITEMS.add(this);
    }

    @Override
    public void registerModels() {

        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
