package net.arcanamod.fluids;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.entities.TaintedGooWrapper;
import net.arcanamod.systems.taint.Taint;
import net.arcanamod.effects.ArcanaEffects;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TaintFluid extends FlowingFluidBlock {
	public TaintFluid(Supplier<? extends FlowingFluid> supplier, Properties properties) {
		super(supplier, properties);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.randomTick(state, world, pos, random);
		Taint.tickTaintedBlock(state, world, pos, random);
	}

	@Override
	public boolean ticksRandomly(BlockState state) {
		return true;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if(entity instanceof LivingEntity) {
			((TaintedGooWrapper) entity).setGooTicks(((TaintedGooWrapper) entity).getGooTicks() + 1);
			if (((TaintedGooWrapper) entity).getGooTicks() > 6) {
				((LivingEntity) entity).addPotionEffect(new EffectInstance(ArcanaEffects.TAINTED.get(), 5 * 20));
			}
		}
	}
}