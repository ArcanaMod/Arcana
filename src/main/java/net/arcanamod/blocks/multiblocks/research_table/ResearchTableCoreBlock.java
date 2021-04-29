package net.arcanamod.blocks.multiblocks.research_table;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.arcanamod.blocks.bases.WaterloggableBlock;
import net.arcanamod.blocks.multiblocks.StaticComponent;
import net.arcanamod.blocks.tiles.ResearchTableTileEntity;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.ShapeUtils;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.arcanamod.blocks.multiblocks.research_table.ResearchTableComponentBlock.COM_OFFSET;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ResearchTableCoreBlock extends WaterloggableBlock implements StaticComponent, GroupedBlock {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty INK = BooleanProperty.create("ink");

    public ResearchTableCoreBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public ItemGroup getGroup() {
        return null;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ResearchTableTileEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public boolean isCore(BlockPos pos, BlockState state) {
        return true;
    }

    public BlockPos getCorePos(BlockPos pos, BlockState state) {
        return pos;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING).add(INK);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player){
        BlockPos offset = pos.add(ShapeUtils.fromNorth(COM_OFFSET, state.get(FACING)));
        if(world.getBlockState(offset).getBlock() == ArcanaBlocks.RESEARCH_TABLE_COMPONENT.get()) {
            world.destroyBlock(offset, false);
        }
        super.onBlockHarvested(world, pos, state, player);
    }

    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof ResearchTableTileEntity) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (ResearchTableTileEntity)tileentity);
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        Direction facing = state.get(FACING);
        Vector3i rotated = ShapeUtils.fromNorth(COM_OFFSET, facing);
        if(world.getBlockState(pos.add(rotated)).getBlock() != ArcanaBlocks.RESEARCH_TABLE_COMPONENT.get())
            world.destroyBlock(pos.add(rotated), false);
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction facing = context.getPlacementHorizontalFacing();
        if (!context.getWorld().getBlockState(context.getPos()).isReplaceable(context))
            return null;
        if (!context.getWorld().getBlockState(context.getPos().add(ShapeUtils.fromNorth(COM_OFFSET, facing))).isReplaceable(context))
            return null;
        return this.getDefaultState().with(FACING, facing).with(INK, false);
    }

    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (!world.isRemote) {
            Direction facing = state.get(FACING);
            BlockPos comPos = pos.add(ShapeUtils.fromNorth(COM_OFFSET, facing));
            world.setBlockState(comPos,
                    ArcanaBlocks.RESEARCH_TABLE_COMPONENT.get().getDefaultState()
                            .with(ResearchTableComponentBlock.FACING, facing));
            world.notifyNeighborsOfStateChange(comPos, Blocks.AIR);
            state.updateNeighbours(world, comPos, 3);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if(world.isRemote)
            return ActionResultType.SUCCESS;
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof ResearchTableTileEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, buf -> buf.writeBlockPos(pos));
            return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, world, pos, player, hand, rayTraceResult);
    }

    @Override
    public Item asItem() {
        return ArcanaItems.RESEARCH_TABLE_ITEM.get();
    }
}