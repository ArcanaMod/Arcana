package net.arcanamod.systems.spell.casts.impl;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.tiles.VacuumTileEntity;
import net.arcanamod.systems.spell.*;
import net.arcanamod.systems.spell.casts.Cast;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class VacuumCast extends Cast {

    @Override
    public ResourceLocation getId() {
        return ArcanaVariables.arcLoc("vacuum");
    }
    
    /**
     * Core aspect in spell.
     *
     * @return returns core aspect.
     */
    @Override
    public Aspect getSpellAspect() {
        return Aspects.VACUUM;
    }

    @Override
    public int getSpellDuration() {
        return 1;
    }

    @Override
    public ActionResultType useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget) {
        if(caster.world.isRemote) return ActionResultType.SUCCESS;
        BlockPos.getAllInBoxMutable(blockTarget.add(
                -Math.floor(getWidth(caster)),
                -Math.floor(getWidth(caster)),
                -Math.floor(getWidth(caster))),
                blockTarget.offset(caster.getHorizontalFacing(),getDistance(caster)).add(
                        Math.floor(getWidth(caster)),
                        Math.floor(getWidth(caster)),
                        Math.floor(getWidth(caster)))).forEach(blockPos -> {
            Block blockToReplace = world.getBlockState(blockTarget).getBlock();
            if (blockToReplace != Blocks.AIR && blockToReplace != Blocks.CAVE_AIR) {
                BlockState vaccumBlock = ArcanaBlocks.VACUUM_BLOCK.get().getDefaultState();
                world.setBlockState(blockTarget, vaccumBlock);
                ((VacuumTileEntity)world.getTileEntity(blockTarget)).setDuration(getDuration(caster));
                ((VacuumTileEntity)world.getTileEntity(blockTarget)).setOriginBlock(blockToReplace.getDefaultState());
            }
        });
        return ActionResultType.SUCCESS;
    }

    protected int getWidth(PlayerEntity playerEntity) {
        return SpellValues.getOrDefault(AspectUtils.deserializeAspect(data,"sinModifier"), 1);
    }

    protected int getDistance(PlayerEntity playerEntity) {
        return SpellValues.getOrDefault(AspectUtils.deserializeAspect(data,"secondModifier"), 16);
    }

    /**
     * Gets Vacuum blocks duration from modifiers
     * @return Vacuum blocks duration
     */
    protected int getDuration(PlayerEntity playerEntity) {
        return (1+SpellValues.getOrDefault(AspectUtils.deserializeAspect(data,"firstModifier"), 0))*100;
    }

    @Override
    public ActionResultType useOnPlayer(PlayerEntity playerTarget) {
        playerTarget.sendStatusMessage(new TranslationTextComponent("status.arcana.invalid_spell"), true);
        return ActionResultType.FAIL;
    }

    @Override
    public ActionResultType useOnEntity(PlayerEntity caster, Entity entityTarget) {
        caster.sendStatusMessage(new TranslationTextComponent("status.arcana.invalid_spell"), true);
        return ActionResultType.FAIL;
    }
}