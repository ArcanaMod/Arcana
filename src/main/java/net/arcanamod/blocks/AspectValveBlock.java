package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.tiles.AspectValveTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectValveBlock extends AspectTubeBlock{
	
	protected AspectValveBlock(Properties properties){
		super(properties);
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return new AspectValveTileEntity();
	}
	
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult raytrace){
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof AspectValveTileEntity){
			AspectValveTileEntity valve = (AspectValveTileEntity)te;
			valve.setEnabledAndNotify(!valve.enabledByHand());
			return ActionResultType.SUCCESS;
		}
		return super.onBlockActivated(state, world, pos, player, hand, raytrace);
	}
	
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving){
		super.neighborChanged(state, world, pos, block, fromPos, isMoving);
		if(!world.isRemote()){
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof AspectValveTileEntity){
				AspectValveTileEntity valve = (AspectValveTileEntity)te;
				valve.setSuppressedByRedstone(world.isBlockPowered(pos));
				valve.markDirty();
				world.notifyBlockUpdate(pos, state, state, Constants.BlockFlags.BLOCK_UPDATE);
			}
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand){
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof AspectValveTileEntity && ((AspectValveTileEntity)te).isSuppressedByRedstone() && rand.nextFloat() < 0.25F)
			addParticles(world, pos);
	}
	
	private static void addParticles(IWorld world, BlockPos pos){
		double x = (double)pos.getX() + .5;
		double y = (double)pos.getY() + 1;
		double z = (double)pos.getZ() + .5;
		world.addParticle(RedstoneParticleData.REDSTONE_DUST, x, y, z, 0.0D, 0.0D, 0.0D);
	}
	
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side){
		return true;
	}
}