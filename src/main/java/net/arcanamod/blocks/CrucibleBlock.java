package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.tiles.CrucibleTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrucibleBlock extends Block{
	
	public static final VoxelShape INSIDE = makeCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 15.0D, 14.0D);
	protected static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(makeCuboidShape(0, 0, 0, 16, 15, 16), VoxelShapes.or(makeCuboidShape(0.0D, 0.0D, 3.0D, 16.0D, 3.0D, 13.0D), makeCuboidShape(3.0D, 0.0D, 0.0D, 13.0D, 3.0D, 16.0D), makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), IBooleanFunction.ONLY_FIRST);
	
	public static final BooleanProperty FULL = BooleanProperty.create("full");
	
	public CrucibleBlock(Properties properties){
		super(properties);
		setDefaultState(stateContainer.getBaseState().with(FULL, false));
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		builder.add(FULL);
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	
	public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos){
		return INSIDE;
	}
	
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type){
		return false;
	}
	
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult rayTrace){
		ItemStack itemstack = player.getHeldItem(handIn);
		if(itemstack.isEmpty()){
			if(player.isCrouching()){
				if(state.get(FULL)){
					if(!world.isRemote){
						world.setBlockState(pos, state.with(FULL, false), 2);
						world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
					((CrucibleTileEntity)world.getTileEntity(pos)).empty();
				}
				return ActionResultType.SUCCESS;
			}
			return ActionResultType.PASS;
		}else{
			Item item = itemstack.getItem();
			if(item == Items.WATER_BUCKET){
				if(!state.get(FULL) && !world.isRemote){
					if(!player.abilities.isCreativeMode)
						player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
					player.addStat(Stats.FILL_CAULDRON);
					world.setBlockState(pos, state.with(FULL, true), 2);
					world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				return ActionResultType.SUCCESS;
			}else if(item == Items.BUCKET){
				if(state.get(FULL) && !world.isRemote && ((CrucibleTileEntity)world.getTileEntity(pos)).getAspectStackMap().isEmpty()){
					if(!player.abilities.isCreativeMode){
						itemstack.shrink(1);
						if(itemstack.isEmpty())
							player.setHeldItem(handIn, new ItemStack(Items.WATER_BUCKET));
						else if(!player.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET)))
							player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
					}
					player.addStat(Stats.USE_CAULDRON);
					world.setBlockState(pos, state.with(FULL, false), 2);
					world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				return ActionResultType.SUCCESS;
			}
		}
		return super.onBlockActivated(state, world, pos, player, handIn, rayTrace);
	}
	
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving){
		TileEntity entity = world.getTileEntity(pos);
		if(entity instanceof CrucibleTileEntity)
			((CrucibleTileEntity)entity).empty();
		super.onReplaced(state, world, pos, newState, isMoving);
	}
	
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand){
		// if boiling, show bubbles
		if(((CrucibleTileEntity)world.getTileEntity(pos)).isBoiling()){
			// we boiling
			double x = pos.getX();
			double y = pos.getY();
			double z = pos.getZ();
			// bubble column particles remove themselves quickly, we might want our own thing
			world.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x + .125 + rand.nextFloat() * .75f, y + .8125f, z + .125 + rand.nextFloat() * .75f, 0.0D, 0.04D, 0.0D);
			world.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x + .125 + rand.nextFloat() * .75f, y + .8125f, z + .125 + rand.nextFloat() * .75f, 0.0D, 0.04D, 0.0D);
		}
	}
	
	public void fillWithRain(World world, BlockPos pos) {
		if(world.rand.nextInt(20) == 1){
			float f = world.getBiome(pos).getTemperature(pos);
			if(!(f < 0.15F)){
				BlockState blockstate = world.getBlockState(pos);
				if(!blockstate.get(FULL))
					world.setBlockState(pos, blockstate.with(FULL, true), 2);
			}
		}
	}
	
	@Override
	public boolean hasTileEntity(BlockState state){
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return new CrucibleTileEntity();
	}
}