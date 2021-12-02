package net.arcanamod.blocks.pipes;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PumpBlock extends TubeBlock{
	
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	public PumpBlock(Properties properties){
		super(properties);
		setDefaultState(this.stateContainer.getBaseState()
				.with(NORTH, Boolean.FALSE)
				.with(EAST, Boolean.FALSE)
				.with(SOUTH, Boolean.FALSE)
				.with(WEST, Boolean.FALSE)
				.with(UP, Boolean.FALSE)
				.with(DOWN, Boolean.FALSE)
				.with(FACING, Direction.UP));
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return new PumpTileEntity(state.get(FACING));
	}
	
	public BlockState getStateForPlacement(BlockItemUseContext context){
		return super.getStateForPlacement(context)
				.with(FACING, context.getFace());
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, FACING);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult){
		if(world.isRemote)
			return ActionResultType.SUCCESS;
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof PumpTileEntity){
			NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider)te, buf -> buf.writeBlockPos(pos));
			return ActionResultType.SUCCESS;
		}
		return super.onBlockActivated(state, world, pos, player, hand, rayTraceResult);
	}
	
	@SuppressWarnings("deprecation")
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving){
		super.neighborChanged(state, world, pos, block, fromPos, isMoving);
		if(!world.isRemote()){
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof PumpTileEntity){
				PumpTileEntity alembic = (PumpTileEntity)te;
				alembic.suppressedByRedstone = world.isBlockPowered(pos);
				alembic.markDirty();
				world.notifyBlockUpdate(pos, state, state, Constants.BlockFlags.BLOCK_UPDATE);
			}
		}
	}
}