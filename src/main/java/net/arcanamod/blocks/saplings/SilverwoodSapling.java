package net.arcanamod.blocks.saplings;

import net.arcanamod.blocks.bases.SaplingBase;
import net.arcanamod.worldgen.trees.SilverwoodGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public class SilverwoodSapling extends SaplingBase{
	public SilverwoodSapling(String name){
		super(name);
	}
	
	@Override
	public void generateTree(World worldIn, BlockPos pos, BlockState state, Random rand){
		if(!TerrainGen.saplingGrowTree(worldIn, rand, pos))
			return;
		
		Feature worldgenerator = new SilverwoodGenerator(true);
		
		worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
		
		if(!worldgenerator.generate(worldIn, rand, pos)){
			worldIn.setBlockState(pos, state, 4);
		}
	}
	
}
