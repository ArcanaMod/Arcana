package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.tiles.JarTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class JarBlock extends Block{
	public static final BooleanProperty UP = BooleanProperty.create("up");
	private Type type;
	
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
	
	public enum Type{
		BASIC,
		SECURED,
		VOID
	}
}