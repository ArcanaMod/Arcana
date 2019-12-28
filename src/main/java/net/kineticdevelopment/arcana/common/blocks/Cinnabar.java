package net.kineticdevelopment.arcana.common.blocks;

import java.util.Random;

import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.init.blockinit;
import net.kineticdevelopment.arcana.init.iteminit;
import net.kineticdevelopment.arcana.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class Cinnabar extends Block implements IHasModel
{
	public Cinnabar(String name, Material material) 
	{
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		this.setHardness(1.0F);
		this.setResistance(1.0F);
		this.setHarvestLevel("pickaxe", 2);
		
		blockinit.BLOCKS.add(this);
		iteminit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
		return iteminit.QUICKSILVER;
    }
	
	@Override
	public void registerModels() 
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
