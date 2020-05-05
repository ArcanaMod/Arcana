package net.arcanamod.blocks.bases;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.OreDictEntry;
import net.arcanamod.util.IHasModel;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * Sapling Base, all basic saplings should extend this
 *
 * @author Mozaran
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class SaplingBase extends BushBlock implements IGrowable, IHasModel, OreDictEntry{
	
	public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
	protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
	
	public SaplingBase(String name){
		setUnlocalizedName(name);
		setRegistryName(name);
		setHardness(0.0F);
		setSoundType(SoundType.PLANT);
		
		ArcanaBlocks.BLOCKS.add(this);
		ArcanaItems.ITEMS.add(new BlockItem(this).setRegistryName(this.getRegistryName()));
	}
	
	public String getOreDictName(){
		return "treeSapling";
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos){
		return SAPLING_AABB;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand){
		if(!worldIn.isRemote){
			super.updateTick(worldIn, pos, state, rand);
			
			if(worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0){
				grow(worldIn, rand, pos, state);
			}
		}
	}
	
	public abstract void generateTree(World worldIn, BlockPos pos, BlockState state, Random rand);
	
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, BlockState state, boolean isClient){
		return true;
	}
	
	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state){
		return worldIn.rand.nextFloat() < 0.45D;
	}
	
	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, BlockState state){
		if(state.getValue(STAGE) == 0){
			worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
		}else{
			generateTree(worldIn, pos, state, rand);
		}
	}
	
	@Override
	public BlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(STAGE, (meta & 8) >> 3);
	}
	
	@Override
	public int getMetaFromState(BlockState state){
		int i = 0;
		i = i | state.getValue(STAGE) << 3;
		return i;
	}
	
	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, STAGE);
	}
	
	@Override
	public void registerModels(){
		Arcana.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
