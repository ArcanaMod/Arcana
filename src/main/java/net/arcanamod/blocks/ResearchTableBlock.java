package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.arcanamod.blocks.bases.WaterloggableBlock;
import net.arcanamod.blocks.tiles.ResearchTableTileEntity;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.arcanamod.blocks.ResearchTableBlock.EnumSide.LEFT;
import static net.arcanamod.blocks.ResearchTableBlock.EnumSide.RIGHT;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
// TODO: This has a problem with being broken. I don't think this can be solved without a TESR.
// Thankfully, I'll probably switch this over to a TESR anyways to show ink, wands, and research notes. yay.
public class ResearchTableBlock extends WaterloggableBlock implements GroupedBlock{
	
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final EnumProperty<EnumSide> PART = EnumProperty.create("part", EnumSide.class);
	
	public ResearchTableBlock(Properties properties){
		super(properties);
		setDefaultState(stateContainer.getBaseState().with(WATERLOGGED, Boolean.FALSE).with(FACING, Direction.NORTH).with(PART, LEFT));
	}
	
	public boolean hasTileEntity(BlockState state){
		return state.get(PART) == LEFT;
	}
	
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return state.get(PART) == LEFT ? new ResearchTableTileEntity() : null;
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		super.fillStateContainer(builder);
		builder.add(FACING, PART);
	}
	
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player){
		if(state.get(PART) == RIGHT){
			BlockPos blockpos = pos.offset(state.get(FACING).rotateYCCW());
			if(world.getBlockState(blockpos).getBlock() == this)
				world.destroyBlock(blockpos, false);
		}else{
			BlockPos blockpos = pos.offset(state.get(FACING).rotateY());
			if(world.getBlockState(blockpos).getBlock() == this)
				world.destroyBlock(blockpos, false);
		}
	}
	
	public BlockState mirror(BlockState state, Mirror mirror){
		return state.rotate(mirror.toRotation(state.get(FACING)));
	}
	
	public BlockState rotate(BlockState state, Rotation rot){
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}
	
	public BlockRenderType getRenderType(BlockState state){
		return state.get(PART).equals(LEFT) ? BlockRenderType.MODEL : BlockRenderType.INVISIBLE;
	}
	
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving){
		Direction facing = state.get(FACING);
		if(state.get(PART).equals(LEFT)){
			if(world.getBlockState(pos.offset(facing.rotateY())).getBlock() != this)
				world.destroyBlock(pos, false);
		}else if(world.getBlockState(pos.offset(facing.rotateYCCW())).getBlock() != this)
			world.destroyBlock(pos, false);
	}
	
	public BlockState getStateForPlacement(BlockItemUseContext context){
		return super.getStateForPlacement(context).with(FACING, context.getPlacementHorizontalFacing()).with(PART, LEFT);
	}
	
	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos){
		return false;
	}
	
	public ItemGroup getGroup(){
		return null;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult rayTraceResult) {
		if(world.isRemote)
			return ActionResultType.PASS;
		if(state.get(PART) == RIGHT)
			pos = pos.offset(state.get(FACING).rotateYCCW());
		TileEntity te = getTE(pos, world);
		if(te instanceof ResearchTableTileEntity)
		{
			BlockPos finalPos = te.getPos();
			NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, buf -> buf.writeBlockPos(finalPos));
			return ActionResultType.SUCCESS;
		}
		return super.onBlockActivated(state, world, pos, player, handIn, rayTraceResult);
	}
	
	@Nullable
	private static TileEntity getTE(BlockPos pos, World world){
		if(world.getBlockState(pos).get(PART).equals(LEFT))
			return world.getTileEntity(pos);
		else
			return world.getTileEntity(pos.offset(world.getBlockState(pos).get(FACING).rotateYCCW()));
	}
	
	/*public void breakBlock(World world, BlockPos pos, BlockState state){
		TileEntity te;
		if(state.getValue(PART).equals(LEFT))
			te = world.getTileEntity(pos);
		else
			te = world.getTileEntity(pos.offset(state.getValue(FACING).rotateYCCW()));
		
		if(te instanceof ResearchTableTileEntity){
			ResearchTableTileEntity rt = (ResearchTableTileEntity)te;
			
			ItemStack itemstack = new ItemStack(ArcanaItems.RESEARCH_TABLE_PLACER);
			CompoundNBT nbttagcompound = new CompoundNBT();
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
	
	public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param){
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(id, param);
	}

	public ItemStack getPickBlock(BlockState state, RayTraceResult target, World world, BlockPos pos, PlayerEntity player){
		return new ItemStack(ArcanaItems.RESEARCH_TABLE_PLACER);
	}
	
	public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack){
		super.harvestBlock(world, player, pos, state, null, stack);
	}
	
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, BlockState state, int fortune){
		// Handled by breakBlock while preserving aspects
	}
	
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face){
		return face.equals(Direction.UP) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
	
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos){
		return super.canPlaceBlockAt(worldIn, pos);
	}
	
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack){
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	*/
	
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