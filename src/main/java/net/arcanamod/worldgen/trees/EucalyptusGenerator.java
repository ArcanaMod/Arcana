package net.arcanamod.worldgen.trees;

import net.arcanamod.worldgen.GenerationUtilities;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.*;
import net.minecraft.block.LogBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.Random;

import static net.minecraft.block.LogBlock.LOG_AXIS;

/**
 * @author Mozaran
 * <p>
 * Used to generate eucalyptus trees
 */
public class EucalyptusGenerator extends AbstractTreeFeature{
	private static final BlockState DEFAULT_TRUNK = ArcanaBlocks.EUCALYPTUS_LOG.getDefaultState();
	private static final BlockState DEFAULT_TAINTED_TRUNK = ArcanaBlocks.TAINTED_EUCALYPTUS_LOG.getDefaultState();
	private static final BlockState DEFAULT_UNTAINTED_TRUNK = ArcanaBlocks.UNTAINTED_EUCALYPTUS_LOG.getDefaultState();
	private static final BlockState DEFAULT_LEAVES = ArcanaBlocks.EUCALYPTUS_LEAVES.getDefaultState().withProperty(LeavesBlock.CHECK_DECAY, Boolean.FALSE);
	private static final BlockState DEFAULT_TAINTED_LEAVES = ArcanaBlocks.TAINTED_EUCALYPTUS_LEAVES.getDefaultState().withProperty(LeavesBlock.CHECK_DECAY, Boolean.FALSE);
	private static final BlockState DEFAULT_UNTAINTED_LEAVES = ArcanaBlocks.UNTAINTED_EUCALYPTUS_LEAVES.getDefaultState().withProperty(LeavesBlock.CHECK_DECAY, Boolean.FALSE);
	
	private final BlockState metaWood;
	private final BlockState metaLeaves;
	
	private final int minTreeHeight = 7;
	
	public EucalyptusGenerator(boolean notify, boolean tainted){
		this(notify, tainted, false);
	}
	
