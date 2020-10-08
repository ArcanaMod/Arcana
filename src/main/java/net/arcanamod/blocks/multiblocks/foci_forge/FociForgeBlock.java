package net.arcanamod.blocks.multiblocks.foci_forge;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class FociForgeBlock extends Block implements GroupedBlock{
	
	public static final VoxelShape SHAPE = VoxelShapes.or(
			VoxelShapes.create(0, 0, 0, 32 / 16f, 4 / 16f, 32 / 16f),
			VoxelShapes.create(0, 4 / 16f, 17 / 16f, 32 / 16f, 16 / 16f, 31 / 16f),
			VoxelShapes.create(26 / 16f, 4 / 16f, 1 / 16f, 32 / 16f, 27 / 16f, 31 / 16f),
			VoxelShapes.create(18 / 16f, 16 / 16f, 19 / 16f, 26 / 16f, 30 / 16f, 28 / 16f)
	).simplify();
	
	public FociForgeBlock(Properties properties){
		super(properties);
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context){
		return SHAPE.withOffset(isForward(state) ? -1 : 0, isTop(state) ? -1 : 0, isRight(state) ? -1 : 0);
	}
	
	abstract boolean isTop(BlockState state);
	
	abstract boolean isRight(BlockState state);
	
	abstract boolean isForward(BlockState state);
	
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving){
		// check the blocks next to us
		if(isTop(state)){
			if(!(world.getBlockState(pos.down()).getBlock() instanceof FociForgeBlock))
				world.destroyBlock(pos, true);
		}else if(!(world.getBlockState(pos.up()).getBlock() instanceof FociForgeBlock))
			world.destroyBlock(pos, true);
		
		if(isRight(state)){
			if(!(world.getBlockState(pos.north()).getBlock() instanceof FociForgeBlock))
				world.destroyBlock(pos, true);
		}else if(!(world.getBlockState(pos.south()).getBlock() instanceof FociForgeBlock))
			world.destroyBlock(pos, true);
		
		if(isForward(state)){
			if(!(world.getBlockState(pos.west()).getBlock() instanceof FociForgeBlock))
				world.destroyBlock(pos, true);
		}else if(!(world.getBlockState(pos.east()).getBlock() instanceof FociForgeBlock))
			world.destroyBlock(pos, true);
		// h
		super.neighborChanged(state, world, pos, block, fromPos, isMoving);
	}
	
	public BlockRenderType getRenderType(BlockState state){
		return BlockRenderType.INVISIBLE;
	}
	
	@Nullable
	public ItemGroup getGroup(){
		return null;
	}
}