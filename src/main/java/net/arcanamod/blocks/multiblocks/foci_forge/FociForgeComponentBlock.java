package net.arcanamod.blocks.multiblocks.foci_forge;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FociForgeComponentBlock extends FociForgeBlock{
	
	public static final BooleanProperty TOP = BooleanProperty.create("top");
	public static final BooleanProperty RIGHT = BooleanProperty.create("right");
	public static final BooleanProperty FORWARD = BooleanProperty.create("forward");
	
	public FociForgeComponentBlock(Properties properties){
		super(properties);
		setDefaultState(stateContainer.getBaseState().with(TOP, false).with(RIGHT, false).with(FORWARD, false));
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		super.fillStateContainer(builder);
		builder.add(TOP, RIGHT, FORWARD);
	}
	
	boolean isTop(BlockState state){
		return state.get(TOP);
	}
	
	boolean isRight(BlockState state){
		return state.get(RIGHT);
	}
	
	boolean isForward(BlockState state){
		return state.get(FORWARD);
	}
}