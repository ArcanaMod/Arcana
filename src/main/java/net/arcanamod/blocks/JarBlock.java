package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.AspectLabel;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.bases.WaterloggableBlock;
import net.arcanamod.blocks.tiles.JarTileEntity;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.MagicDeviceItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class JarBlock extends WaterloggableBlock {
	public static final BooleanProperty UP = BooleanProperty.create("up");
	private Type type;

	public JarBlock(Properties properties, Type type){
		super(properties);
		this.type = type;
		setDefaultState(stateContainer.getBaseState()
				.with(UP, Boolean.FALSE)
				.with(WATERLOGGED, Boolean.FALSE));
	}
	
	public VoxelShape SHAPE = Block.makeCuboidShape(3, 0, 3, 13, 14, 13);
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return SHAPE;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return SHAPE;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state){
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return new JarTileEntity(this.type);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		super.fillStateContainer(builder);
		builder.add(UP);
	}
	
	public BlockState getStateForPlacement(BlockItemUseContext context){
		return super.getStateForPlacement(context)
				.with(UP, false);
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving){
		if(worldIn.getBlockState(pos.up()).getBlock() instanceof AspectTubeBlock)
			worldIn.setBlockState(pos, state.with(UP, true));
		else
			worldIn.setBlockState(pos, state.with(UP, false));
	}
	
	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving){
		if(worldIn.getBlockState(pos.up()).getBlock() instanceof AspectTubeBlock)
			worldIn.setBlockState(pos, state.with(UP, true));
		else
			worldIn.setBlockState(pos, state.with(UP, false));
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (placer != null && ((JarTileEntity) Objects.requireNonNull(worldIn.getTileEntity(pos))).label != null) {
			((JarTileEntity) Objects.requireNonNull(worldIn.getTileEntity(pos))).label.direction = getYaw(placer);
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		JarTileEntity jar = ((JarTileEntity) Objects.requireNonNull(worldIn.getTileEntity(pos)));
		if (jar.label == null && player.getHeldItem(handIn).getItem() == ArcanaItems.LABEL.get()) {
			if (!player.isCreative()) {
				player.getHeldItem(handIn).setCount(player.getHeldItem(handIn).getCount() - 1);
			}
			if (hit.getFace() != Direction.UP && hit.getFace() != Direction.DOWN) {
				jar.label = new AspectLabel(hit.getFace());
			} else {
				jar.label = new AspectLabel(getYaw(player));
			}
		} else if (player.getHeldItem(handIn).getItem() instanceof MagicDeviceItem && player.isCrouching()) {
			onBlockHarvested(worldIn, pos, state, player);
			worldIn.removeBlock(pos, false);
		} else if (jar.label != null && player.getHeldItem(handIn).getItem() == Blocks.AIR.asItem() && player.isCrouching()) {
			if (!player.isCreative()) {
				if (!player.addItemStackToInventory(new ItemStack(ArcanaItems.LABEL.get()))) {
					ItemEntity itementity = new ItemEntity(worldIn,
							player.getPosX(),
							player.getPosY(),
							player.getPosZ(), new ItemStack(ArcanaItems.LABEL.get()));
					itementity.setNoPickupDelay();
					worldIn.addEntity(itementity);
				}
			}
			jar.label = null;
		} else if (jar.label != null && player.getHeldItem(handIn).getItem() instanceof MagicDeviceItem) {
			if (hit.getFace() != Direction.UP && hit.getFace() != Direction.DOWN) {
				jar.label.direction = hit.getFace();
			} else {
				jar.label.direction = getYaw(player);
			}
			jar.label.seal = Aspects.EMPTY;
		}
		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	}

	@Override
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		if (te instanceof JarTileEntity) {
			JarTileEntity jte = (JarTileEntity) te;
			if (!worldIn.isRemote && jte.vis.getHolder(0).getCurrentVis() == 0 && jte.label == null) {
				te.setPos(new BlockPos(0, 0, 0));
				if (((JarTileEntity) te).label != null) {
					((JarTileEntity) te).label.direction = Direction.NORTH;
				}
				spawnDrops(state, worldIn, pos, te, player, stack);
			}
		}
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!player.isCreative()) {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof JarTileEntity) {
				JarTileEntity jte = (JarTileEntity)te;
				if (!worldIn.isRemote && jte.vis.getHolder(0).getCurrentVis() != 0 || jte.label != null){
					te.setPos(new BlockPos(0, 0, 0));
					if (((JarTileEntity) te).label != null) {
						((JarTileEntity) te).label.direction = Direction.NORTH;
					}
					ItemEntity itementity = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), getItem(worldIn, pos, state));
					itementity.setDefaultPickupDelay();
					worldIn.addEntity(itementity);
				}
			}
		}
		super.onBlockHarvested(worldIn, pos, state, player);
	}

	public static Direction getYaw(LivingEntity player) {
		int yaw = (int)player.rotationYaw;
		if (yaw<0)              //due to the yaw running a -360 to positive 360
			yaw+=360;    //not sure why it's that way
		yaw+=22;    //centers coordinates you may want to drop this line
		yaw%=360;  //and this one if you want a strict interpretation of the zones
		int facing = yaw/45;  //  360degrees divided by 45 == 8 zones
		switch (facing){
			case 0: case 1:
				return Direction.NORTH;
			case 2: case 3:
				return Direction.EAST;
			case 4: case 5:
				return Direction.SOUTH;
			default:
				return Direction.WEST;
		}
	}

	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state){
		ItemStack itemstack = super.getItem(worldIn, pos, state);
		JarTileEntity jarTe = (JarTileEntity)worldIn.getTileEntity(pos);
		CompoundNBT compoundnbt = jarTe.write(new CompoundNBT());
		if(!compoundnbt.isEmpty())
			itemstack.setTagInfo("BlockEntityTag", compoundnbt);
		
		return itemstack;
	}
	
	public boolean hasComparatorInputOverride(BlockState state){
		return true;
	}
	
	public int getComparatorInputOverride(BlockState block, World world, BlockPos pos){
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof JarTileEntity){
			JarTileEntity jar = (JarTileEntity)te;
			return (int)Math.ceil((jar.vis.getHolder(0).getContainedAspectStack().getAmount() / 100f) * 15);
		}
		return 0;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if (stack.getTag() != null)
			if(!stack.getTag().isEmpty()) {
				CompoundNBT cell = stack.getTag().getCompound("BlockEntityTag").getCompound("aspects").getCompound("cells").getCompound("cell_0");
				if (stack.getTag().getCompound("BlockEntityTag").contains("label")) {
					tooltip.add(new StringTextComponent("Labelled").setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.DARK_GRAY))));
				}
				if (cell.getInt("amount") > 0) {
					tooltip.add(new StringTextComponent(AspectUtils.getLocalizedAspectDisplayName(Objects.requireNonNull(
							AspectUtils.getAspectByName(cell.getString("aspect")))) + ": " +
							cell.getInt("amount")).setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.AQUA))));
				}
				if (ArcanaConfig.JAR_ANIMATION_SPEED.get()>=299792458D){ // Small easter egg ;)
					tooltip.add(new StringTextComponent("\"being faster than light leaves you in the darkness\" -jar").setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.GRAY))));
				}
			}
	}

	public enum Type{
		BASIC,
		SECURED,
		VOID,
		VACUUM,
		PRESSURE
	}
}