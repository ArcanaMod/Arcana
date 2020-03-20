package net.kineticdevelopment.arcana.common.objects.items.tools;

import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.minecraft.item.ItemSpade;
import static net.kineticdevelopment.arcana.core.Main.TAB_ARCANA;

/**
 * Basic Shovel, all Shovels should either use this or extends this
 *
 * @author Wilkon
 */

public class ShovelBase extends ItemSpade implements IHasModel {
    public ShovelBase(String name, ToolMaterial material){
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
