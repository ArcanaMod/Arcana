package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.ArcanaSounds;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.arcanamod.capabilities.TaintTrackable;
import net.arcanamod.systems.taint.Taint;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.FarmlandWaterManager;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

import static net.minecraft.block.FarmlandBlock.MOISTURE;
import static net.minecraft.block.SnowyDirtBlock.SNOWY;
import static net.minecraftforge.common.ForgeHooks.onFarmlandTrample;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TaintedBlock extends DelegatingBlock implements GroupedBlock{
	
	public static final BooleanProperty UNTAINTED = Taint.UNTAINTED;
	
	@Deprecated() // Use Taint#taintedOf instead
	public TaintedBlock(Block block){
		super(block, ArcanaSounds.TAINT);
		Taint.addTaintMapping(block, this);
	}
	
	public IFormattableTextComponent getTranslatedName(){
		return new TranslationTextComponent("arcana.status.tainted", super.getTranslatedName());
	}
	
	public boolean ticksRandomly(BlockState state){
		return true;
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		super.fillStateContainer(builder);
		builder.add(UNTAINTED);
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
				world.setBlockState(pos, nudgeEntitiesWithNewState(world.getBlockState(pos), ArcanaBlocks.TAINTED_SOIL.get().getDefaultState().with(UNTAINTED, state.get(UNTAINTED)), world, pos));
				continueTick = false;
			}else if(!hasWater(world, pos) && !world.isRainingAt(pos.up()))
				if(state.get(MOISTURE) == 0)
					if(!hasCrops(world, pos)){
						world.setBlockState(pos, nudgeEntitiesWithNewState(world.getBlockState(pos), ArcanaBlocks.TAINTED_SOIL.get().getDefaultState().with(UNTAINTED, state.get(UNTAINTED)), world, pos));
						continueTick = false;
					}
		}
		// Tainted grass path decays into tainted soil
		if(parentBlock == Blocks.GRASS_PATH){
			if(!state.isValidPosition(world, pos))
				world.setBlockState(pos, nudgeEntitiesWithNewState(world.getBlockState(pos), ArcanaBlocks.TAINTED_SOIL.get().getDefaultState().with(UNTAINTED, state.get(UNTAINTED)), world, pos));
			continueTick = false;
		}
		// And tainted grass decays into tainted soil, and spreads.
		// Should also cover mycelium.
		if(parentBlock instanceof SpreadableSnowyDirtBlock){
			if(!isLocationUncovered(state, world, pos)){
				if(!world.isAreaLoaded(pos, 3))
					return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
				world.setBlockState(pos, ArcanaBlocks.TAINTED_SOIL.get().getDefaultState().with(UNTAINTED, state.get(UNTAINTED)));
			}else if(world.getLight(pos.up()) >= 9){
				BlockState blockstate = getDefaultState();
				for(int i = 0; i < 4; ++i){
					BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
					if(world.getBlockState(blockpos).getBlock() == ArcanaBlocks.TAINTED_SOIL.get() && isLocationValidForGrass(blockstate, world, blockpos))
						world.setBlockState(blockpos, blockstate.with(SNOWY, world.getBlockState(blockpos.up()).getBlock() == Blocks.SNOW).with(UNTAINTED, state.get(UNTAINTED)));
				}
			}
			continueTick = false;
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
	
	@SuppressWarnings("deprecation")
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity){
		super.onEntityCollision(state, world, pos, entity);
		startTracking(entity);
	}
	
	public void onEntityWalk(World world, BlockPos pos, Entity entity){
		super.onEntityWalk(world, pos, entity);
		startTracking(entity);
	}
	
	private void startTracking(Entity entity){
		if(entity instanceof LivingEntity){
			// Start tracking taint biome for entity
			TaintTrackable trackable = TaintTrackable.getFrom((LivingEntity)entity);
			if(trackable != null)
				trackable.setTracking(true);
		}
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
	
	// Private stuff in SpreadableSnowyDirtBlock
	private static boolean isLocationUncovered(BlockState state, IWorldReader world, BlockPos pos){
		BlockPos blockpos = pos.up();
		BlockState blockstate = world.getBlockState(blockpos);
		if(blockstate.getBlock() == Blocks.SNOW && blockstate.get(SnowBlock.LAYERS) == 1)
			return true;
		else{
			int i = LightEngine.func_215613_a(world, state, pos, blockstate, blockpos, Direction.UP, blockstate.getOpacity(world, blockpos));
			return i < world.getMaxLightLevel();
		}
	}
	
	private static boolean isLocationValidForGrass(BlockState state, IWorldReader world, BlockPos pos){
		BlockPos blockpos = pos.up();
		return isLocationUncovered(state, world, pos) && !world.getFluidState(blockpos).isTagged(FluidTags.WATER);
	}
}