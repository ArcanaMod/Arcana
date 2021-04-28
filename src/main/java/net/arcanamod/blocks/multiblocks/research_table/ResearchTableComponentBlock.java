package net.arcanamod.blocks.multiblocks.research_table;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.arcanamod.blocks.bases.WaterloggableBlock;
import net.arcanamod.blocks.multiblocks.StaticComponent;
import net.arcanamod.blocks.tiles.ResearchTableTileEntity;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.ShapeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ResearchTableComponentBlock extends WaterloggableBlock implements StaticComponent, GroupedBlock {
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final BooleanProperty PAPER = BooleanProperty.create("paper");
	public static final Vector3i COM_OFFSET = new Vector3i(1, 0, 0);
	public static final Vector3i COM_INVERT = new Vector3i(-1, 0, 0);

	public ResearchTableComponentBlock(Properties properties){
		super(properties);
		setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(PAPER, false));
	}

	@Override
	public BlockState mirror(BlockState state,Mirror mirror) {
		return state.rotate(mirror.toRotation(state.get(FACING)));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation direction) {
		return state.with(FACING, direction.rotate(state.get(FACING)));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	public boolean isCore(BlockPos pos, BlockState state) {
		return false;
	}

	public BlockPos getCorePos(BlockPos pos, BlockState state) {
		return pos.add(ShapeUtils.fromNorth(COM_INVERT, state.get(FACING)));
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING).add(PAPER);
	}

	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockPos corePos = getCorePos(pos, state);
		if (world.getBlockState(corePos).getBlock() == ArcanaBlocks.RESEARCH_TABLE.get()) {
			world.destroyBlock(corePos, false);
		}
		// TODO: loot table that detects harvested by player
		if (!player.isCreative()) {
			spawnDrops(state, world, pos);
		}
		super.onBlockHarvested(world, pos, state, player);
	}

	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(getCorePos(pos, state));
			if (tileentity instanceof ResearchTableTileEntity) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (ResearchTableTileEntity)tileentity);
			}
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		BlockPos corePos = getCorePos(pos, state);
		if (world.getBlockState(corePos).getBlock() != ArcanaBlocks.RESEARCH_TABLE.get())
			world.destroyBlock(pos, false);
		super.neighborChanged(state, world, pos, block, fromPos, isMoving);
	}

	public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}

	public ItemGroup getGroup() {
		return null;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult rayTraceResult) {
		if(world.isRemote)
			return ActionResultType.SUCCESS;
		BlockPos corePos = getCorePos(pos, state);
		TileEntity te = world.getTileEntity(corePos);
		if (te instanceof ResearchTableTileEntity) {
			NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, buf -> buf.writeBlockPos(corePos));
			return ActionResultType.SUCCESS;
		}
		return super.onBlockActivated(state, world, pos, player, handIn, rayTraceResult);
	}

	@Override
	public Item asItem() {
		return ArcanaItems.RESEARCH_TABLE_ITEM.get();
	}
}