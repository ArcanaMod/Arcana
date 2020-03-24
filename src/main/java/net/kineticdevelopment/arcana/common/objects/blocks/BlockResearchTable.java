package net.kineticdevelopment.arcana.common.objects.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.common.objects.blocks.bases.BlockBase;
import net.kineticdevelopment.arcana.common.objects.tile.ResearchTableTileEntity;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
public class BlockResearchTable extends BlockBase implements ITileEntityProvider{
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	
	public BlockResearchTable(){
		super("research_table", Material.WOOD);
		translucent = true;
	}
	
	public boolean hasTileEntity(IBlockState state){
		return true;
	}
	
	@Nullable
	@ParametersAreNonnullByDefault
	public TileEntity createNewTileEntity(World worldIn, int meta){
		return new ResearchTableTileEntity();
	}
	
	// pretty much copied from BlockChest
	// just handles facing
	
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, FACING);
	}
	
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn){
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}
	
	public IBlockState withRotation(IBlockState state, Rotation rot){
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}
	
	public int getMetaFromState(IBlockState state){
		return state.getValue(FACING).getIndex();
	}
	
	public IBlockState getStateFromMeta(int meta){
		EnumFacing enumfacing = EnumFacing.getFront(meta);
		if(enumfacing.getAxis() == EnumFacing.Axis.Y)
			enumfacing = EnumFacing.NORTH;
		return getDefaultState().withProperty(FACING, enumfacing);
	}
	
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
	}
	
	// end facing
	
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos){
		return false;
	}
	
	public boolean isFullCube(IBlockState state){
		return false;
	}
	
	public BlockRenderLayer getBlockLayer(){
		return BlockRenderLayer.TRANSLUCENT;
	}
}