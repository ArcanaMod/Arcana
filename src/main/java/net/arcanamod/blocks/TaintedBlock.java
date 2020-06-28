package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.FarmlandWaterManager;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

import static net.minecraft.block.FarmlandBlock.MOISTURE;
import static net.minecraftforge.common.ForgeHooks.onFarmlandTrample;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TaintedBlock extends DelegatingBlock implements GroupedBlock{
	
	public static final BooleanProperty UNTAINTED = Taint.UNTAINTED;
	
	@Deprecated() // Use Taint#taintedOf instead
	public TaintedBlock(Block block){
		super(block);
		Taint.addTaintMapping(block, this);
	}
	
	public boolean ticksRandomly(BlockState state){
		return true;
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		super.fillStateContainer(builder);
		builder.add(UNTAINTED);
	}
	
	public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld world, BlockPos pos1, BlockPos pos2, Hand hand){
		return super.getStateForPlacement(state, facing, state2, world, pos1, pos2, hand).with(UNTAINTED, true);
	}
	
	public BlockState getStateForPlacement(BlockItemUseContext context){
		BlockState placement = super.getStateForPlacement(context);
		return placement != null ? placement.with(UNTAINTED, true) : null;
	}
	
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random){
		// Tainted Farmland yet again
		boolean continueTick = true;
		if(parentBlock == Blocks.FARMLAND){
			if(!state.isValidPosition(world, pos)){
				world.setBlockState(pos, nudgeEntitiesWithNewState(world.getBlockState(pos), ArcanaBlocks.TAINTED_SOIL.get().getDefaultState(), world, pos));
				continueTick = false;
			}else if(!hasWater(world, pos) && !world.isRainingAt(pos.up()))
				if(state.get(MOISTURE) == 0)
					if(!hasCrops(world, pos)){
						world.setBlockState(pos, nudgeEntitiesWithNewState(world.getBlockState(pos), ArcanaBlocks.TAINTED_SOIL.get().getDefaultState(), world, pos));
						continueTick = false;
					}
		}
		if(parentBlock == Blocks.GRASS_PATH){
			if(!state.isValidPosition(world, pos)){
				world.setBlockState(pos, nudgeEntitiesWithNewState(world.getBlockState(pos), ArcanaBlocks.TAINTED_SOIL.get().getDefaultState(), world, pos));
				continueTick = false;
			}
		}
		if(continueTick)
			super.randomTick(state, world, pos, random);
		Taint.tickTaintedBlock(state, world, pos, random);
	}
	
	// Tainted Cactus and Sugar Cane
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable){
		// BlockState plant = plantable.getPlant(world, pos.offset(facing));
		return super.canSustainPlant(state, world, pos, facing, plantable);
				/*|| ((parentBlock == Blocks.GRASS_BLOCK || parentBlock == Blocks.DIRT || parentBlock == Blocks.COARSE_DIRT || parentBlock == Blocks.PODZOL || parentBlock == Blocks.FARMLAND)
						&& plantable instanceof BushBlock)
				|| (parentBlock == Blocks.CACTUS
						&& (plant.getBlock() == Blocks.CACTUS *//*|| plant.getBlock() == ArcanaBlocks.TAINTED_CACTUS*//*))
				|| (parentBlock == Blocks.SUGAR_CANE
						&& (plant.getBlock() == Blocks.SUGAR_CANE *//*|| plant.getBlock() == ArcanaBlocks.TAINTED_SUGAR_CANE*//*));*/
	}
	
	// Make farmland turn to tainted soil
	public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance){
		if(parentBlock == Blocks.FARMLAND){
			// Forge: Move logic to Entity#canTrample
			if(!world.isRemote && onFarmlandTrample(world, pos, Blocks.DIRT.getDefaultState(), fallDistance, entity))
				world.setBlockState(pos, nudgeEntitiesWithNewState(world.getBlockState(pos), ArcanaBlocks.TAINTED_SOIL.get().getDefaultState(), world, pos));
			entity.onLivingFall(fallDistance, 1.0F);
		}else
			super.onFallenUpon(world, pos, entity, fallDistance);
	}
	
	@Nullable
	@Override
	public ItemGroup getGroup(){
		return Arcana.TAINT;
	}
	
	// Private stuff in FarmlandBlock
	// TODO: AT this
	private boolean hasCrops(IBlockReader worldIn, BlockPos pos){
		BlockState state = worldIn.getBlockState(pos.up());
		return state.getBlock() instanceof IPlantable && canSustainPlant(state, worldIn, pos, Direction.UP, (IPlantable)state.getBlock());
	}
	
	private static boolean hasWater(IWorldReader worldIn, BlockPos pos){
		for(BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4)))
			if(worldIn.getFluidState(blockpos).isTagged(FluidTags.WATER))
				return true;
		return FarmlandWaterManager.hasBlockWaterTicket(worldIn, pos);
	}
}