package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.ArcanaSounds;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.blocks.bases.HorizontalWaterloggableBlock;
import net.arcanamod.blocks.bases.WaterloggableBlock;
import net.arcanamod.blocks.tiles.AspectBookshelfTileEntity;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.PhialItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.arcanamod.ArcanaSounds.playPhialshelfSlideSound;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectBookshelfBlock extends WaterloggableBlock{
	public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
	public VoxelShape SHAPE_NORTH = Block.makeCuboidShape(0, 0, 8, 16, 16, 16);
	public VoxelShape SHAPE_SOUTH = Block.makeCuboidShape(0, 0, 0, 16, 16, 8);
	public VoxelShape SHAPE_EAST = Block.makeCuboidShape(0, 0, 0, 8, 16, 16);
	public VoxelShape SHAPE_WEST = Block.makeCuboidShape(8, 0, 0, 16, 16, 16);

	public static final IntegerProperty LEVEL_0_9 = IntegerProperty.create("level",0,9);

	public AspectBookshelfBlock(Properties properties){
		super(properties);
		setDefaultState(stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(WATERLOGGED, Boolean.FALSE).with(LEVEL_0_9, 0));
	}

	public BlockState getStateForPlacement(BlockItemUseContext context){
		return super.getStateForPlacement(context).with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(LEVEL_0_9,0);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		builder.add(HORIZONTAL_FACING, WATERLOGGED, LEVEL_0_9);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		if(state.get(HORIZONTAL_FACING) == Direction.NORTH)
			return SHAPE_NORTH;
		if(state.get(HORIZONTAL_FACING) == Direction.SOUTH)
			return SHAPE_SOUTH;
		if(state.get(HORIZONTAL_FACING) == Direction.EAST)
			return SHAPE_EAST;
		else
			return SHAPE_WEST;
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

		if (p_225533_6_.getFace() == state.get(HORIZONTAL_FACING)) {
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
			}
			if (heightSlot == 0) {
				heightSlot = 1;
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
									pos.getX() + .5 + (state.get(HORIZONTAL_FACING).getXOffset() * .4),
									pos.getY() + .5 + (state.get(HORIZONTAL_FACING).getYOffset() * .4),
									pos.getZ() + .5 + (state.get(HORIZONTAL_FACING).getZOffset() * .4), returned);
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
		return ((AspectBookshelfTileEntity)te).getRedstoneOut();
	}
}