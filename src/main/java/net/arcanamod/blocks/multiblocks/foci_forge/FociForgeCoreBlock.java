package net.arcanamod.blocks.multiblocks.foci_forge;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.arcanamod.blocks.multiblocks.StaticComponent;
import net.arcanamod.blocks.tiles.FociForgeTileEntity;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.arcanamod.blocks.multiblocks.foci_forge.FociForgeComponentBlock.*;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FociForgeCoreBlock extends Block implements StaticComponent, GroupedBlock {

	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		VoxelShape shape;
		Direction facing = state.get(FACING);
		switch(facing) {
			case EAST:
				shape = SHAPE_E;
				break;
			case SOUTH:
				shape = SHAPE_S;
				break;
			case WEST:
				shape = SHAPE_W;
				break;
			case NORTH:
			default:
				shape = SHAPE_N;
				break;
		}
		return shape;
	}

	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

	public FociForgeCoreBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public ItemGroup getGroup() {
		return null;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new FociForgeTileEntity();
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	public boolean isCore(BlockPos pos, BlockState state) {
		return true;
	}

	public BlockPos getCorePos(BlockPos pos, BlockState state) {
		return pos;
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player){
		Direction facing = state.get(FACING);
		for (FociForgeComponentBlock.Component com : FociForgeComponentBlock.Component.values()) {
			BlockPos offset = pos.add(com.getOffset(facing));
			if (world.getBlockState(offset).getBlock() == ArcanaBlocks.FOCI_FORGE_COMPONENT.get())
				world.destroyBlock(offset, false);
		}
		super.onBlockHarvested(world, pos, state, player);
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		Direction facing = state.get(FACING);
		boolean broke = false;
		for (FociForgeComponentBlock.Component com : FociForgeComponentBlock.Component.values()) {
			if (world.getBlockState(pos.add(com.getOffset(facing))).getBlock() != ArcanaBlocks.FOCI_FORGE_COMPONENT.get()) {
				broke = true;
				break;
			}
		}
		if (broke) {
			for (FociForgeComponentBlock.Component com : FociForgeComponentBlock.Component.values()) {
				world.destroyBlock(pos.add(com.getOffset(facing)), false);
			}
		}
		super.neighborChanged(state, world, pos, block, fromPos, isMoving);
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction facing = context.getPlacementHorizontalFacing().getOpposite();
		if (!context.getWorld().getBlockState(context.getPos()).isReplaceable(context))
			return null;
		for (FociForgeComponentBlock.Component com : FociForgeComponentBlock.Component.values())
			if (!context.getWorld().getBlockState(context.getPos().add(com.getOffset(facing))).isReplaceable(context))
				return null;
		return this.getDefaultState().with(FACING, facing);
	}

	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		if (!world.isRemote) {
			Direction facing = state.get(FACING);
			for (FociForgeComponentBlock.Component com : FociForgeComponentBlock.Component.values()) {
				BlockPos comPos = pos.add(com.getOffset(facing));
				world.setBlockState(comPos,
						ArcanaBlocks.FOCI_FORGE_COMPONENT.get().getDefaultState()
								.with(FociForgeComponentBlock.FACING, facing)
								.with(FociForgeComponentBlock.COMPONENT, com));
			}
			for (FociForgeComponentBlock.Component com : FociForgeComponentBlock.Component.values()) {
				BlockPos comPos = pos.add(com.getOffset(facing));
				world.notifyNeighborsOfStateChange(comPos, Blocks.AIR);
				state.updateNeighbours(world, comPos, 3);
			}
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
		if(world.isRemote)
			return ActionResultType.SUCCESS;
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof FociForgeTileEntity){
			NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider)te, buf -> buf.writeBlockPos(pos));
			return ActionResultType.SUCCESS;
		}
		return super.onBlockActivated(state, world, pos, player, hand, rayTraceResult);
	}

	@Override
	public Item asItem() {
		return ArcanaItems.FOCI_FORGE_ITEM.get();
	}
}
