package net.arcanamod.blocks.saplings;

import net.arcanamod.worldgen.trees.TaintedDarkOakGenerator;
import net.arcanamod.worldgen.trees.TaintedSpruceGenerator;
import net.arcanamod.blocks.bases.SaplingBase;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

/**
 * @author Mozaran
 * <p>
 * Used to grow all variations of the tainted dark oak tree
 */
public class TaintedDarkOakSapling extends SaplingBase{
	boolean untainted;
	
	public TaintedDarkOakSapling(String name, boolean untainted){
		super(name);
		this.untainted = untainted;
	}
	
	@Override
	public void generateTree(World worldIn, BlockPos pos, BlockState state, Random rand){
		if(!TerrainGen.saplingGrowTree(worldIn, rand, pos))
			return;
		
		int x = 0;
		int z = 0;
		boolean flag = false;
		
		Feature worldgenerator = new TaintedSpruceGenerator(true, false, false);
		
		BlockState iblockstate2 = Blocks.AIR.getDefaultState();
		
		for(x = 0; x >= -1; --x){
			for(z = 0; z >= -1; --z){
				if(this.isTwoByTwoOfType(worldIn, pos, x, z, BlockPlanks.EnumType.DARK_OAK)){
					worldgenerator = untainted ? new TaintedDarkOakGenerator(true, true, true) : new TaintedDarkOakGenerator(true, true, false);
					flag = true;
					break;
				}
			}
			if(flag)
				break;
		}
		
		if(!flag){
			return;
		}
		
		worldIn.setBlockState(pos.add(x, 0, z), iblockstate2, 4);
		worldIn.setBlockState(pos.add(x + 1, 0, z), iblockstate2, 4);
		worldIn.setBlockState(pos.add(x, 0, z + 1), iblockstate2, 4);
		worldIn.setBlockState(pos.add(x + 1, 0, z + 1), iblockstate2, 4);
		
		if(!worldgenerator.generate(worldIn, rand, pos.add(x, 0, z))){
			worldIn.setBlockState(pos.add(x, 0, z), state, 4);
			worldIn.setBlockState(pos.add(x + 1, 0, z), state, 4);
			worldIn.setBlockState(pos.add(x, 0, z + 1), state, 4);
			worldIn.setBlockState(pos.add(x + 1, 0, z + 1), state, 4);
		}
	}
	
	private boolean isTwoByTwoOfType(World worldIn, BlockPos pos, int x, int z, BlockPlanks.EnumType type){
		BlockPos pos1 = pos.add(x, 0, z);
		boolean valid1 = this.isTypeAt(worldIn, pos1, type);
		BlockPos pos2 = pos.add(x + 1, 0, z);
		boolean valid2 = this.isTypeAt(worldIn, pos2, type);
		BlockPos pos3 = pos.add(x, 0, z + 1);
		boolean valid3 = this.isTypeAt(worldIn, pos3, type);
		BlockPos pos4 = pos.add(x + 1, 0, z + 1);
		boolean valid4 = this.isTypeAt(worldIn, pos4, type);
		return valid1 && valid2 && valid3 && valid4;
	}
	
	public boolean isTypeAt(World worldIn, BlockPos pos, BlockPlanks.EnumType type){
		BlockState iblockstate = worldIn.getBlockState(pos);
		return iblockstate.getBlock() == this;
	}
}
