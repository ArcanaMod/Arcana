package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.bases.WaterloggableBlock;
import net.arcanamod.blocks.tiles.AspectBookshelfTileEntity;
import net.arcanamod.items.PhialItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.arcanamod.ArcanaSounds.playPhialshelfSlideSound;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectBookshelfBlock extends WaterloggableBlock{
	public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.FACING;
	public static final BooleanProperty FULL_SIZE = BlockStateProperties.EXTENDED;
	public VoxelShape SHAPE_NORTH = Block.makeCuboidShape(0, 0, 8, 16, 16, 16);
	public VoxelShape SHAPE_SOUTH = Block.makeCuboidShape(0, 0, 0, 16, 16, 8);
	public VoxelShape SHAPE_EAST = Block.makeCuboidShape(0, 0, 0, 8, 16, 16);
	public VoxelShape SHAPE_WEST = Block.makeCuboidShape(8, 0, 0, 16, 16, 16);
	public VoxelShape SHAPE_UP = Block.makeCuboidShape(0, 8, 0, 16, 16, 16);
	public VoxelShape SHAPE_DOWN = Block.makeCuboidShape(0, 0, 0, 16, 8, 16);

	public AspectBookshelfBlock(boolean fullBlock, Properties properties){
		super(properties);
		setDefaultState(stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(WATERLOGGED, Boolean.FALSE).with(FULL_SIZE, fullBlock));
	}

	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof AspectBookshelfTileEntity) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (AspectBookshelfTileEntity)tileentity);
			}
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(id, param);
	}

	@Nullable
	public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider)tileentity : null;
	}

	public BlockState getStateForPlacement(BlockItemUseContext context){
		if (context.getPlayer() != null) {
			if (context.getPlayer().isCrouching() && context.getFace().getOpposite() != Direction.UP) {
				return super.getStateForPlacement(context).with(HORIZONTAL_FACING, context.getFace().getOpposite());
			}
		}
		return super.getStateForPlacement(context).with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, WATERLOGGED, FULL_SIZE);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if (state.get(FULL_SIZE)) {
			return super.getShape(state, worldIn, pos, context);
		} else {
			switch (state.get(HORIZONTAL_FACING)) {
				case SOUTH:
					return SHAPE_SOUTH;
				case EAST:
					return SHAPE_EAST;
				case WEST:
					return SHAPE_WEST;
				case UP:
					return SHAPE_UP;
				case DOWN:
					return SHAPE_DOWN;
				default:
					return SHAPE_NORTH;
			}
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context){
		return getShape(state, world, pos, context);
	}

	public BlockState rotate(BlockState state, Rotation rot){
		return state.with(HORIZONTAL_FACING, rot.rotate(state.get(HORIZONTAL_FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn){
		return state.rotate(mirrorIn.toRotation(state.get(HORIZONTAL_FACING)));
	}

	@Nullable @Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new AspectBookshelfTileEntity(state.get(HORIZONTAL_FACING));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
		TileEntity te = worldIn.getTileEntity(pos);
		boolean vert = p_225533_6_.getFace() == Direction.UP || p_225533_6_.getFace() == Direction.DOWN;
		if ((vert && p_225533_6_.getFace() == state.get(HORIZONTAL_FACING).getOpposite()) || (!vert && p_225533_6_.getFace() == state.get(HORIZONTAL_FACING))) {
			int widthSlot = -1;
			int heightSlot = -1;
			switch (state.get(HORIZONTAL_FACING)) {
				case NORTH:
					widthSlot = 3 - (int) ((p_225533_6_.getHitVec().x - pos.getX()) / .33);
					heightSlot = 3 - (int) ((p_225533_6_.getHitVec().y - pos.getY()) / .33);
					break;
				case SOUTH:
					widthSlot = 1 + (int) ((p_225533_6_.getHitVec().x - pos.getX()) / .33);
					heightSlot = 3 - (int) ((p_225533_6_.getHitVec().y - pos.getY()) / .33);
					break;
				case EAST:
					widthSlot = 3 - (int) ((p_225533_6_.getHitVec().z - pos.getZ()) / .33);
					heightSlot = 3 - (int) ((p_225533_6_.getHitVec().y - pos.getY()) / .33);
					break;
				case WEST:
					widthSlot = 1 + (int) ((p_225533_6_.getHitVec().z - pos.getZ()) / .33);
					heightSlot = 3 - (int) ((p_225533_6_.getHitVec().y - pos.getY()) / .33);
					break;
				case UP:
					widthSlot = 3 - (int) ((p_225533_6_.getHitVec().x - pos.getX()) / .33);
					heightSlot = 1 + (int) ((p_225533_6_.getHitVec().z - pos.getZ()) / .33);
					break;
				case DOWN:
					widthSlot = 3 - (int) ((p_225533_6_.getHitVec().x - pos.getX()) / .33);
					heightSlot = 3 - (int) ((p_225533_6_.getHitVec().z - pos.getZ()) / .33);
					break;
			}
			if (heightSlot <= 0) {
				heightSlot = 1;
			} else if (heightSlot >= 4) {
				heightSlot = 3;
			}
			if (widthSlot <= 0) {
				widthSlot = 1;
			} else if (widthSlot >= 4) {
				widthSlot = 3;
			}
			int slot = (widthSlot + ((heightSlot - 1) * 3)) - 1;

			if (te instanceof AspectBookshelfTileEntity) {
				AspectBookshelfTileEntity abe = (AspectBookshelfTileEntity) te;
				if (player.isCrouching()) {
					player.openContainer(abe);
				} else if (player.getHeldItem(handIn).getItem() instanceof PhialItem && abe.addPhial(player.getHeldItem(handIn), slot)) {
					player.getHeldItem(handIn).shrink(1);
					playPhialshelfSlideSound(player);
				} else {
					ItemStack returned = abe.removePhial(slot);
					if (returned != ItemStack.EMPTY) {
						if (!player.addItemStackToInventory(returned)) {
							ItemEntity itementity = new ItemEntity(worldIn,
									player.getPosX(),
									player.getPosY(),
									player.getPosZ(), returned);
							itementity.setNoPickupDelay();
							worldIn.addEntity(itementity);
							playPhialshelfSlideSound(player);
						}
					} else {
						return ActionResultType.PASS;
					}
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	public boolean hasComparatorInputOverride(BlockState state){
		return true;
	}

	public int getComparatorInputOverride(BlockState block, World world, BlockPos pos){
		TileEntity te = world.getTileEntity(pos);
		assert te != null;
		return ((AspectBookshelfTileEntity)te).getRedstoneOut();
	}
}