package net.arcanamod.blocks.bases.tainted;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.OreDictEntry;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSlab;

/**
 * Basic Tainted Half Slab, all tainted half slabs should either be this, or extend it
 *
 * @author Mozaran
 * @see TaintedSlabBase
 */
public class TaintedHalfSlabBase extends TaintedSlabBase implements IHasModel, OreDictEntry{
	public TaintedHalfSlabBase(String name, Material material, Block doubleSlab){
		super(name, material);
		ArcanaItems.ITEMS.add(new ItemSlab(this, this, (SlabBlock)doubleSlab).setRegistryName(this.getRegistryName()));
	}
	
	public String getOreDictName(){
		return "slabWood";
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
