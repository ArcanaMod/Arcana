package net.arcanamod.worldgen.trees;

import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.*;
import net.minecraft.block.VineBlock;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
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
 * Used to generate small oak tainted trees
 */
public class TaintedOakGenerator extends AbstractTreeFeature{
	private static final BlockState DEFAULT_TAINTED_TRUNK = ArcanaBlocks.TAINTED_OAK_LOG.getDefaultState();
	private static final BlockState DEFAULT_UNTAINTED_TRUNK = ArcanaBlocks.UNTAINTED_OAK_LOG.getDefaultState();
	private static final BlockState DEFAULT_TAINTED_LEAVES = ArcanaBlocks.TAINTED_OAK_LEAVES.getDefaultState().withProperty(LeavesBlock.CHECK_DECAY, Boolean.FALSE);
	private static final BlockState DEFAULT_UNTAINTED_LEAVES = ArcanaBlocks.UNTAINTED_OAK_LEAVES.getDefaultState().withProperty(LeavesBlock.CHECK_DECAY, Boolean.FALSE);
	
	private final int minTreeHeight;
	private final boolean vinesGrow;
	private final BlockState metaWood;
	private final BlockState metaLeaves;
	
	TaintedOakGenerator(boolean notify){
		this(notify, false);
	}
	
	public TaintedOakGenerator(boolean notify, boolean untainted){
		this(notify, 4, untainted ? DEFAULT_UNTAINTED_TRUNK : DEFAULT_TAINTED_TRUNK, untainted ? DEFAULT_UNTAINTED_LEAVES : DEFAULT_TAINTED_LEAVES, false);
	}
	
	public TaintedOakGenerator(boolean notify, int minTreeHeightIn, BlockState woodMeta, BlockState leavesMeta, boolean growVines){
		super(notify);
		this.minTreeHeight = minTreeHeightIn;
		this.metaWood = woodMeta;
		this.metaLeaves = leavesMeta;
		this.vinesGrow = growVines;
	}
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position){
		int i = rand.nextInt(3) + this.minTreeHeight;
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
							
							if(this.vinesGrow && j3 > 0){
								if(rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(-1, j3, 0))){
									this.addVine(worldIn, position.add(-1, j3, 0), VineBlock.EAST);
								}
								
								if(rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(1, j3, 0))){
									this.addVine(worldIn, position.add(1, j3, 0), VineBlock.WEST);
								}
								
								if(rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(0, j3, -1))){
									this.addVine(worldIn, position.add(0, j3, -1), VineBlock.SOUTH);
								}
								
								if(rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(0, j3, 1))){
									this.addVine(worldIn, position.add(0, j3, 1), VineBlock.NORTH);
								}
							}
						}
					}
					
					if(this.vinesGrow){
						for(int k3 = position.getY() - 3 + i; k3 <= position.getY() + i; ++k3){
							int j4 = k3 - (position.getY() + i);
							int k4 = 2 - j4 / 2;
							BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();
							
							for(int l4 = position.getX() - k4; l4 <= position.getX() + k4; ++l4){
								for(int i5 = position.getZ() - k4; i5 <= position.getZ() + k4; ++i5){
									blockpos$mutableblockpos1.setPos(l4, k3, i5);
									
									state = worldIn.getBlockState(blockpos$mutableblockpos1);
									if(state.getBlock().isLeaves(state, worldIn, blockpos$mutableblockpos1)){
										BlockPos blockpos2 = blockpos$mutableblockpos1.west();
										BlockPos blockpos3 = blockpos$mutableblockpos1.east();
										BlockPos blockpos4 = blockpos$mutableblockpos1.north();
										BlockPos blockpos1 = blockpos$mutableblockpos1.south();
										
										if(rand.nextInt(4) == 0 && worldIn.isAirBlock(blockpos2)){
											this.addHangingVine(worldIn, blockpos2, VineBlock.EAST);
										}
										
										if(rand.nextInt(4) == 0 && worldIn.isAirBlock(blockpos3)){
											this.addHangingVine(worldIn, blockpos3, VineBlock.WEST);
										}
										
										if(rand.nextInt(4) == 0 && worldIn.isAirBlock(blockpos4)){
											this.addHangingVine(worldIn, blockpos4, VineBlock.SOUTH);
										}
										
										if(rand.nextInt(4) == 0 && worldIn.isAirBlock(blockpos1)){
											this.addHangingVine(worldIn, blockpos1, VineBlock.NORTH);
										}
									}
								}
							}
						}
						
						if(rand.nextInt(5) == 0 && i > 5){
							for(int l3 = 0; l3 < 2; ++l3){
								for(Direction enumfacing : Direction.Plane.HORIZONTAL){
									if(rand.nextInt(4 - l3) == 0){
										Direction enumfacing1 = enumfacing.getOpposite();
										this.placeCocoa(worldIn, rand.nextInt(3), position.add(enumfacing1.getFrontOffsetX(), i - 5 + l3, enumfacing1.getFrontOffsetZ()), enumfacing);
									}
								}
							}
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
	
	private void placeCocoa(World worldIn, int p_181652_2_, BlockPos pos, Direction side){
		this.setBlockAndNotifyAdequately(worldIn, pos, Blocks.COCOA.getDefaultState().withProperty(CocoaBlock.AGE, Integer.valueOf(p_181652_2_)).withProperty(CocoaBlock.FACING, side));
	}
	
	private void addVine(World worldIn, BlockPos pos, PropertyBool prop){
		this.setBlockAndNotifyAdequately(worldIn, pos, Blocks.VINE.getDefaultState().withProperty(prop, Boolean.TRUE));
	}
	
	private void addHangingVine(World worldIn, BlockPos pos, PropertyBool prop){
		this.addVine(worldIn, pos, prop);
		int i = 4;
		
		for(BlockPos blockpos = pos.down(); worldIn.isAirBlock(blockpos) && i > 0; --i){
			this.addVine(worldIn, blockpos, prop);
			blockpos = blockpos.down();
		}
	}
}
