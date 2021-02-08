package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.blocks.tiles.JarTileEntity;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class JarBlock extends Block{
	public static final BooleanProperty UP = BooleanProperty.create("up");
	private Type type;

	Logger LOGGER = LogManager.getLogger();
	
	public JarBlock(Properties properties, Type type){
		super(properties);
		this.type = type;
		setDefaultState(stateContainer.getBaseState().with(UP, Boolean.FALSE));
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
		return super.getStateForPlacement(context).with(UP, false);
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
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (player.getHeldItem(handIn).getItem() == ArcanaItems.LABEL.get()) {
			((JarTileEntity) worldIn.getTileEntity(pos)).label = getYaw(player);
			LOGGER.debug(getYaw(player));
		}
		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	}

	public static Direction getYaw(PlayerEntity player) {
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
				tooltip.add(new StringTextComponent(AspectUtils.getLocalizedAspectDisplayName(AspectUtils.getAspectByName(cell.getString("aspect")))+": " +
						cell.getInt("amount")).applyTextStyle(TextFormatting.AQUA));
				if (ArcanaConfig.JAR_ANIMATION_SPEED.get()>=299792458D){ // Small easter egg ;)
					tooltip.add(new StringTextComponent("\"being faster than light leaves you in the darkness\" -jar").applyTextStyle(TextFormatting.GRAY));
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