package net.arcanamod.blocks.bases;

import net.arcanamod.blocks.OreDictEntry;
import net.arcanamod.util.IHasModel;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;

/**
 * Basic Stairs, all stairs should either be this, or extend it
 *
 * @author Tea
 */
public class StairsBase extends StairsBlock implements IHasModel, OreDictEntry{
	
	public StairsBase(String name, BlockState state){
		super(state);
		setUnlocalizedName(name);
		setRegistryName(name);
		setLightOpacity(0);
		ArcanaBlocks.BLOCKS.add(this);
		ArcanaItems.ITEMS.add(new BlockItem(this).setRegistryName(getRegistryName()));
	}
	
	public String getOreDictName(){
		return "stairWood";
	}
	
	@Override
	public void registerModels(){
		Arcana.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}