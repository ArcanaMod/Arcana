package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
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
		if (player.getHeldItem(handIn).getItem() instanceof PhialItem)
		{
			if (worldIn.getTileEntity(pos) instanceof JarTileEntity)
			{
				JarTileEntity jarTE = ((JarTileEntity) worldIn.getTileEntity(pos));
				/*if (jarTE.vis.getCurrentVis(jarTE.allowedAspect)<100)
				{
					if (jarTE.getAllowedAspect()!=)
					player.getHeldItem(handIn).shrink(1);
					player.addItemStackToInventory(new ItemStack(ArcanaItems.EMPTY_PHIAL.get()));
					jarTE.fill(2);
					return ActionResultType.SUCCESS;
				}*/
			}
		}
		//Empty Phial isn't instance of PhialItem because has no aspect inside.
		else if (/*player.getHeldItem(handIn).getItem() == ArcanaItems.EMPTY_PHIAL.get()*/false)
		{
			if (worldIn.getTileEntity(pos) instanceof JarTileEntity)
			{
				JarTileEntity jarTE = ((JarTileEntity) worldIn.getTileEntity(pos));
				/*if (jarTE.vis.getCurrentVis(jarTE.allowedAspect)<100)
				{
					player.getHeldItem(handIn).shrink(1);
					player.addItemStackToInventory(new ItemStack(Aspects.getPhialItemStackForAspect(Aspect.GREED).getItem()));//TODO: check aspect in jar and get PhialItem with that aspect.
					jarTE.drain(2);
					return ActionResultType.SUCCESS;
				}*/
			}
		}
		return super.onBlockActivated(state, worldIn, pos, player, handIn, p_225533_6_);
	}
}