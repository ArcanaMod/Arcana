package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.blocks.bases.WaterloggableBlock;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrystalFragmentBlock extends WaterloggableBlock {
    public static final BooleanProperty UP = SixWayBlock.UP;
    public static final BooleanProperty EAST = SixWayBlock.EAST;
    public static final BooleanProperty WEST = SixWayBlock.WEST;
    public static final BooleanProperty NORTH = SixWayBlock.NORTH;
    public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
    public static final BooleanProperty DOWN = SixWayBlock.DOWN;
    public static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = SixWayBlock.FACING_TO_PROPERTY_MAP;
    public static final VoxelShape DOWN_AABB = Block.makeCuboidShape(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    public static final VoxelShape EAST_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    public static final VoxelShape WEST_AABB = Block.makeCuboidShape(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    public static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    public static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    public static final VoxelShape UP_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    private Aspect aspect;

    public CrystalFragmentBlock(Properties properties, Aspect aspect) {
        super(properties);
        this.aspect = aspect;
        setDefaultState(this.stateContainer.getBaseState()
                .with(WATERLOGGED, Boolean.FALSE)
                .with(UP, Boolean.FALSE)
                .with(NORTH, Boolean.FALSE)
                .with(EAST, Boolean.FALSE)
                .with(SOUTH, Boolean.FALSE)
                .with(WEST, Boolean.FALSE)
                .with(DOWN, Boolean.FALSE));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context){
        BlockState state = super.getStateForPlacement(context);
        for (Direction dir : Direction.values()) {
            state = state.with(FACING_TO_PROPERTY_MAP.get(dir), canAttachTo(context.getPos(), context.getWorld(), dir));
        }
        return state;
    }

    private static boolean canAttachTo(BlockPos pos, IWorldReader world, Direction dir) {
        BlockPos offset = pos.offset(dir.getOpposite());
        return Block.doesSideFillSquare(world.getBlockState(offset).getCollisionShape(world, offset), dir);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(UP, NORTH, EAST, SOUTH, WEST, DOWN, WATERLOGGED);
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos){
        for (Direction dir : Direction.values()) {
            state = state.with(FACING_TO_PROPERTY_MAP.get(dir), canAttachTo(currentPos, world, dir));
        }

        return super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            if (canAttachTo(pos, world, dir)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        VoxelShape voxelshape = VoxelShapes.empty();
        if (state.get(UP)) {
            voxelshape = VoxelShapes.or(voxelshape, UP_AABB);
        }
        if (state.get(EAST)) {
            voxelshape = VoxelShapes.or(voxelshape, EAST_AABB);
        }
        if (state.get(WEST)) {
            voxelshape = VoxelShapes.or(voxelshape, WEST_AABB);
        }
        if (state.get(NORTH)) {
            voxelshape = VoxelShapes.or(voxelshape, NORTH_AABB);
        }
        if (state.get(SOUTH)) {
            voxelshape = VoxelShapes.or(voxelshape, SOUTH_AABB);
        }
        if (state.get(DOWN)) {
            voxelshape = VoxelShapes.or(voxelshape, DOWN_AABB);
        }

        return voxelshape;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        boolean someAttached = false;
        for (Direction dir : Direction.values()) {
            boolean attached = canAttachTo(pos, world, dir);
            state = state.with(FACING_TO_PROPERTY_MAP.get(dir), attached);
            if (attached) {
                someAttached = true;
            }
        }
        if (!someAttached) {
            spawnDrops(state, world, pos);
            world.removeBlock(pos, false);
        } else {
            world.setBlockState(pos, state);
            super.neighborChanged(state, world, pos, block, fromPos, isMoving);
        }
    }
}
