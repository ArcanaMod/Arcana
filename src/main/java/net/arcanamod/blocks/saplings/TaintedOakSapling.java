package net.arcanamod.blocks.saplings;

import net.arcanamod.worldgen.trees.TaintedLargeOakGenerator;
import net.arcanamod.worldgen.trees.TaintedOakGenerator;
import net.arcanamod.blocks.bases.SaplingBase;
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
 * Used to grow all variations of the tainted oak tree
 */
public class TaintedOakSapling extends SaplingBase{
	boolean untainted;
	
	public TaintedOakSapling(String name, boolean untainted){
		super(name);
		this.untainted = untainted;
	}
	
	@Override
	public void generateTree(World worldIn, BlockPos pos, BlockState state, Random rand){
		Random random = new Random();
		if(!TerrainGen.saplingGrowTree(worldIn, rand, pos))
			return;
		
		boolean bigTree = random.nextInt(10) == 0;
		Feature worldgenerator;
		if(untainted){
			worldgenerator = bigTree ? new TaintedLargeOakGenerator(true, true) : new TaintedOakGenerator(true, true);
		}else{
			worldgenerator = bigTree ? new TaintedLargeOakGenerator(true, false) : new TaintedOakGenerator(true, false);
		}
		
		worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
		
		if(!worldgenerator.generate(worldIn, rand, pos)){
			worldIn.setBlockState(pos, state, 4);
		}
	}
}
