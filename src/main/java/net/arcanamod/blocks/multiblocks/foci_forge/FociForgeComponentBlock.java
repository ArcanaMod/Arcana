package net.arcanamod.blocks.multiblocks.foci_forge;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.arcanamod.blocks.multiblocks.IStaticEnum;
import net.arcanamod.blocks.multiblocks.StaticComponent;
import net.arcanamod.blocks.tiles.FociForgeTileEntity;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.ShapeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FociForgeComponentBlock extends Block implements StaticComponent, GroupedBlock {

	public static final VoxelShape SHAPE_N = VoxelShapes.or(
			VoxelShapes.create(0, 0, 0, 32 / 16f, 4 / 16f, 32 / 16f),
			VoxelShapes.create(0, 4 / 16f, 17 / 16f, 32 / 16f, 16 / 16f, 31 / 16f),
			VoxelShapes.create(26 / 16f, 4 / 16f, 1 / 16f, 32 / 16f, 27 / 16f, 31 / 16f),
			VoxelShapes.create(18 / 16f, 16 / 16f, 19 / 16f, 26 / 16f, 30 / 16f, 28 / 16f)
	).simplify();
	public static final VoxelShape SHAPE_E = ShapeUtils.rotate(SHAPE_N, Direction.EAST);
	public static final VoxelShape SHAPE_S = ShapeUtils.rotate(SHAPE_N, Direction.SOUTH);
	public static final VoxelShape SHAPE_W = ShapeUtils.rotate(SHAPE_N, Direction.WEST);

	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final EnumProperty<Component> COMPONENT = EnumProperty.create("ff_com", Component.class);
	
	public FociForgeComponentBlock(Properties properties){
		super(properties);
		setDefaultState(stateContainer.getBaseState()
				.with(FACING, Direction.NORTH)
				.with(COMPONENT, Component.F));
	}

	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		VoxelShape shape;
		Direction facing = state.get(FACING);
		Vector3i fromCore = state.get(COMPONENT).getInvert(facing);
		switch(facing) {
			case EAST:
				shape = SHAPE_E;
				break;
			case SOUTH:
				shape = SHAPE_S;
				break;
			case WEST:
				shape = SHAPE_W;
				break;
			case NORTH:
			default:
				shape = SHAPE_N;
				break;
		}
		return shape.withOffset(fromCore.getX(), fromCore.getY(), fromCore.getZ());
	}

		@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.toRotation(state.get(FACING)));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation direction) {
		return state.with(FACING, direction.rotate(state.get(FACING)));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	public boolean isCore(BlockPos pos, BlockState state) {
		return false;
	}

	public BlockPos getCorePos(BlockPos pos, BlockState state) {
		return pos.add(state.get(COMPONENT).getInvert(state.get(FACING)));
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		super.fillStateContainer(builder);
		builder.add(FACING, COMPONENT);
	}

	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player){
		BlockPos corePos = getCorePos(pos, state);
		if(world.getBlockState(corePos).getBlock() == ArcanaBlocks.FOCI_FORGE.get())
			world.destroyBlock(corePos, false);
		// Components don't naturally spawn drops, for some reason
		if (!player.isCreative())
			spawnDrops(state, world, pos);
		super.onBlockHarvested(world, pos, state, player);
	}

	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		BlockPos corePos = getCorePos(pos, state);
		if (world.getBlockState(corePos).getBlock() != ArcanaBlocks.FOCI_FORGE.get())
			world.destroyBlock(pos, false);
		super.neighborChanged(state, world, pos, block, fromPos, isMoving);
	}

	public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}

	public ItemGroup getGroup() {
		return null;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult rayTraceResult) {
		if(world.isRemote)
			return ActionResultType.SUCCESS;
		BlockPos corePos = getCorePos(pos, state);
		TileEntity te = world.getTileEntity(corePos);
		if (te instanceof FociForgeTileEntity) {
			NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, buf -> buf.writeBlockPos(corePos));
			return ActionResultType.SUCCESS;
		}
		return super.onBlockActivated(state, world, pos, player, handIn, rayTraceResult);
	}

	public enum Component implements IStaticEnum {
		U("u", 0, 1, 0),
		UR("ur", 1, 1, 0),
		R("r", 1, 0, 0),
		F("f",0, 0, 1),
		FU("fu",0, 1, 1),
		FUR("fur", 1, 1, 1),
		FR("fr", 1, 0, 1);

		private final String name;
		private final Vector3i offset;
		private final Vector3i invert;

		Component(String name, int x, int y, int z) {
			this.name = name;
			this.offset = new Vector3i(x, y, z);
			this.invert = new Vector3i(-x, -y, -z);
		}

		public String getName() {
			return name;
		}

		public Vector3i getOffset(Direction direction) {
			return ShapeUtils.fromNorth(this.offset, direction);
		}

		public Vector3i getInvert(Direction direction) {
			return ShapeUtils.fromNorth(this.invert, direction);
		}
		
		public String getString(){
			return name;
		}
	}

	@Override
	public Item asItem() {
		return ArcanaItems.FOCI_FORGE_ITEM.get();
	}
}