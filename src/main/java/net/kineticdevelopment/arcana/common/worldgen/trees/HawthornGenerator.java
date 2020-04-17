package net.kineticdevelopment.arcana.common.worldgen.trees;

import net.kineticdevelopment.arcana.common.init.BlockInit;
import net.kineticdevelopment.arcana.common.worldgen.GenerationUtilities;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.Random;

import static net.minecraft.block.BlockLog.LOG_AXIS;

/**
 * @author Mozaran
 *
 * Used to generate hawthorn trees (WIP)
 */
public class HawthornGenerator extends WorldGenAbstractTree {
    private static final IBlockState DEFAULT_TRUNK = BlockInit.HAWTHORN_LOG.getDefaultState();
    private static final IBlockState DEFAULT_TAINTED_TRUNK = BlockInit.TAINTED_HAWTHORN_LOG.getDefaultState();
    private static final IBlockState DEFAULT_UNTAINTED_TRUNK = BlockInit.UNTAINTED_HAWTHORN_LOG.getDefaultState();
    private static final IBlockState DEFAULT_LEAVES = BlockInit.HAWTHORN_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
    private static final IBlockState DEFAULT_TAINTED_LEAVES = BlockInit.TAINTED_HAWTHORN_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
    private static final IBlockState DEFAULT_UNTAINTED_LEAVES = BlockInit.UNTAINTED_HAWTHORN_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);

    private final IBlockState metaWood;
    private final IBlockState metaLeaves;

    private final int minTreeHeight = 4;

    public HawthornGenerator(boolean notify, boolean tainted) {
        this(notify, tainted, false);
    }

    public HawthornGenerator(boolean notify, boolean tainted, boolean untainted) {
        super(notify);
        if(tainted) {
            metaWood = untainted ? DEFAULT_UNTAINTED_TRUNK : DEFAULT_TAINTED_TRUNK;
            metaLeaves = untainted ? DEFAULT_UNTAINTED_LEAVES : DEFAULT_TAINTED_LEAVES;
        } else {
            metaWood = DEFAULT_TRUNK;
            metaLeaves = DEFAULT_LEAVES;
        }
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int seed = rand.nextInt(8);
        int height = rand.nextInt(2) + minTreeHeight;

        BlockPos xBranch = null;
        BlockPos zBranch = null;
        BlockPos xzBranch = null;
        if(seed < 4) {
            if (seed % 4 == 0) {
                // Branch +x +z (-x)(-z)
                xBranch = position.add(2 + rand.nextInt(2), height, 0);
                zBranch = position.add(0, height, 2 + rand.nextInt(2));
                xzBranch = position.add(-2 - rand.nextInt(2), height, -2 - rand.nextInt(2));
            } else if (seed % 4 == 1) {
                // Branch +x -z (-x)z
                xBranch = position.add(2 + rand.nextInt(2), height, 0);
                zBranch = position.add(0, height, -2 - rand.nextInt(2));
                xzBranch = position.add(-2 - rand.nextInt(2), height, 2 + rand.nextInt(2));
            } else if (seed % 4 == 2) {
                // Branch -x +z x(-z)
                xBranch = position.add(-2 - rand.nextInt(2), height, 0);
                zBranch = position.add(0, height, 2 + rand.nextInt(2));
                xzBranch = position.add(2 + rand.nextInt(2), height, -2 - rand.nextInt(2));
            } else {
                // Branch -x -z xz
                xBranch = position.add(-2 - rand.nextInt(2), height, 0);
                zBranch = position.add(0, height, -2 - rand.nextInt(2));
                xzBranch = position.add(2 + rand.nextInt(2), height, 2 + rand.nextInt(2));
            }
        } else {
            int adjSeed = rand.nextInt(12);
            if(adjSeed == 0) {
                xBranch = position.add(2 + rand.nextInt(2), height, 0);
                zBranch = position.add(0, height, 2 + rand.nextInt(2));
            } else if (adjSeed == 1) {
                xBranch = position.add(-2 - rand.nextInt(2), height, 0);
                zBranch = position.add(0, height, 2 + rand.nextInt(2));
            } else if (adjSeed == 2) {
                xBranch = position.add(2 + rand.nextInt(2), height, 0);
                zBranch = position.add(0, height, -2 - rand.nextInt(2));
            } else if (adjSeed == 3) {
                xBranch = position.add(-2 - rand.nextInt(2), height, 0);
                zBranch = position.add(0, height, -2 - rand.nextInt(2));
            } else if (adjSeed == 4) {
                xBranch = position.add(2 + rand.nextInt(2), height, 0);
                xzBranch = position.add(0, height, 2 + rand.nextInt(2));
            } else if (adjSeed == 5) {
                xBranch = position.add(-2 - rand.nextInt(2), height, 0);
                xzBranch = position.add(0, height, 2 + rand.nextInt(2));
            } else if (adjSeed == 6) {
                xBranch = position.add(2 + rand.nextInt(2), height, 0);
                xzBranch = position.add(0, height, -2 - rand.nextInt(2));
            } else if (adjSeed == 7) {
                xBranch = position.add(-2 - rand.nextInt(2), height, 0);
                xzBranch = position.add(0, height, -2 - rand.nextInt(2));
            } else if (adjSeed == 8) {
                zBranch = position.add(2 + rand.nextInt(2), height, 0);
                xzBranch = position.add(0, height, 2 + rand.nextInt(2));
            } else if (adjSeed == 9) {
                zBranch = position.add(-2 - rand.nextInt(2), height, 0);
                xzBranch = position.add(0, height, 2 + rand.nextInt(2));
            } else if (adjSeed == 10) {
                zBranch = position.add(2 + rand.nextInt(2), height, 0);
                xzBranch = position.add(0, height, -2 - rand.nextInt(2));
            } else if (adjSeed == 11) {
                zBranch = position.add(-2 - rand.nextInt(2), height, 0);
                xzBranch = position.add(0, height, -2 - rand.nextInt(2));
            }
        }

        ArrayList<BlockPos> trunkBlockList = new ArrayList<>();
        ArrayList<BlockPos> leafBlockList = new ArrayList<>();
        for(int y = 0; y <= height; ++y) {
            trunkBlockList.add(position.add(0, y, 0));
        }
        leafBlockList.addAll(GenerationUtilities.GenerateCircle(position.add(0, height, 0), 5, GenerationUtilities.GenType.FULL));
        leafBlockList.addAll(GenerationUtilities.GenerateCircle(position.add(0,height + 1,0), 3, GenerationUtilities.GenType.FULL));

        if(xBranch != null) {
            trunkBlockList.addAll(GenerationUtilities.GenerateTrunk(position.add(0, 2 + rand.nextInt(2), 0), xBranch, 1));
            leafBlockList.addAll(GenerationUtilities.GenerateCircle(xBranch, 5, GenerationUtilities.GenType.FULL));
            leafBlockList.addAll(GenerationUtilities.GenerateCircle(xBranch.add(0,1,0), 3, GenerationUtilities.GenType.FULL));
        }
        if(zBranch != null) {
            trunkBlockList.addAll(GenerationUtilities.GenerateTrunk(position.add(0, 2 + rand.nextInt(2), 0), zBranch, 1));
            leafBlockList.addAll(GenerationUtilities.GenerateCircle(zBranch, 5, GenerationUtilities.GenType.FULL));
            leafBlockList.addAll(GenerationUtilities.GenerateCircle(zBranch.add(0,1,0), 3, GenerationUtilities.GenType.FULL));
        }
        if(xzBranch != null) {
            trunkBlockList.addAll(GenerationUtilities.GenerateTrunk(position.add(0, 2 + rand.nextInt(2), 0), xzBranch, 1));
            leafBlockList.addAll(GenerationUtilities.GenerateCircle(xzBranch, 5, GenerationUtilities.GenType.FULL));
            leafBlockList.addAll(GenerationUtilities.GenerateCircle(xzBranch.add(0, 1, 0), 3, GenerationUtilities.GenType.FULL));
        }

        // Check if tree fits in world
        if (position.getY() >= 1 && position.getY() + height + 1 <= worldIn.getHeight())
        {
            for(BlockPos pos : leafBlockList) {
                if (!this.isReplaceable(worldIn, pos)) {
                    return false;
                }
            }
            for(BlockPos pos : trunkBlockList) {
                if (!this.isReplaceable(worldIn, pos)) {
                    return false;
                }
            }
        } else {
            return false;
        }


        IBlockState state = worldIn.getBlockState(position.down());

        if (state.getBlock().canSustainPlant(state, worldIn, position.down(), EnumFacing.UP, (IPlantable) Blocks.SAPLING) && position.getY() < worldIn.getHeight() - height - 1)
        {
            state.getBlock().onPlantGrow(state, worldIn, position.down(), position);
            for(BlockPos pos : leafBlockList) {
                setBlockAndNotifyAdequately(worldIn, pos, metaLeaves);
            }
            for(BlockPos pos : trunkBlockList) {
                setBlockAndNotifyAdequately(worldIn, pos, metaWood);
            }

            setBlockAndNotifyAdequately(worldIn, position.add(1,0,0), metaWood.withProperty(LOG_AXIS, BlockLog.EnumAxis.X));
            setBlockAndNotifyAdequately(worldIn, position.add(-1,0,0), metaWood.withProperty(LOG_AXIS, BlockLog.EnumAxis.X));
            setBlockAndNotifyAdequately(worldIn, position.add(0,0,1), metaWood.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z));
            setBlockAndNotifyAdequately(worldIn, position.add(0,0,-1), metaWood.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z));

            return true;
        }
        else
        {
            return false;
        }

    }
}
