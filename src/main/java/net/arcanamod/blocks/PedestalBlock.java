package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.bases.WaterloggableBlock;
import net.arcanamod.blocks.tiles.PedestalTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PedestalBlock extends WaterloggableBlock{

	protected static final VoxelShape SHAPE = VoxelShapes.or(makeCuboidShape(1, 0, 1, 15, 4, 15), makeCuboidShape(6, 0, 6, 10, 16, 10), makeCuboidShape(3, 12, 3, 13, 16, 13)).simplify();

	public PedestalBlock(Properties properties){
		super(properties);
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return SHAPE;
	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type){
		return false;
	}

	public boolean hasTileEntity(BlockState state){
		return true;
	}

	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return new PedestalTileEntity();
	}

	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTrace) {
		ItemStack itemstack = player.getHeldItem(hand);
		PedestalTileEntity te = (PedestalTileEntity) world.getTileEntity(pos);

		if (te.getItem() == ItemStack.EMPTY) {
			if (!itemstack.isEmpty()) {
				te.setItem(itemstack.split(1));
				te.markDirty();
				return ActionResultType.SUCCESS;
			}
		} else {
			ItemStack pedestalItem = te.getItem();
			if (!pedestalItem.isEmpty() && !player.addItemStackToInventory(pedestalItem)) {
				ItemEntity itementity = new ItemEntity(world,
						player.getPosX(),
						player.getPosY(),
						player.getPosZ(), pedestalItem);
				itementity.setNoPickupDelay();
				world.addEntity(itementity);
			}
			te.setItem(ItemStack.EMPTY);
			te.markDirty();
			return ActionResultType.CONSUME;
		}
		return ActionResultType.PASS;
	}

	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving){
		if(state.getBlock() != newState.getBlock()){
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof PedestalTileEntity)
				InventoryHelper.spawnItemStack(world, te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), ((PedestalTileEntity)te).getItem());
			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}
}