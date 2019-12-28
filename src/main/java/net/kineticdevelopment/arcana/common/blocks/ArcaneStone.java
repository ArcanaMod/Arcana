package net.kineticdevelopment.arcana.common.blocks;

import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.init.blockinit;
import net.kineticdevelopment.arcana.init.iteminit;
import net.kineticdevelopment.arcana.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class ArcaneStone extends Block implements IHasModel
{
	public ArcaneStone(String name, Material material) 
	{
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		this.setSoundType(SoundType.STONE);
        this.setHardness(3.0f);
        this.setResistance(3.0f);
		
		blockinit.BLOCKS.add(this);
		iteminit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	@Override
	public void registerModels() 
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
