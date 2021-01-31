package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.bases.WaterloggableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrystalClusterBlock extends WaterloggableBlock{
	
	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_3;
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	public CrystalClusterBlock(Properties properties){
		super(properties);
		setDefaultState(stateContainer.getBaseState().with(WATERLOGGED, Boolean.FALSE).with(AGE, 3).with(FACING, Direction.UP));
	}
	
	@Nonnull
	public BlockState getStateForPlacement(BlockItemUseContext context){
		// Placement = the block item, which you get with silk touch, so fully grown
		return super.getStateForPlacement(context).with(AGE, 3).with(FACING, context.getFace());
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		builder.add(FACING, WATERLOGGED, AGE);
	}
	
	public boolean hasComparatorInputOverride(BlockState state){
		return true;
	}
	
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos){
		// output comparator signal when fully grown
		return state.get(AGE) == 3 ? 15 : 0;
	}
	
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos){
		return facing == state.get(FACING).getOpposite() && !this.isValidPosition(state, world, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
	}
	
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos){
		Direction dir = state.get(FACING);
		return hasEnoughSolidSide(world, pos.offset(dir.getOpposite()), dir);
	}
	
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player){
		// They're not ItemBlocks
		return new ItemStack(ForgeRegistries.ITEMS.getValue(getRegistryName()));
	}
}