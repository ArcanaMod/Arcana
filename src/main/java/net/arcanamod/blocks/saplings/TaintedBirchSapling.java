package net.arcanamod.blocks.saplings;

import net.arcanamod.worldgen.trees.TaintedBirchGenerator;
import net.arcanamod.blocks.bases.SaplingBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

/**
 * @author Mozaran
 * <p>
 * Used to grow all variations of the tainted birch tree
 */
public class TaintedBirchSapling extends SaplingBase{
	boolean untainted;
	boolean useExtraRandomHeight;
	
	public TaintedBirchSapling(String name, boolean useExtraRandomHeight, boolean untainted){
		super(name);
		this.untainted = untainted;
		this.useExtraRandomHeight = useExtraRandomHeight;
	}
	
	@Override
	public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand){
		if(!TerrainGen.saplingGrowTree(worldIn, rand, pos))
			return;
		
		WorldGenerator worldgenerator = untainted ? new TaintedBirchGenerator(true, useExtraRandomHeight, true, true) : new TaintedBirchGenerator(true, useExtraRandomHeight, true, false);
		
		worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
		
		if(!worldgenerator.generate(worldIn, rand, pos)){
			worldIn.setBlockState(pos, state, 4);
		}
	}
}
