package net.arcanamod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;

public class TaintedBlock extends Block {
    private final Block parentBlock;

    public TaintedBlock(Block blockIn) {
        super(Properties.create(
                blockIn.getDefaultState().getMaterial())
                .hardnessAndResistance(blockIn.getDefaultState().getBlockHardness(null, null))
                .sound(blockIn.getDefaultState().getSoundType()));
        this.parentBlock = blockIn;
    }

    @Override
    public boolean ticksRandomly(BlockState p_149653_1_) {
        return parentBlock.ticksRandomly(p_149653_1_);
    }

    @Override
    public void animateTick(BlockState p_180655_1_, World p_180655_2_, BlockPos p_180655_3_, Random p_180655_4_) {
        parentBlock.animateTick(p_180655_1_, p_180655_2_, p_180655_3_, p_180655_4_);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return parentBlock.propagatesSkylightDown(p_200123_1_, p_200123_2_, p_200123_3_);
    }

    @Override
    public int tickRate(IWorldReader p_149738_1_) {
        return parentBlock.tickRate(p_149738_1_);
    }

    @Override
    public void dropXpOnBlockBreak(World p_180637_1_, BlockPos p_180637_2_, int p_180637_3_) {
        parentBlock.dropXpOnBlockBreak(p_180637_1_, p_180637_2_, p_180637_3_);
    }

    @Override
    public void onExplosionDestroy(World p_180652_1_, BlockPos p_180652_2_, Explosion p_180652_3_) {
        parentBlock.onExplosionDestroy(p_180652_1_, p_180652_2_, p_180652_3_);
    }

    @Override
    public void onEntityWalk(World p_176199_1_, BlockPos p_176199_2_, Entity p_176199_3_) {
        parentBlock.onEntityWalk(p_176199_1_, p_176199_2_, p_176199_3_);
    }

    @Override
    public boolean canSpawnInBlock() {
        return parentBlock.canSpawnInBlock();
    }

    @Override
    public void onFallenUpon(World p_180658_1_, BlockPos p_180658_2_, Entity p_180658_3_, float p_180658_4_) {
        parentBlock.onFallenUpon(p_180658_1_, p_180658_2_, p_180658_3_, p_180658_4_);
    }

    @Override
    public void onLanded(IBlockReader p_176216_1_, Entity p_176216_2_) {
        parentBlock.onLanded(p_176216_1_, p_176216_2_);
    }

    @Override
    public float getSpeedFactor() {
        return parentBlock.getSpeedFactor();
    }

    @Override
    public float getJumpFactor() {
        return parentBlock.getJumpFactor();
    }

    @Override
    public void onProjectileCollision(World p_220066_1_, BlockState p_220066_2_, BlockRayTraceResult p_220066_3_, Entity p_220066_4_) {
        parentBlock.onProjectileCollision(p_220066_1_, p_220066_2_, p_220066_3_, p_220066_4_);
    }

    @Override
    public void fillWithRain(World p_176224_1_, BlockPos p_176224_2_) {
        parentBlock.fillWithRain(p_176224_1_, p_176224_2_);
    }

    @Override
    public OffsetType getOffsetType() {
        return parentBlock.getOffsetType();
    }

    @Override
    public boolean isVariableOpacity() {
        return parentBlock.isVariableOpacity();
    }

    @Override
    public float getSlipperiness(BlockState p_getSlipperiness_1_, IWorldReader p_getSlipperiness_2_, BlockPos p_getSlipperiness_3_, @Nullable Entity p_getSlipperiness_4_) {
        return parentBlock.getSlipperiness(p_getSlipperiness_1_, p_getSlipperiness_2_, p_getSlipperiness_3_, p_getSlipperiness_4_);
    }

    @Nullable
    @Override
    public ToolType getHarvestTool(BlockState p_getHarvestTool_1_) {
        return parentBlock.getHarvestTool(p_getHarvestTool_1_);
    }

    @Override
    public int getHarvestLevel(BlockState p_getHarvestLevel_1_) {
        return parentBlock.getHarvestLevel(p_getHarvestLevel_1_);
    }

    @Override
    public boolean canSustainPlant(BlockState p_canSustainPlant_1_, IBlockReader p_canSustainPlant_2_, BlockPos p_canSustainPlant_3_, Direction p_canSustainPlant_4_, IPlantable p_canSustainPlant_5_) {
        return parentBlock.canSustainPlant(p_canSustainPlant_1_, p_canSustainPlant_2_, p_canSustainPlant_3_, p_canSustainPlant_4_, p_canSustainPlant_5_);
    }
}
