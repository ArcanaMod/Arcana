package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.ArcanaGuiHandler;
import net.arcanamod.blocks.bases.BlockBase;
import net.arcanamod.blocks.tiles.ResearchTableTileEntity;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.arcanamod.blocks.BlockResearchTable.EnumSide.LEFT;
import static net.arcanamod.blocks.BlockResearchTable.EnumSide.RIGHT;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
// TODO: This has a problem with being broken. I don't think this can be solved without a TESR.
// Thankfully, I'll probably switch this over to a TESR anyways to show ink, wands, and research notes. yay.
public class BlockResearchTable extends BlockBase implements ITileEntityProvider{
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyEnum<EnumSide> PART = PropertyEnum.create("part", EnumSide.class);
	
	public BlockResearchTable(){
		super("research_table", Material.WOOD);
		translucent = true;
		setHardness(2.5f);
	}
	
	public boolean hasTileEntity(IBlockState state){
		return state.getValue(PART) == LEFT;
	}
	
	@Nullable
	@ParametersAreNonnullByDefault
	public TileEntity createNewTileEntity(World worldIn, int meta){
		return (meta & 8) > 0 ? null : new ResearchTableTileEntity();
	}
	
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(world.isRemote)
			return true;
		if(state.getValue(PART) == RIGHT)
			pos = pos.offset(state.getValue(FACING).rotateYCCW());
		TileEntity te = getTE(pos, world);
		if(te instanceof ResearchTableTileEntity)
			player.openGui(Arcana.instance, ArcanaGuiHandler.RESEARCH_TABLE_ID, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	@Nullable
	private static TileEntity getTE(BlockPos pos, World world){
		if(world.getBlockState(pos).getValue(PART).equals(LEFT))
			return world.getTileEntity(pos);
		else
			return world.getTileEntity(pos.offset(world.getBlockState(pos).getValue(FACING).rotateYCCW()));
	}
	
	public void breakBlock(World world, BlockPos pos, IBlockState state){
		TileEntity te;
		if(state.getValue(PART).equals(LEFT))
			te = world.getTileEntity(pos);
		else
			te = world.getTileEntity(pos.offset(state.getValue(FACING).rotateYCCW()));
		
		if(te instanceof ResearchTableTileEntity){
			ResearchTableTileEntity rt = (ResearchTableTileEntity)te;
			
			ItemStack itemstack = new ItemStack(ArcanaItems.RESEARCH_TABLE_PLACER);
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setTag("BlockEntityTag", rt.saveToNBT());
			itemstack.setTagCompound(nbttagcompound);
			
			if(state.getValue(PART).equals(LEFT)){
				if(rt.shouldDrop())
					spawnAsEntity(world, pos, itemstack);
				IItemHandler items = rt.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				if(items != null)
					for(int i = 0; i < items.getSlots(); i++)
						if(!items.getStackInSlot(i).isEmpty())
							spawnAsEntity(world, pos, items.getStackInSlot(i));
			}
		}
		super.breakBlock(world, pos, state);
	}
	
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param){
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(id, param);
	}
	
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player){
		return new ItemStack(ArcanaItems.RESEARCH_TABLE_PLACER);
	}
	
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack){
		super.harvestBlock(world, player, pos, state, null, stack);
	}
	
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		TileEntity te;
		if(state.getValue(PART).equals(LEFT))
			te = world.getTileEntity(pos);
		else
			te = world.getTileEntity(pos.offset(state.getValue(FACING).rotateYCCW()));
		
		if(te instanceof ResearchTableTileEntity){
			ResearchTableTileEntity researchTable = (ResearchTableTileEntity)te;
			researchTable.setShouldDrop(!player.capabilities.isCreativeMode);
			researchTable.markDirty();
		}
		
		if(state.getValue(PART) == RIGHT){
			BlockPos blockpos = pos.offset(state.getValue(FACING).rotateYCCW());
			if(world.getBlockState(blockpos).getBlock() == this)
				world.setBlockToAir(blockpos);
		}else{
			BlockPos blockpos = pos.offset(state.getValue(FACING).rotateY());
			if(world.getBlockState(blockpos).getBlock() == this)
				world.setBlockToAir(blockpos);
		}
	}
	
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
		// Handled by breakBlock while preserving aspects
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
	
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face){
		return face.equals(EnumFacing.UP) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
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
	
	public enum EnumSide implements IStringSerializable{
		LEFT("left"),
		RIGHT("right");
		
		private final String name;
		
		EnumSide(String name){
			this.name = name;
		}
		
		public String toString(){
			return this.name;
		}
		
		public String getName(){
			return this.name;
		}
	}
}