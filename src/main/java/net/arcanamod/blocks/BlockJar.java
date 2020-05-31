package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.VisHandler;
import net.arcanamod.aspects.VisHandlerCapability;
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
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
	{
		ItemStack heldItem = player.getHeldItem(handIn);
		if (heldItem.getItem() instanceof PhialItem)
		{
			if (worldIn.getTileEntity(pos) instanceof JarTileEntity)
			{
				JarTileEntity jarTE = ((JarTileEntity) worldIn.getTileEntity(pos));

				VisHandler vis = heldItem.getCapability(VisHandlerCapability.ASPECT_HANDLER).orElse(null);
				if (vis != null)
				{
					if (vis.getContainedAspects().size()==0)
					{
						if (jarTE.vis.getCurrentVis(jarTE.allowedAspect)>=8)
						{
							ItemStack capedItemStack = new ItemStack(ArcanaItems.PHIAL.get());
							capedItemStack.getCapability(VisHandlerCapability.ASPECT_HANDLER).orElse(null).insert(jarTE.allowedAspect,8,false);
							player.addItemStackToInventory(capedItemStack);
							jarTE.drain(8);
						}
					}
					else
					{
						if (jarTE.vis.getCurrentVis(jarTE.allowedAspect)<100)
						{
							Aspect target = ((Aspect)(vis.getContainedAspects().toArray()[0]));
							if (target==jarTE.allowedAspect||jarTE.allowedAspect==Aspect.EMPTY)
							{
								//vis.drain(target,vis.getCurrentVis(target),false);
								heldItem.shrink(1);
								player.addItemStackToInventory(new ItemStack(ArcanaItems.PHIAL.get()));
								jarTE.fill(vis.getCurrentVis(target), target);
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