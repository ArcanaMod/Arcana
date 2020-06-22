package net.arcanamod.fluids;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.function.Supplier;

public class TaintFluid extends FlowingFluidBlock
{
	protected TaintFluid(FlowingFluid fluidIn, Properties builder)
	{
		super(fluidIn, builder);
	}

	public TaintFluid(Supplier<? extends FlowingFluid> supplier, Properties properties)
	{
		super(supplier, properties);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		//Add taint things on random Tick
		super.randomTick(state, worldIn, pos, random);
	}

	@Override
	public boolean ticksRandomly(BlockState state)
	{
		return true;
	}

	@Override
	public MaterialColor getMaterialColor(BlockState p_180659_1_, IBlockReader p_180659_2_, BlockPos p_180659_3_)
	{
		return MaterialColor.RED;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return false;
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		//Player touches fluid
	}
}