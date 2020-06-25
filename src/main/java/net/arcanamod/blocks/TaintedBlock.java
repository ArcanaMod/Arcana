package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.arcanamod.world.ServerAuraView;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
public class TaintedBlock extends DelegatingBlock implements GroupedBlock{
	
	public static final BooleanProperty UNTAINTED = BooleanProperty.create("untainted"); // false by default
	
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
		super.randomTick(state, world, pos, random);
		// If I spread taint,
		if(!state.get(UNTAINTED)){
			// and if flux level is greater than 5,
			int at = new ServerAuraView(world).getTaintAt(pos);
			if(at > 5){
				// pick a block within a 4x6x4 area
				// If this block is air, stop. If this block doesn't have a tainted form, re-roll.
				// Do this up to 5 times.
				Block tainted = null;
				BlockPos taintingPos = pos;
				int iter = 0;
				while(tainted == null && iter < 5){
					taintingPos = pos.north(random.nextInt(9) - 4).west(random.nextInt(9) - 4).up(random.nextInt(13) - 6);
					tainted = world.getBlockState(taintingPos).getBlock();
					if(tainted.isAir(world.getBlockState(taintingPos), world, taintingPos)){
						tainted = null;
						break;
					}
					tainted = Taint.getTaintedOfBlock(tainted);
					iter++;
				}
				// Replace it with its tainted form if found.
				if(tainted != null){
					BlockState taintedState = switchBlock(world.getBlockState(taintingPos), tainted).with(UNTAINTED, false);
					world.setBlockState(taintingPos, taintedState);
					// Schedule a tick
					world.getPendingBlockTicks().scheduleTick(pos, this, taintTickWait(at));
				}
			}
		}
	}
	
	private int taintTickWait(int taintLevel){
		// more taint level -> less tick wait
		int base = (int)((1d / taintLevel) * 200);
		return base > 0 ? base : 1;
	}
	
	@Nullable
	@Override
	public ItemGroup getGroup(){
		return Arcana.TAINT;
	}
}