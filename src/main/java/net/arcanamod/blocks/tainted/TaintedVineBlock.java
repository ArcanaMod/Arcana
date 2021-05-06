package net.arcanamod.blocks.tainted;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.arcanamod.capabilities.TaintTrackable;
import net.arcanamod.systems.taint.Taint;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TaintedVineBlock extends VineBlock implements GroupedBlock{
	public static final BooleanProperty UNTAINTED = Taint.UNTAINTED;
	
	public TaintedVineBlock(Block parent){
		super(Properties.from(parent));
		Taint.addTaintMapping(parent, this);
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
	
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random){
		super.tick(state, world, pos, random);
		Taint.tickTaintedBlock(state, world, pos, random);
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
}
