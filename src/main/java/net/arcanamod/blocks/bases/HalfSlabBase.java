package net.arcanamod.blocks.bases;

import net.arcanamod.util.IHasModel;
import net.arcanamod.Arcana;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSlab;

/**
 * Basic Half Slab, all half slabs should either be this, or extend it
 *
 * @author Tea, Mozaran
 */
public class HalfSlabBase extends SlabBase implements IHasModel{
	
	public HalfSlabBase(String name, Material material, Block doubleSlab){
		super(name, material);
		ArcanaItems.ITEMS.add(new ItemSlab(this, this, (BlockSlab)doubleSlab).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public boolean isDouble(){
		return false;
	}
	
	@Override
	public void registerModels(){
		Arcana.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
