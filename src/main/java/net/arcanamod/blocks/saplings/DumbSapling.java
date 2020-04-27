package net.arcanamod.blocks.saplings;

import net.arcanamod.blocks.bases.SaplingBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author Mozaran
 * <p>
 * Saplings that are a WIP
 */
public class DumbSapling extends SaplingBase{
	public DumbSapling(String name){
		super(name);
	}
	
	@Override
	public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand){
	
	}
}
