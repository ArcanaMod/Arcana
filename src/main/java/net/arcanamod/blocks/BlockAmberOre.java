package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.bases.BlockBase;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
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
		super("amber_ore", Material.ROCK, MaterialColor.GRAY);
	}
	
	public Item getItemDropped(BlockState state, Random rand, int fortune){
		return ArcanaItems.AMBER;
	}
	
	public int quantityDropped(BlockState state, int fortune, Random random){
		if(fortune > 0)
			return random.nextInt(fortune + 2) + 1;
		else
			return quantityDropped(random);
	}
	
	public int getExpDrop(BlockState state, IBlockAccess world, BlockPos pos, int fortune){
		Random rand = world instanceof World ? ((World)world).rand : new Random();
		return MathHelper.getInt(rand, 2, 5);
	}
}