package net.arcanamod.blocks.bases;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.trees.Tree;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * SaplingBlock's constructor is protected, for some reason. This is literally just
 * a copypaste.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ArcanaSaplingBlock extends BushBlock implements IGrowable{
	
	public static final IntegerProperty STAGE = BlockStateProperties.STAGE_0_1;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
	private final Tree tree;
	
	public ArcanaSaplingBlock(Tree treeIn, Block.Properties properties){
		super(properties);
		this.tree = treeIn;
		this.setDefaultState(this.stateContainer.getBaseState().with(STAGE, 0));
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return SHAPE;
	}
	
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand){
		super.tick(state, worldIn, pos, rand);
		if(!worldIn.isAreaLoaded(pos, 1))
			return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if(worldIn.getLight(pos.up()) >= 9 && rand.nextInt(7) == 0)
			this.func_226942_a_(worldIn, pos, state, rand);
	}
	
	public void func_226942_a_(ServerWorld p_226942_1_, BlockPos p_226942_2_, BlockState p_226942_3_, Random p_226942_4_){
		if(p_226942_3_.get(STAGE) == 0)
			p_226942_1_.setBlockState(p_226942_2_, p_226942_3_.cycle(STAGE), 4);
		else{
			if(!ForgeEventFactory.saplingGrowTree(p_226942_1_, p_226942_4_, p_226942_2_))
				return;
			tree.func_225545_a_(p_226942_1_, p_226942_1_.getChunkProvider().getChunkGenerator(), p_226942_2_, p_226942_3_, p_226942_4_);
		}
		
	}
	
	/**
	 * Whether this IGrowable can grow
	 */
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient){
		return true;
	}
	
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state){
		return (double)worldIn.rand.nextFloat() < 0.45D;
	}
	
	public void grow(ServerWorld p_225535_1_, Random p_225535_2_, BlockPos p_225535_3_, BlockState p_225535_4_){
		this.func_226942_a_(p_225535_1_, p_225535_3_, p_225535_4_, p_225535_2_);
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		builder.add(STAGE);
	}
}