package net.arcanamod.blocks.bases;

import net.arcanamod.blocks.OreDictEntry;
import net.arcanamod.util.IHasModel;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

/**
 * Basic Stairs, all stairs should either be this, or extend it
 *
 * @author Tea
 */
public class StairsBase extends BlockStairs implements IHasModel, OreDictEntry{
	
	public StairsBase(String name, IBlockState state){
		super(state);
		setUnlocalizedName(name);
		setRegistryName(name);
		setLightOpacity(0);
		ArcanaBlocks.BLOCKS.add(this);
		ArcanaItems.ITEMS.add(new ItemBlock(this).setRegistryName(getRegistryName()));
	}
	
	public String getOreDictName(){
		return "stairWood";
	}
	
	@Override
	public void registerModels(){
		Arcana.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}