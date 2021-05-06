package net.arcanamod.blocks.tainted;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.TaintedBlock;
import net.arcanamod.systems.taint.Taint;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.IShearable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TaintedPlantBlock extends TaintedBlock implements IPlantable, IGrowable, IForgeShearable{
	
	public TaintedPlantBlock(Block block){
		super(block);
	}
	
	// Growable methods
	
	@Nullable
	private IGrowable getGrowable(){
		return parentBlock instanceof IGrowable ? (IGrowable)parentBlock : null;
	}
	
	public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean isClient){
		return getGrowable() != null && getGrowable().canGrow(world, pos, state, isClient);
	}
	
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, BlockState state){
		return getGrowable() != null && getGrowable().canUseBonemeal(world, rand, pos, state);
	}
	
	public void grow(ServerWorld world, Random rand, BlockPos pos, BlockState state){
		if(getGrowable() != null)
			getGrowable().grow(world, rand, pos, state);
	}
	
	// Plantable methods
	
	@Nullable
	private IPlantable getPlantable(){
		return parentBlock instanceof IPlantable ? (IPlantable)parentBlock : null;
	}
	
	public PlantType getPlantType(IBlockReader world, BlockPos pos){
		return getPlantable() != null ? getPlantable().getPlantType(world, pos) : PlantType.PLAINS;
	}
	
	public BlockState getPlant(IBlockReader world, BlockPos pos){
		return getPlantable() != null ? switchBlock(getPlantable().getPlant(world, pos), this) : getDefaultState();
	}
	
	// Shearable methods
	
	@Nullable
	private IShearable getShearable(){
		return parentBlock instanceof IShearable ? (IShearable)parentBlock : null;
	}
	
	public boolean isShearable(@Nonnull ItemStack item, World world, BlockPos pos){
		return getShearable() != null && getShearable().isShearable();
	}
	
	// Fix BushBlock
	// TODO: AT instead of reflection
	// func_200014_a_(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;)Z
	private static final Method isValidGround = ObfuscationReflectionHelper.findMethod(BushBlock.class, "func_200014_a_", BlockState.class, IBlockReader.class, BlockPos.class);
	
	private static boolean invokeIsValidGround(BushBlock block, BlockState state, IBlockReader reader, BlockPos pos){
		isValidGround.setAccessible(true);
		boolean ret = false;
		try{
			ret = (boolean)isValidGround.invoke(block, state, reader, pos);
		}catch(IllegalAccessException | InvocationTargetException e){
			e.printStackTrace();
		}
		isValidGround.setAccessible(false);
		return ret;
	}
	
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos){
		BlockState bushBaseState = world.getBlockState(pos.down());
		return super.isValidPosition(state, world, pos)
				|| (parentBlock instanceof BushBlock && Taint.getPureOfBlock(bushBaseState.getBlock()) != null && invokeIsValidGround((BushBlock)parentBlock, switchBlock(bushBaseState, Taint.getPureOfBlock(bushBaseState.getBlock())), world, pos))
				|| (parentBlock instanceof BushBlock && Taint.getLivingOfBlock(bushBaseState.getBlock()) != null && invokeIsValidGround((BushBlock)parentBlock, switchBlock(bushBaseState, Taint.getLivingOfBlock(bushBaseState.getBlock())), world, pos));
	}
}