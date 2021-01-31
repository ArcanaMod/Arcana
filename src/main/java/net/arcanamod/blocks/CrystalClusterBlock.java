package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.IAspectHolder;
import net.arcanamod.blocks.bases.WaterloggableBlock;
import net.arcanamod.world.AuraView;
import net.arcanamod.world.Node;
import net.arcanamod.world.ServerAuraView;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Random;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrystalClusterBlock extends WaterloggableBlock{
	
	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_3;
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	private Aspect aspect;
	
	public CrystalClusterBlock(Properties properties, Aspect aspect){
		super(properties);
		this.aspect = aspect;
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
	
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random){
		super.randomTick(state, world, pos, random);
		// If we're not fully grown,
		if(state.get(AGE) != 3){
			// Check for any nodes in a 9x9x9 area
			ServerAuraView view = (ServerAuraView)AuraView.SIDED_FACTORY.apply(world);
			Collection<Node> nodes = view.getNodesWithinAABB(new AxisAlignedBB(pos.down(4).south(4).west(4), pos.up(4).north(4).east(4)));
			// For each node in range,
			for(Node node : nodes){
				// If it has more than 4 of our aspect,
				IAspectHolder holder = node.getAspects().findAspectInHolders(aspect);
				if(holder != null && holder.getCurrentVis() > 4){
					// Take 2-4 of the aspect,
					holder.drain(new AspectStack(aspect, world.rand.nextInt(3) + 2), false);
					// Sync the node,
					view.sendChunkToClients(node);
					// Increment out growth stage,
					world.setBlockState(pos, state.with(AGE, state.get(AGE) + 1));
					// And stop
					break;
				}
			}
		}
	}
}