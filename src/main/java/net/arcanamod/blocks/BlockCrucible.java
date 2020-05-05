package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.bases.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockCrucible extends BlockBase{
	
	protected static final AxisAlignedBB AABB_LEGS = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
	protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);
	
	public BlockCrucible(String name){
		super(name, Material.IRON);
		//setHardness(2f);
	}
	
	public boolean isOpaqueCube(BlockState state){
		return false;
	}
	
	public boolean isFullCube(BlockState state){
		return false;
	}
	
	public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState){
		//addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LEGS);
		//addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST);
		//addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH);
		//addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST);
		//addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH);
	}
	
	/*public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos){
		;
	}*/
	
	/*public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face){
		if(face == Direction.UP)
			return BlockFaceShape.BOWL;
		return face == Direction.DOWN ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
	}*/
}