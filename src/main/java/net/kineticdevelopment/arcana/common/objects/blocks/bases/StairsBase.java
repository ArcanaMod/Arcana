package net.kineticdevelopment.arcana.common.objects.blocks.bases;

import net.kineticdevelopment.arcana.common.blocks.OreDictEntry;
import net.kineticdevelopment.arcana.common.init.BlockInit;
import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.oredict.OreDictionary;

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
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(getRegistryName()));
	}
	
	public String getOreDictName(){
		return "stairWood";
	}
	
	@Override
	public void registerModels(){
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}