package net.arcanamod.blocks.bases.tainted;

import net.arcanamod.util.IHasModel;
import net.arcanamod.Arcana;
import net.arcanamod.items.ArcanaItems;
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
public class TaintedHalfSlabBase extends TaintedSlabBase implements IHasModel{
	public TaintedHalfSlabBase(String name, Material material, Block doubleSlab){
		super(name, material);
		ArcanaItems.ITEMS.add(new ItemSlab(this, this, (BlockSlab)doubleSlab).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public void registerModels(){
		Arcana.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
	
	@Override
	public boolean isDouble(){
		return false;
	}
}
