package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.*;
import net.arcanamod.blocks.tiles.JarTileEntity;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.PhialItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockJar extends Block{
 
	public BlockJar(Properties properties){
		super(properties);
	}
	
	public VoxelShape SHAPE = Block.makeCuboidShape(3, 0, 3, 13, 14, 13);
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return SHAPE;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return SHAPE;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state){
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return new JarTileEntity();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
		ItemStack heldItem = player.getHeldItem(handIn);
		if (heldItem.getItem() instanceof PhialItem) {
			if (worldIn.getTileEntity(pos) instanceof JarTileEntity) {
				JarTileEntity jarTileEntity = ((JarTileEntity) worldIn.getTileEntity(pos));

				IAspectHandler vis = IAspectHandler.getFrom(heldItem);
				if (vis != null) {
					if (vis.getHolder(0).getCurrentVis() <= 0) {
						int amount = jarTileEntity.vis.getHolder(0).getCurrentVis();
						heldItem.shrink(1);
						ItemStack capedItemStack = new ItemStack(ArcanaItems.PHIAL.get());
						IAspectHandler.getFrom(capedItemStack)
								.insert(0, new AspectStack(jarTileEntity.allowedAspect, amount >= 8 ? 8 : amount),false);
						if (capedItemStack.getTag() == null)
						{
							capedItemStack.setTag(capedItemStack.getShareTag());
						}
						player.addItemStackToInventory(capedItemStack);
						jarTileEntity.drain(amount >= 8 ? 8 : amount);
					} else {
						if (jarTileEntity.vis.getHolder(0).getCurrentVis()<100) {
							Aspect target = vis.getHolder(0).getContainedAspect();
							if (target==jarTileEntity.allowedAspect||jarTileEntity.allowedAspect==Aspect.EMPTY) {
								//vis.drain(target,vis.getCurrentVis(target),false);
								heldItem.shrink(1);
								player.addItemStackToInventory(new ItemStack(ArcanaItems.PHIAL.get()));
								jarTileEntity.fill(vis.getHolder(0).getCurrentVis(), target);
							}
						}
					}
					return ActionResultType.SUCCESS;
				}
			}
		}
		return super.onBlockActivated(state, worldIn, pos, player, handIn, p_225533_6_);
	}
}