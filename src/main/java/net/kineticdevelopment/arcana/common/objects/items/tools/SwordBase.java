package net.kineticdevelopment.arcana.common.objects.items.tools;

import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.minecraft.item.ItemSword;
import static net.kineticdevelopment.arcana.core.Main.TAB_ARCANA;

/**
 * Basic Sword, all Swords should either use this or extends this
 *
 * @author Wilkon
 */

public class SwordBase extends ItemSword implements IHasModel {
    public SwordBase(String name, ToolMaterial material){
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
