package net.arcanamod.blocks.bases;

import net.arcanamod.blocks.OreDictEntry;
import net.arcanamod.util.IHasModel;
import net.arcanamod.Arcana;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSlab;

import javax.annotation.Nonnull;

/**
 * Basic Half Slab, all half slabs should either be this, or extend it
 *
 * @author Tea, Mozaran
 */
public class HalfSlabBase extends SlabBase implements IHasModel, OreDictEntry{
	
	public HalfSlabBase(String name, Material material, Block doubleSlab){
		super(name, material);
		ArcanaItems.ITEMS.add(new ItemSlab(this, this, (SlabBlock)doubleSlab).setRegistryName(this.getRegistryName()));
	}
	
	@Nonnull
	public String getOreDictName(){
		return "slabWood";
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
