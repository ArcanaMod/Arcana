package net.arcanamod.blocks.multiblocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.arcanamod.blocks.bases.WaterloggableBlock;
import net.arcanamod.blocks.tiles.ResearchTableTileEntity;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
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

import static net.arcanamod.blocks.multiblocks.ResearchTableBlock.EnumSide.LEFT;
import static net.arcanamod.blocks.multiblocks.ResearchTableBlock.EnumSide.RIGHT;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
// TODO: This has a visual problem when being mined. I don't think this can be solved without a TER.
// Thankfully, I'll probably switch this over to a TER anyways to show ink, wands, and research notes. yay.
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

	@Override
	public Item asItem() {
		return ArcanaItems.RESEARCH_TABLE_PLACER.get();
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