package net.arcanamod.worldgen.trees;

import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.*;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.AbstractTreeFeature;

import java.util.Random;

/**
 * @author Mozaran
 * <p>
 * Used to generate small jungle tainted trees
 */
public class TaintedJungleGenerator extends AbstractTreeFeature{
	private static final BlockState DEFAULT_TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
	private static final BlockState DEFAULT_TAINTED_TRUNK = ArcanaBlocks.TAINTED_JUNGLE_LOG.getDefaultState();
	private static final BlockState DEFAULT_UNTAINTED_TRUNK = ArcanaBlocks.UNTAINTED_JUNGLE_LOG.getDefaultState();
	private static final BlockState DEFAULT_LEAVES = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(LeavesBlock.CHECK_DECAY, Boolean.FALSE);
	private static final BlockState DEFAULT_TAINTED_LEAVES = ArcanaBlocks.TAINTED_JUNGLE_LEAVES.getDefaultState().withProperty(LeavesBlock.CHECK_DECAY, Boolean.FALSE);
	private static final BlockState DEFAULT_UNTAINTED_LEAVES = ArcanaBlocks.UNTAINTED_JUNGLE_LEAVES.getDefaultState().withProperty(LeavesBlock.CHECK_DECAY, Boolean.FALSE);
	
	private final BlockState metaWood;
	private final BlockState metaLeaves;
	
	public TaintedJungleGenerator(boolean notify, boolean tainted){
		this(notify, tainted, false);
	}
	
	public TaintedJungleGenerator(boolean notify, boolean tainted, boolean untainted){
		super(notify);
		if(tainted){
			metaWood = untainted ? DEFAULT_UNTAINTED_TRUNK : DEFAULT_TAINTED_TRUNK;
			metaLeaves = untainted ? DEFAULT_UNTAINTED_LEAVES : DEFAULT_TAINTED_LEAVES;
		}else{
			metaWood = DEFAULT_TRUNK;
			metaLeaves = DEFAULT_LEAVES;
		}
	}
	
	public boolean generate(World worldIn, Random rand, BlockPos position){
		int i = rand.nextInt(3) + 4 + rand.nextInt(7);
		boolean flag = true;
		
		if(position.getY() >= 1 && position.getY() + i + 1 <= worldIn.getHeight()){
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
				BlockState state = worldIn.getBlockState(position.down());
				
				if(state.getBlock().canSustainPlant(state, worldIn, position.down(), Direction.UP, (SaplingBlock)Blocks.SAPLING) && position.getY() < worldIn.getHeight() - i - 1){
					state.getBlock().onPlantGrow(state, worldIn, position.down(), position);
					int k2 = 3;
					int l2 = 0;
					
					for(int i3 = position.getY() - 3 + i; i3 <= position.getY() + i; ++i3){
						int i4 = i3 - (position.getY() + i);
						int j1 = 1 - i4 / 2;
						
						for(int k1 = position.getX() - j1; k1 <= position.getX() + j1; ++k1){
							int l1 = k1 - position.getX();
							
							for(int i2 = position.getZ() - j1; i2 <= position.getZ() + j1; ++i2){
								int j2 = i2 - position.getZ();
								
								if(Math.abs(l1) != j1 || Math.abs(j2) != j1 || rand.nextInt(2) != 0 && i4 != 0){
									BlockPos blockpos = new BlockPos(k1, i3, i2);
									state = worldIn.getBlockState(blockpos);
									
									if(state.getBlock().isAir(state, worldIn, blockpos) || state.getBlock().isLeaves(state, worldIn, blockpos) || state.getMaterial() == Material.VINE){
										this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
									}
								}
							}
						}
					}
					
					for(int j3 = 0; j3 < i; ++j3){
						BlockPos upN = position.up(j3);
						state = worldIn.getBlockState(upN);
						
						if(state.getBlock().isAir(state, worldIn, upN) || state.getBlock().isLeaves(state, worldIn, upN) || state.getMaterial() == Material.VINE){
							this.setBlockAndNotifyAdequately(worldIn, position.up(j3), this.metaWood);
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
