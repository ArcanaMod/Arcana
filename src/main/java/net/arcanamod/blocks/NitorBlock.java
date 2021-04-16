package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
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
	public static final IntegerProperty COLOUR = IntegerProperty.create("colour", 0, 15);
	
	public NitorBlock(Properties properties){
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(COLOUR, 1));
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

	private BasicParticleType getColour(BlockState state) {
		BasicParticleType colour = ParticleTypes.CRIT;
		switch(state.get(COLOUR)) {
			case 0:
				colour = ParticleTypes.SPIT;
				break;
			case 1:
				colour = ParticleTypes.FLAME;
				break;
			case 2:
				colour = ParticleTypes.PORTAL;
				break;
			case 3:
				colour = ParticleTypes.BUBBLE;
				break;
			case 4:
				colour = ParticleTypes.LANDING_HONEY;
				break;
			case 5:
				colour = ParticleTypes.SNEEZE;
				break;
			case 6:
				colour = ParticleTypes.HEART;
				break;
			case 7:
				colour = ParticleTypes.SMOKE;
				break;
			case 8:
				colour = ParticleTypes.CAMPFIRE_COSY_SMOKE;
				break;
			case 9:
				colour = ParticleTypes.SPLASH;
				break;
			case 10:
				colour = ParticleTypes.DRAGON_BREATH;
				break;
			case 11:
				colour = ParticleTypes.NAUTILUS;
				break;
			case 12:
				colour = ParticleTypes.MYCELIUM;
				break;
			case 13:
				colour = ParticleTypes.ITEM_SLIME;
				break;
			case 14:
				colour = ParticleTypes.LAVA;
				break;
			case 15:
				colour = ParticleTypes.SQUID_INK;
				break;
		}
		return colour;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		Item item = player.getHeldItem(handIn).getItem();
		if (player.getHeldItem(handIn).getItem() instanceof DyeItem) {
			worldIn.setBlockState(pos, state.with(COLOUR, ((DyeItem) item).getDyeColor().getId()));
		}
		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(COLOUR);
	}
}