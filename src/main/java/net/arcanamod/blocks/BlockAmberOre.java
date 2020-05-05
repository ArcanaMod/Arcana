package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockAmberOre extends GroupedBlock{
	
	public BlockAmberOre(Properties properties, ItemGroup group){
		super(properties, group);
	}
	
	/*public Item getItemDropped(BlockState state, Random rand, int fortune){
		return ArcanaItems.AMBER;
	}
	
	public int quantityDropped(BlockState state, int fortune, Random random){
		if(fortune > 0)
			return random.nextInt(fortune + 2) + 1;
		else
			return quantityDropped(random);
	}
	
	public int getExpDrop(BlockState state, IBlockReader world, BlockPos pos, int fortune){
		Random rand = world instanceof World ? ((World)world).rand : new Random();
		return MathHelper.getInt(rand, 2, 5);
	}*/
	
	// TODO: loot tables!
}