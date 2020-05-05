package net.arcanamod.blocks.bases;

import net.arcanamod.blocks.bases.tainted.TaintedBlockBase;
import net.arcanamod.util.IHasModel;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

/**
 * Basic Block, all blocks should either be this, or extend it
 *
 * @author Atlas
 * @see TaintedBlockBase
 */
public class BlockBase extends Block implements IHasModel{
	
	public BlockBase(String name, Material material, MaterialColor colour){
		super(material, colour);
		setUnlocalizedName(name);
		setRegistryName(name);
		
		ArcanaBlocks.BLOCKS.add(this);
		ArcanaItems.ITEMS.add(new BlockItem(this).setRegistryName(this.getRegistryName()));
	}
	
	public BlockBase(String name, Material material){
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		
		ArcanaBlocks.BLOCKS.add(this);
		ArcanaItems.ITEMS.add(new BlockItem(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public void registerModels(){
		Arcana.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
