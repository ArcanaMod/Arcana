package net.kineticdevelopment.arcana.common.objects.blocks.bases.tainted;

import net.kineticdevelopment.arcana.common.blocks.OreDictEntry;
import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSlab;

/**
 * Basic Tainted Half Slab, all tainted half slabs should either be this, or extend it
 *
 * @author Mozaran
 * @see TaintedSlabBase
 */
public class TaintedHalfSlabBase extends TaintedSlabBase implements IHasModel, OreDictEntry {
    public TaintedHalfSlabBase(String name, Material material, Block doubleSlab) {
        super(name, material);
        ItemInit.ITEMS.add(new ItemSlab(this, this, (BlockSlab) doubleSlab).setRegistryName(this.getRegistryName()));
    }

    public String getOreDictName(){
        return "slabWood";
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public boolean isDouble() {
        return false;
    }
}
