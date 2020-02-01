package net.kineticdevelopment.arcana.common.objects.blocks.bases;
import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSlab;

/**
 * Basic Half Slab, all half slabs should either be this, or extend it
 *
 * @author Tea, Mozaran
 *
 */
public class HalfSlabBase extends SlabBase implements IHasModel {

    public HalfSlabBase(String name, Material material, Block doubleSlab) {
        super(name, material);
        ItemInit.ITEMS.add(new ItemSlab(this, this, (BlockSlab) doubleSlab).setRegistryName(this.getRegistryName()));
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
