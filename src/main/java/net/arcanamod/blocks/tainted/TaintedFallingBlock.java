package net.arcanamod.blocks.tainted;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.Taint;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TaintedFallingBlock extends FallingBlock implements GroupedBlock{
	
	public static final BooleanProperty UNTAINTED = Taint.UNTAINTED;
	
	public TaintedFallingBlock(Block parent){
		super(Properties.from(parent));
		Taint.addTaintMapping(parent, this);
	}
	
	// Thankfully, no falling block has block properties so we don't have to do delegation stuff.
	// If we want to make a tainted anvil, we need to reimplement stuff, sadly.
	
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
	
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random){
		super.tick(state, world, pos, random);
		Taint.tickTaintedBlock(state, world, pos, random);
	}
	
	@Nullable
	@Override
	public ItemGroup getGroup(){
		return Arcana.TAINT;
	}
}
