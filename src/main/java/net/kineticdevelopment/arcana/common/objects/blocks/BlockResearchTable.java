package net.kineticdevelopment.arcana.common.objects.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.common.objects.blocks.bases.BlockBase;
import net.kineticdevelopment.arcana.common.objects.tile.ResearchTableTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.kineticdevelopment.arcana.common.objects.blocks.BlockResearchTable.EnumSide.LEFT;
import static net.kineticdevelopment.arcana.common.objects.blocks.BlockResearchTable.EnumSide.RIGHT;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockResearchTable extends BlockBase implements ITileEntityProvider{
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyEnum<EnumSide> PART = PropertyEnum.create("part", EnumSide.class);
	
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
	
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player){
		return new ItemStack(ItemInit.RESEARCH_TABLE_PLACER);
	}
	
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack){
		super.harvestBlock(world, player, pos, state, null, stack);
	}
	
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player){
		if(player.capabilities.isCreativeMode && state.getValue(PART) == RIGHT){
			BlockPos blockpos = pos.offset(state.getValue(FACING).rotateYCCW());
			if(worldIn.getBlockState(blockpos).getBlock() == this)
				worldIn.setBlockToAir(blockpos);
		}
	}
	
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
		drops.add(new ItemStack(ItemInit.RESEARCH_TABLE_PLACER));
	}
	
	public EnumBlockRenderType getRenderType(IBlockState state){
		return state.getValue(PART).equals(LEFT) ? EnumBlockRenderType.MODEL : EnumBlockRenderType.INVISIBLE;
	}
	
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos){
		EnumFacing facing = state.getValue(FACING);
		if(state.getValue(PART).equals(LEFT)){
			if(world.getBlockState(pos.offset(facing.rotateY())).getBlock() != this)
				world.setBlockToAir(pos);
		}else if(world.getBlockState(pos.offset(facing.rotateYCCW())).getBlock() != this)
			world.setBlockToAir(pos);
	}
	
	// pretty much copied from BlockChest
	// just handles facing
	
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, FACING, PART);
	}
	
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn){
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}
	
	public IBlockState withRotation(IBlockState state, Rotation rot){
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}
	
	public int getMetaFromState(IBlockState state){
		return state.getValue(FACING).getIndex() | (state.getValue(PART).equals(RIGHT) ? 8 : 0);
	}
	
	public IBlockState getStateFromMeta(int meta){
		EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
		EnumSide side = (meta & 8) > 0 ? RIGHT : LEFT;
		return getDefaultState().withProperty(FACING, enumfacing).withProperty(PART, side);
	}
	
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand){
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(PART, LEFT);
	}
	
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos){
		return super.canPlaceBlockAt(worldIn, pos);
	}
	
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
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
	
	public enum EnumSide implements IStringSerializable
	{
		LEFT("left"),
		RIGHT("right");
		
		private final String name;
		
		EnumSide(String name)
		{
			this.name = name;
		}
		
		public String toString()
		{
			return this.name;
		}
		
		public String getName()
		{
			return this.name;
		}
	}
}