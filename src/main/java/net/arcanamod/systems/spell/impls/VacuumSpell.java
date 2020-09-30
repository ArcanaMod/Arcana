package net.arcanamod.systems.spell.impls;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.tiles.VacuumTileEntity;
import net.arcanamod.systems.spell.*;
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

public class VacuumSpell extends Spell {

    private SpellData data;
    private int distance = 10;

    public boolean isBuilt = false;

    /**
     * Defines all variables. DON'T USE THAT IN REGISTRY!!!
     *
     * @param data     spell data
     * @param compound extra data
     * @return this but with defined variables.
     */
    @Override
    public ISpell build(SpellData data, CompoundNBT compound) {
        this.data = data;
        if (compound.contains("distance"))
            this.distance = compound.getInt("distance");
        isBuilt = true;
        return this;
    }

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

    /**
     * Returns spell modifiers and returns Cast Aspects that are neat modifiers and are used in combos.
     *
     * @return Mod 1, Mod 2, Sin Mod, Cast, Cast+
     */
    @Override
    public SpellData getSpellData() {
        return data;
    }

    /**
     * Cost of spell in AspectStacks.
     *
     * @return returns cost of spell.
     */
    @Override
    public SpellCosts getSpellCosts() {
        return new SpellCosts(1,0,0,0,0,1,0);
    }

    /**
     * How spell is complex to use / create
     *
     * @return returns spell complexity.
     */
    @Override
    public int getComplexity() {
        if (!isBuilt) return -2;
        return  3
                + SpellValues.getOrDefault(data.firstModifier,0)*2
                + SpellValues.getOrDefault(data.secondModifier,0)*4
                + SpellValues.getOrDefault(data.sinModifier,0)*2
                + SpellValues.getOrDefault(data.primaryCast.getSecond(),0)*3
                + SpellValues.getOrDefault(data.plusCast.getSecond(),0)*3;
    }

    @Override
    public int getSpellDuration() {
        return 1;
    }

    @Override
    public ActionResultType useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget) {
        try {
            if(caster.world.isRemote) return ActionResultType.SUCCESS;
            BlockState blockToReplace = world.getBlockState(blockTarget);
            if (blockToReplace.getBlock() != Blocks.AIR && blockToReplace.getBlock() != Blocks.CAVE_AIR) {
                BlockState vaccumBlock = ArcanaBlocks.VACUUM_BLOCK.get().getDefaultState();
                world.setBlockState(blockTarget, vaccumBlock);
                ((VacuumTileEntity)world.getTileEntity(blockTarget)).setDuration(getDuration());
                ((VacuumTileEntity)world.getTileEntity(blockTarget)).setOriginBlock(blockToReplace);
            }
        } catch (SpellNotBuiltError spellNotBuiltError) {
            spellNotBuiltError.throwException(caster);
            return ActionResultType.FAIL;
        }
        return ActionResultType.SUCCESS;
    }

    /**
     * Gets Vacuum blocks duration from modifiers
     * @return Vacuum blocks duration
     */
    protected int getDuration() throws SpellNotBuiltError {
        if (!isBuilt) throw new SpellNotBuiltError();
        return SpellValues.getOrDefault(data.firstModifier, 40);
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