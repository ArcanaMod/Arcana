package net.arcanamod.worldgen.trees;

import net.arcanamod.worldgen.GenerationUtilities;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.Random;

public class SilverwoodGenerator extends WorldGenAbstractTree{
	private final IBlockState metaWood = ArcanaBlocks.SILVERWOOD_LOG.getDefaultState();
	private final IBlockState metaLeaves = ArcanaBlocks.SILVERWOOD_LEAVES.getDefaultState();
	
	private final int minTreeHeight = 9;
	
	public SilverwoodGenerator(boolean notify){
		super(notify);
	}
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position){
		int height = rand.nextInt(2) + minTreeHeight;
		
		ArrayList<BlockPos> trunkBlockList = new ArrayList<>();
		int heightCenter = height == 9 ? 1 : 2;
		trunkBlockList.add(position.add(2, 0, 0));
		trunkBlockList.add(position.add(-2, 0, 0));
		trunkBlockList.add(position.add(0, 0, 2));
		trunkBlockList.add(position.add(0, 0, -2));
		trunkBlockList.addAll(GenerationUtilities.GenerateTrunk(position, position.add(0, height - 1, 0), 1));
		trunkBlockList.addAll(GenerationUtilities.GenerateTrunk(position, position.add(0, heightCenter, 0), 3));
		trunkBlockList.add(position.add(1, heightCenter + 1, 0));
		trunkBlockList.add(position.add(-1, heightCenter + 1, 0));
		trunkBlockList.add(position.add(0, heightCenter + 1, 1));
		trunkBlockList.add(position.add(0, heightCenter + 1, -1));
		
		if(rand.nextInt(2) == 0){
			// x larger
			trunkBlockList.add(position.add(1, heightCenter + 2, 0));
			trunkBlockList.add(position.add(-1, heightCenter + 2, 0));
		}else{
			// z larger
			trunkBlockList.add(position.add(0, heightCenter + 2, 1));
			trunkBlockList.add(position.add(0, heightCenter + 2, -1));
		}
		
		ArrayList<BlockPos> leafBlockList = new ArrayList<>();
		int leafStart = heightCenter + 4;
		leafBlockList.addAll(GenerationUtilities.GenerateCircle(position.add(0, leafStart, 0), 1, GenerationUtilities.GenType.FULL));
		leafBlockList.addAll(GenerationUtilities.GenerateCircle(position.add(0, leafStart + 1, 0), 3, GenerationUtilities.GenType.FULL));
		int numMidRings = height == 9 ? 2 : 3;
		int midRingStart = leafStart + 2;
		for(int y = midRingStart; y < midRingStart + numMidRings; ++y){
			leafBlockList.addAll(genMidRing(position.add(0, y, 0)));
		}
		int topStart = midRingStart + numMidRings;
		leafBlockList.addAll(GenerationUtilities.GenerateCircle(position.add(0, topStart, 0), 3, GenerationUtilities.GenType.FULL));
		leafBlockList.addAll(GenerationUtilities.GenerateCircle(position.add(0, topStart + 1, 0), 1, GenerationUtilities.GenType.FULL));
		
		// Check if tree fits in world
		if(position.getY() >= 1 && position.getY() + height + 1 <= worldIn.getHeight()){
			for(BlockPos pos : leafBlockList){
				if(!this.isReplaceable(worldIn, pos)){
					return false;
				}
			}
			for(BlockPos pos : trunkBlockList){
				if(!this.isReplaceable(worldIn, pos)){
					return false;
				}
			}
		}else{
			return false;
		}
		
		IBlockState state = worldIn.getBlockState(position.down());
		if(state.getBlock().canSustainPlant(state, worldIn, position.down(), EnumFacing.UP, (IPlantable)Blocks.SAPLING) && position.getY() < worldIn.getHeight() - height - 1){
			state.getBlock().onPlantGrow(state, worldIn, position.down(), position);
			for(BlockPos pos : leafBlockList){
				setBlockAndNotifyAdequately(worldIn, pos, metaLeaves);
			}
			for(BlockPos pos : trunkBlockList){
				setBlockAndNotifyAdequately(worldIn, pos, metaWood);
			}
			
			return true;
		}else{
			return false;
		}
		
	}
	
	public static ArrayList<BlockPos> genMidRing(BlockPos origin){
		ArrayList<BlockPos> blockList = new ArrayList<>();
		for(int x = -2; x <= 2; ++x){
			for(int z = -1; z <= 1; ++z){
				blockList.add(origin.add(x, 0, z));
			}
		}
		for(int x = -1; x <= 1; ++x){
			blockList.add(origin.add(x, 0, 2));
			blockList.add(origin.add(x, 0, -2));
		}
		return blockList;
	}
}
