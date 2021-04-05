package net.arcanamod.systems.spell.casts.impl;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.util.NotImplementedException;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.systems.spell.casts.Cast;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.arcanamod.aspects.Aspects.LIGHT;

public class LightCast extends Cast {
	@Override
	public ResourceLocation getId() {
		return ArcanaVariables.arcLoc("light");
	}

	@Override
	public ActionResultType useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget) {
		if (world.getBlockState(blockTarget.up()).getBlock() == ArcanaBlocks.LIGHT_BLOCK.get()){
			return ActionResultType.SUCCESS;
		}
		if (world.getBlockState(blockTarget.up()).getBlock().isAir(world.getBlockState(blockTarget.up()),world,blockTarget.up())){
			world.setBlockState(blockTarget.up(), ArcanaBlocks.LIGHT_BLOCK.get().getDefaultState());
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}

	@Override
	public ActionResultType useOnPlayer(PlayerEntity playerTarget) {
		return placeLight(playerTarget);
	}

	@Override
	public ActionResultType useOnEntity(PlayerEntity caster, Entity entityTarget) {
		return placeLight(entityTarget);
	}

	public ActionResultType placeLight(Entity entityTarget){
		if (entityTarget.world.getBlockState(entityTarget.getPosition().up()).getBlock() == ArcanaBlocks.LIGHT_BLOCK.get()){
			return ActionResultType.SUCCESS;
		}
		if (entityTarget.world.getBlockState(entityTarget.getPosition().up()).getBlock().isAir(entityTarget.world.getBlockState(entityTarget.getPosition().up()),entityTarget.world,entityTarget.getPosition().up())){
			entityTarget.world.setBlockState(entityTarget.getPosition().up(), ArcanaBlocks.LIGHT_BLOCK.get().getDefaultState());
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}

	@Override
	public Aspect getSpellAspect() {
		return LIGHT;
	}

	@Override
	public int getSpellDuration() {
		return 1;
	}
}