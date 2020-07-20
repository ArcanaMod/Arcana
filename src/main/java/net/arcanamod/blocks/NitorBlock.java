package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NitorBlock extends Block{
	
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(6, 6, 6, 10, 10, 10);
	
	public NitorBlock(Properties properties){
		super(properties);
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context){
		return SHAPE;
	}
	
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context){
		return VoxelShapes.empty();
	}
	
	public VoxelShape getRenderShape(BlockState state, IBlockReader world, BlockPos pos){
		return VoxelShapes.empty();
	}
	
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand){
		// add a bunch of fire
		double x = pos.getX() + .5;
		double y = pos.getY() + .5;
		double z = pos.getZ() + .5;
		for(int i = 0; i < 3; i++){
			double vX = rand.nextGaussian() / 12;
			double vY = rand.nextGaussian() / 12;
			double vZ = rand.nextGaussian() / 12;
			world.addParticle(ParticleTypes.FLAME, x + vX, y + vY, z + vZ, vX / 16, vY / 16, vZ / 16);
		}
	}
}