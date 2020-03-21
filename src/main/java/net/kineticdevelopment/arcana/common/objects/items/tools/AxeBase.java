package net.kineticdevelopment.arcana.common.objects.items.tools;

import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.minecraft.item.ItemAxe;
import static net.kineticdevelopment.arcana.core.Main.TAB_ARCANA;

/**
 * Basic Axe, all Axes should either use this or extends this
 *
 * @author Wilkon
 */


public class AxeBase extends ItemAxe implements IHasModel {
    public AxeBase(String name, ToolMaterial material){
        super(material, 9.0F, -3.2F);
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