	public EucalyptusGenerator(boolean notify, boolean tainted, boolean untainted){
		super(notify);
		if(tainted){
			metaWood = untainted ? DEFAULT_UNTAINTED_TRUNK : DEFAULT_TAINTED_TRUNK;
			metaLeaves = untainted ? DEFAULT_UNTAINTED_LEAVES : DEFAULT_TAINTED_LEAVES;
		}else{
			metaWood = DEFAULT_TRUNK;
			metaLeaves = DEFAULT_LEAVES;
		}
	}
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position){
		int seed = rand.nextInt(8);
		int height = rand.nextInt(2) + minTreeHeight;
		
		ArrayList<BlockPos> trunkBlockList = new ArrayList<>();
		ArrayList<BlockPos> leafBlockList = new ArrayList<>();
		ArrayList<BlockPos> xLogList = new ArrayList<>();
		ArrayList<BlockPos> zLogList = new ArrayList<>();
		for(int y = 0; y <= height; ++y){
			trunkBlockList.add(position.add(0, y, 0));
		}
		
		int branchWidth = 4;
		int xBranchBaseY = -1;
		int zBranchBaseY = -1;
		int xzBranchBaseY = -1;
		BlockPos xBranch = null;
		BlockPos zBranch = null;
		BlockPos xzBranch = null;
		if(seed < 4){
			// Branch +x +z (-x)(-z) == 0, +x -z == 1, -x +z == 2, -x -z == 3
			// X branch
			xBranchBaseY = 3 + rand.nextInt(2);
			if(seed % 4 == 0 || seed % 4 == 1){
				for(int i = 1; i <= 3; ++i){
					xLogList.add(position.add(i, xBranchBaseY, 0));
					trunkBlockList.add(position.add(branchWidth, xBranchBaseY + i, 0));
				}
				trunkBlockList.add(position.add(3, xBranchBaseY + 1, 0));
				xBranch = position.add(branchWidth, xBranchBaseY + 3, 0);
			}else{
				for(int i = 1; i <= 3; ++i){
					xLogList.add(position.add(-i, xBranchBaseY, 0));
					trunkBlockList.add(position.add(-branchWidth, xBranchBaseY + i, 0));
				}
				trunkBlockList.add(position.add(-3, xBranchBaseY + 1, 0));
				xBranch = position.add(-branchWidth, xBranchBaseY + 3, 0);
			}
			// Z branch
			zBranchBaseY = 3 + rand.nextInt(2);
			if(seed % 4 == 0 || seed % 4 == 2){
				for(int i = 1; i <= 3; ++i){
					zLogList.add(position.add(0, zBranchBaseY, i));
					trunkBlockList.add(position.add(0, zBranchBaseY + i, branchWidth));
				}
				trunkBlockList.add(position.add(0, zBranchBaseY + 1, 3));
				zBranch = position.add(0, zBranchBaseY + 3, branchWidth);
			}else{
				for(int i = 1; i <= 3; ++i){
					zLogList.add(position.add(0, zBranchBaseY, -i));
					trunkBlockList.add(position.add(0, zBranchBaseY + i, -branchWidth));
				}
				trunkBlockList.add(position.add(0, zBranchBaseY + 1, -3));
				zBranch = position.add(0, zBranchBaseY + 3, -branchWidth);
			}
			// XZ Branch
			xzBranchBaseY = 3;
			int xModifier = (seed % 4 == 0 || seed % 4 == 1) ? 1 : -1;
			int zModifier = (seed % 4 == 0 || seed % 4 == 2) ? 1 : -1;
			for(int i = 1; i <= 2; ++i){
				xLogList.add(position.add((-i * xModifier) + xModifier, xzBranchBaseY, -i * zModifier));
				xLogList.add(position.add(-i * xModifier, xzBranchBaseY, -i * zModifier));
				trunkBlockList.add(position.add(-2 * xModifier, xzBranchBaseY + i, -2 * zModifier));
			}
			xzBranch = position.add(-2 * xModifier, xzBranchBaseY + 2, -2 * zModifier);
		}else{
			int adjSeed = rand.nextInt(8);
			if(adjSeed < 4){
				// X branch
				xBranchBaseY = 3 + rand.nextInt(2);
				if(adjSeed % 4 == 0 || adjSeed % 4 == 1){
					for(int i = 1; i <= 3; ++i){
						xLogList.add(position.add(i, xBranchBaseY, 0));
						trunkBlockList.add(position.add(branchWidth, xBranchBaseY + i, 0));
					}
					trunkBlockList.add(position.add(3, xBranchBaseY + 1, 0));
					xBranch = position.add(branchWidth, xBranchBaseY + 3, 0);
				}else{
					for(int i = 1; i <= 3; ++i){
						xLogList.add(position.add(-i, xBranchBaseY, 0));
						trunkBlockList.add(position.add(-branchWidth, xBranchBaseY + i, 0));
					}
					trunkBlockList.add(position.add(-3, xBranchBaseY + 1, 0));
					xBranch = position.add(-branchWidth, xBranchBaseY + 3, 0);
				}
			}else if(adjSeed < 8){
				// Z branch
				zBranchBaseY = 3 + rand.nextInt(2);
				if(adjSeed % 4 == 0 || adjSeed % 4 == 2){
					for(int i = 1; i <= 3; ++i){
						zLogList.add(position.add(0, zBranchBaseY, i));
						trunkBlockList.add(position.add(0, zBranchBaseY + i, branchWidth));
					}
					trunkBlockList.add(position.add(0, zBranchBaseY + 1, 3));
					zBranch = position.add(0, zBranchBaseY + 3, branchWidth);
				}else{
					for(int i = 1; i <= 3; ++i){
						zLogList.add(position.add(0, zBranchBaseY, -i));
						trunkBlockList.add(position.add(0, zBranchBaseY + i, -branchWidth));
					}
					trunkBlockList.add(position.add(0, zBranchBaseY + 1, -3));
					zBranch = position.add(0, zBranchBaseY + 3, -branchWidth);
				}
			}
			// XZ Branch
			xzBranchBaseY = 3;
			int xModifier = (adjSeed % 4 == 0 || adjSeed % 4 == 1) ? 1 : -1;
			int zModifier = (adjSeed % 4 == 0 || adjSeed % 4 == 2) ? 1 : -1;
			for(int i = 1; i <= 2; ++i){
				xLogList.add(position.add((-i * xModifier) + xModifier, xzBranchBaseY, -i * zModifier));
				xLogList.add(position.add(-i * xModifier, xzBranchBaseY, -i * zModifier));
				trunkBlockList.add(position.add(-2 * xModifier, xzBranchBaseY + i, -2 * zModifier));
			}
			xzBranch = position.add(-2 * xModifier, xzBranchBaseY + 2, -2 * zModifier);
		}
		
		// Generate Leaves
		leafBlockList.addAll(GenerationUtilities.GenerateCircle(position.add(0, height - 1, 0), 1, GenerationUtilities.GenType.FULL));
		leafBlockList.addAll(GenerationUtilities.GenerateCircle(position.add(0, height, 0), 3, GenerationUtilities.GenType.FULL));
		leafBlockList.addAll(GenerationUtilities.GenerateCircle(position.add(0, height + 1, 0), 1, GenerationUtilities.GenType.FULL));
		
		if(xBranch != null){
			leafBlockList.addAll(GenerationUtilities.GenerateCircle(xBranch, 1, GenerationUtilities.GenType.FULL));
			leafBlockList.add(xBranch.add(0, 1, 0));
		}
		if(zBranch != null){
			leafBlockList.addAll(GenerationUtilities.GenerateCircle(zBranch, 1, GenerationUtilities.GenType.FULL));
			leafBlockList.add(zBranch.add(0, 1, 0));
		}
		// xz Branch
		leafBlockList.addAll(GenerationUtilities.GenerateCircle(xzBranch, 1, GenerationUtilities.GenType.FULL));
		leafBlockList.add(xzBranch.add(0, 1, 0));
		
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
			for(BlockPos pos : xLogList){
				if(!this.isReplaceable(worldIn, pos)){
					return false;
				}
			}
			for(BlockPos pos : zLogList){
				if(!this.isReplaceable(worldIn, pos)){
					return false;
				}
			}
		}else{
			return false;
		}
		
		BlockState state = worldIn.getBlockState(position.down());
		
		if(state.getBlock().canSustainPlant(state, worldIn, position.down(), Direction.UP, (IPlantable)Blocks.SAPLING) && position.getY() < worldIn.getHeight() - height - 1){
			state.getBlock().onPlantGrow(state, worldIn, position.down(), position);
			for(BlockPos pos : leafBlockList){
				setBlockAndNotifyAdequately(worldIn, pos, metaLeaves);
			}
			for(BlockPos pos : trunkBlockList){
				setBlockAndNotifyAdequately(worldIn, pos, metaWood);
			}
			for(BlockPos pos : xLogList){
				setBlockAndNotifyAdequately(worldIn, pos, metaWood.withProperty(LOG_AXIS, LogBlock.EnumAxis.X));
			}
			for(BlockPos pos : zLogList){
				setBlockAndNotifyAdequately(worldIn, pos, metaWood.withProperty(LOG_AXIS, LogBlock.EnumAxis.Z));
			}
			
			return true;
		}else{
			return false;
		}
		
	}
}
