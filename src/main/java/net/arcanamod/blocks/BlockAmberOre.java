package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.bases.BlockBase;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockAmberOre extends BlockBase{
	
	public BlockAmberOre(){
		super("amber_ore", Material.ROCK, MapColor.GRAY);
	}
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune){
		return ArcanaItems.AMBER;
	}
	
	public int quantityDropped(IBlockState state, int fortune, Random random){
		if(fortune > 0)
			return random.nextInt(fortune + 2) + 1;
		else
			return quantityDropped(random);
	}
	
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune){
		Random rand = world instanceof World ? ((World)world).rand : new Random();
		return MathHelper.getInt(rand, 2, 5);
	}
}