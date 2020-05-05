package net.arcanamod.worldgen.trees;

import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.AbstractTreeFeature;

import java.util.Random;

/**
 * @author Mozaran
 * <p>
 * Used to generate small birch tainted trees
 */
public class TaintedBirchGenerator extends AbstractTreeFeature{
	private final boolean useExtraRandomHeight;
	private static final BlockState DEFAULT_TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);
	private static final BlockState DEFAULT_TAINTED_TRUNK = ArcanaBlocks.TAINTED_BIRCH_LOG.getDefaultState();
	private static final BlockState DEFAULT_UNTAINTED_TRUNK = ArcanaBlocks.UNTAINTED_BIRCH_LOG.getDefaultState();
	private static final BlockState DEFAULT_LEAVES = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.FALSE);
	private static final BlockState DEFAULT_TAINTED_LEAVES = ArcanaBlocks.TAINTED_BIRCH_LEAVES.getDefaultState().withProperty(LeavesBlock.CHECK_DECAY, Boolean.FALSE);
	private static final BlockState DEFAULT_UNTAINTED_LEAVES = ArcanaBlocks.UNTAINTED_BIRCH_LEAVES.getDefaultState().withProperty(LeavesBlock.CHECK_DECAY, Boolean.FALSE);
	
	private final BlockState metaWood;
	private final BlockState metaLeaves;
	
	public TaintedBirchGenerator(boolean notify, boolean useExtraRandomHeightIn, boolean tainted){
		this(notify, useExtraRandomHeightIn, tainted, false);
	}
	
	public TaintedBirchGenerator(boolean notify, boolean useExtraRandomHeightIn, boolean tainted, boolean untainted){
		super(notify);
		this.useExtraRandomHeight = useExtraRandomHeightIn;
		if(tainted){
			metaWood = untainted ? DEFAULT_UNTAINTED_TRUNK : DEFAULT_TAINTED_TRUNK;
			metaLeaves = untainted ? DEFAULT_UNTAINTED_LEAVES : DEFAULT_TAINTED_LEAVES;
		}else{
			metaWood = DEFAULT_TRUNK;
			metaLeaves = DEFAULT_LEAVES;
		}
	}
	
	public boolean generate(World worldIn, Random rand, BlockPos position){
		int i = rand.nextInt(3) + 5;
		
		if(this.useExtraRandomHeight){
			i += rand.nextInt(7);
		}
		
		boolean flag = true;
		
		if(position.getY() >= 1 && position.getY() + i + 1 <= 256){
			for(int j = position.getY(); j <= position.getY() + 1 + i; ++j){
				int k = 1;
				
				if(j == position.getY()){
					k = 0;
				}
				
				if(j >= position.getY() + 1 + i - 2){
					k = 2;
				}
				
				BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
				
				for(int l = position.getX() - k; l <= position.getX() + k && flag; ++l){
					for(int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1){
						if(j >= 0 && j < worldIn.getHeight()){
							if(!this.isReplaceable(worldIn, blockpos$mutableblockpos.setPos(l, j, i1))){
								flag = false;
							}
						}else{
							flag = false;
						}
					}
				}
			}
			
			if(!flag){
				return false;
			}else{
				BlockPos down = position.down();
				BlockState state = worldIn.getBlockState(down);
				boolean isSoil = state.getBlock().canSustainPlant(state, worldIn, down, Direction.UP, (SaplingBlock)Blocks.SAPLING);
				
				if(isSoil && position.getY() < worldIn.getHeight() - i - 1){
					state.getBlock().onPlantGrow(state, worldIn, down, position);
					
					for(int i2 = position.getY() - 3 + i; i2 <= position.getY() + i; ++i2){
						int k2 = i2 - (position.getY() + i);
						int l2 = 1 - k2 / 2;
						
						for(int i3 = position.getX() - l2; i3 <= position.getX() + l2; ++i3){
							int j1 = i3 - position.getX();
							
							for(int k1 = position.getZ() - l2; k1 <= position.getZ() + l2; ++k1){
								int l1 = k1 - position.getZ();
								
								if(Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0){
									BlockPos blockpos = new BlockPos(i3, i2, k1);
									BlockState state2 = worldIn.getBlockState(blockpos);
									
									if(state2.getBlock().isAir(state2, worldIn, blockpos) || state2.getBlock().isAir(state2, worldIn, blockpos)){
										this.setBlockAndNotifyAdequately(worldIn, blockpos, metaLeaves);
									}
								}
							}
						}
					}
					
					for(int j2 = 0; j2 < i; ++j2){
						BlockPos upN = position.up(j2);
						BlockState state2 = worldIn.getBlockState(upN);
						
						if(state2.getBlock().isAir(state2, worldIn, upN) || state2.getBlock().isLeaves(state2, worldIn, upN)){
							this.setBlockAndNotifyAdequately(worldIn, position.up(j2), metaWood);
						}
					}
					
					return true;
				}else{
					return false;
				}
			}
		}else{
			return false;
		}
	}
}
