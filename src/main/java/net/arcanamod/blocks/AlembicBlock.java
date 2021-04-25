package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.IAspectHolder;
import net.arcanamod.blocks.tiles.AlembicTileEntity;
import net.arcanamod.world.AuraView;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlembicBlock extends Block{
	
	protected static final VoxelShape SHAPE = VoxelShapes.or(
			makeCuboidShape(1, 1, 1, 15, 15, 15),
			makeCuboidShape(0, 2, 0, 16, 4, 16),
			makeCuboidShape(0, 12, 0, 16, 14, 16),
			makeCuboidShape(4, 0, 4, 12, 2, 12),
			makeCuboidShape(4, 14, 4, 12, 16, 12)
	).simplify();
	
	public AlembicBlock(Properties properties){
		super(properties);
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return SHAPE;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state){
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return new AlembicTileEntity();
	}
	
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving){
		super.neighborChanged(state, world, pos, block, fromPos, isMoving);
		if(!world.isRemote()){
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof AlembicTileEntity){
				AlembicTileEntity alembic = (AlembicTileEntity)te;
				alembic.suppressedByRedstone = world.isBlockPowered(pos);
				alembic.markDirty();
				world.notifyBlockUpdate(pos, state, state, Constants.BlockFlags.BLOCK_UPDATE);
			}
		}
	}
	
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit){
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof AlembicTileEntity && player.getHeldItem(hand).isEmpty() && player.isCrouching()){
			AlembicTileEntity alembic = (AlembicTileEntity)te;
			// get rid of the content of the alembic
			for(IAspectHolder holder : ((AlembicTileEntity)te).aspects.getHolders()){
				AuraView.getSided(world).addFluxAt(pos, (float)(holder.getCurrentVis() * ArcanaConfig.ASPECT_DUMPING_WASTE.get()));
				holder.drain(holder.getContainedAspectStack(), false);
				// TODO: flux particles
			}
			alembic.markDirty();
			world.notifyBlockUpdate(pos, state, state, Constants.BlockFlags.BLOCK_UPDATE);
		}
		return super.onBlockActivated(state, world, pos, player, hand, hit);
	}
}