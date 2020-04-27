package net.kineticdevelopment.arcana.common.worldgen.trees;

import net.kineticdevelopment.arcana.common.init.BlockInit;
import net.kineticdevelopment.arcana.common.worldgen.GenerationUtilities;
import net.minecraft.block.Block;
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
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static net.minecraft.block.BlockLog.LOG_AXIS;

/**
 * @author Mozaran
 *
 * Used to generate greatwood trees
 */
public class GreatwoodGenerator extends WorldGenAbstractTree {
    private static final IBlockState DEFAULT_TRUNK = BlockInit.GREATWOOD_LOG.getDefaultState();
    private static final IBlockState DEFAULT_TAINTED_TRUNK = BlockInit.TAINTED_GREATWOOD_LOG.getDefaultState();
    private static final IBlockState DEFAULT_UNTAINTED_TRUNK = BlockInit.UNTAINTED_GREATWOOD_LOG.getDefaultState();
    private static final IBlockState DEFAULT_LEAVES = BlockInit.GREATWOOD_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
    private static final IBlockState DEFAULT_TAINTED_LEAVES = BlockInit.TAINTED_GREATWOOD_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
    private static final IBlockState DEFAULT_UNTAINTED_LEAVES = BlockInit.UNTAINTED_GREATWOOD_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);

    private final IBlockState metaWood;
    private final IBlockState metaLeaves;

    private final int minTreeHeight = 23;

    private ArrayList<BlockPos> xTrunkBlockList = new ArrayList<>();
    private ArrayList<BlockPos> yTrunkBlockList = new ArrayList<>();
    private ArrayList<BlockPos> zTrunkBlockList = new ArrayList<>();
    private ArrayList<BlockPos> leafBlockList = new ArrayList<>();
    private Set<BlockPos> takenOriginPos = new HashSet<>();
    private Set<BlockPos> takenEndPos = new HashSet<>();

    public GreatwoodGenerator(boolean notify, boolean tainted) {
        this(notify, tainted, false);
    }

    public GreatwoodGenerator(boolean notify, boolean tainted, boolean untainted) {
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
        int height = rand.nextInt(3) + minTreeHeight;

        // Gen Trunk
        for(int i = 0; i < height; ++i) {
            yTrunkBlockList.add(position.add(0,i,0));
            yTrunkBlockList.add(position.add(1,i,0));
            yTrunkBlockList.add(position.add(0,i,1));
            yTrunkBlockList.add(position.add(1,i,1));
        }

        // Gen Top Branch
        int xOffset = (seed % 4 == 0 || seed % 4 == 1) ? 0 : 1;
        int zOffset = (seed % 4 == 0 || seed % 4 == 2) ? 0 : 1;
        genBranch(position.add(xOffset, height, zOffset), position.add(xOffset, height + 1, zOffset), BlockLog.EnumAxis.Y);

        // Gen 4-5 branches per side on lower half 5 blocks long
        int lowerY = 6;
        int upperY = height / 2;
        int numBranches = 5 + ((rand.nextInt(2) % 2 == 0) ? 0 : 2);
        int branchLength = 2 + rand.nextInt(2);
        genTreeSection(lowerY, upperY, numBranches, branchLength, position, rand);
        lowerY = upperY + 1;
        upperY = height;
        numBranches = 7 + ((rand.nextInt(2) % 2 == 0) ? 0 : 2);
        branchLength = 2 + rand.nextInt(3);
        genTreeSection(lowerY, upperY, numBranches, branchLength, position, rand);

        for(BlockPos pos : takenOriginPos) {
            genLeafNode(pos);
        }

        // Check if tree fits in world
        if (position.getY() >= 1 && position.getY() + height + 1 <= worldIn.getHeight())
        {
            for(BlockPos pos : leafBlockList) {
                if (!this.isReplaceable(worldIn, pos)) {
                    return false;
                }
            }
            for(BlockPos pos : xTrunkBlockList) {
                if (!this.isReplaceable(worldIn, pos)) {
                    return false;
                }
            }
            for(BlockPos pos : yTrunkBlockList) {
                if (!this.isReplaceable(worldIn, pos)) {
                    return false;
                }
            }
            for(BlockPos pos : zTrunkBlockList) {
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
            for(BlockPos pos : yTrunkBlockList) {
                setBlockAndNotifyAdequately(worldIn, pos, metaWood);
            }
            for(BlockPos pos : xTrunkBlockList) {
                setBlockAndNotifyAdequately(worldIn, pos, metaWood.withProperty(LOG_AXIS, BlockLog.EnumAxis.X));
            }
            for(BlockPos pos : zTrunkBlockList) {
                setBlockAndNotifyAdequately(worldIn, pos, metaWood.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z));
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    private void genTreeSection(int lowerY, int upperY, int numBranches, int branchLength, BlockPos position, Random rand) {
        for(int i = 0; i < numBranches; ++i) {
            // Strait Branch
            if (i == 0) {
                int adjBranchLength = 2;
                int sideStart = rand.nextInt(2);
                int yStart = ThreadLocalRandom.current().nextInt(lowerY, upperY);
                int yEnd = yStart + 1 + rand.nextInt(2);

                // Mark one branch and reflect it across axises
                BlockPos start = position.add(-1, yStart, sideStart);
                BlockPos end = position.add(-1 - adjBranchLength, yEnd, sideStart);
                takenOriginPos.add(start);
                takenEndPos.add(end);
                genBranch(start, end, BlockLog.EnumAxis.X);
                genBranch(position.add(2, yStart, sideStart), position.add(2 + adjBranchLength, yEnd, sideStart), BlockLog.EnumAxis.X);
                genBranch(position.add(sideStart, yStart, -1), position.add(sideStart, yEnd, -1 - adjBranchLength), BlockLog.EnumAxis.Z);
                genBranch(position.add(sideStart, yStart, 2), position.add(sideStart, yEnd, 2 + adjBranchLength), BlockLog.EnumAxis.Z);
            } else {
                // Diagonal branches
                boolean validStart = false;
                boolean validEnd = false;
                int yStart = -1;
                int yEnd = -1;
                int sideStart = -1;
                int sideEnd = -1;
                while(!validStart) {
                    sideStart = rand.nextInt(2);
                    yStart = ThreadLocalRandom.current().nextInt(lowerY, upperY);

                    BlockPos tempStart = position.add(-1, yStart, sideStart);
                    if(!takenOriginPos.contains(tempStart)) {
                        takenOriginPos.add(tempStart);
                        validStart = true;
                    }
                }
                while(!validEnd) {
                    sideEnd = (i % 2 == 0) ? ThreadLocalRandom.current().nextInt(-branchLength, 0) : ThreadLocalRandom.current().nextInt(0, branchLength);
                    yEnd = (rand.nextInt(4) % 2 == 0) ? yStart + rand.nextInt(4) : yStart - rand.nextInt(4);

                    if(yEnd < 6) yEnd = 6;

                    BlockPos tempEnd = position.add(-1 - branchLength, yEnd, sideEnd);
                    if(!takenEndPos.contains(tempEnd)) {
                        takenEndPos.add(tempEnd);
                        validEnd = true;
                    }
                }
                // -x
                genBranch(position.add(-1, yStart, sideStart), position.add(-1 - branchLength, yEnd, sideEnd), BlockLog.EnumAxis.X);
                // +x
                genBranch(position.add(2, yStart, sideStart), position.add(2 + branchLength, yEnd, sideEnd), BlockLog.EnumAxis.X);
                // -z
                genBranch(position.add(sideStart, yStart, -1), position.add(sideEnd, yEnd, -1 - branchLength), BlockLog.EnumAxis.Z);
                // +z
                genBranch(position.add(sideStart, yStart, 2), position.add(sideEnd, yEnd, 2 + branchLength), BlockLog.EnumAxis.Z);
            }
        }
    }

    private void genBranch(BlockPos origin, BlockPos end, BlockLog.EnumAxis logAxis) {
        if(logAxis == BlockLog.EnumAxis.X) {
            xTrunkBlockList.addAll(GenerationUtilities.GenerateTrunk(origin, end, 1));
        } else if(logAxis == BlockLog.EnumAxis.Y) {
            yTrunkBlockList.addAll(GenerationUtilities.GenerateTrunk(origin, end, 1));
        } else {
            zTrunkBlockList.addAll(GenerationUtilities.GenerateTrunk(origin, end, 1));
        }
        genLeafNode(end.add(0,-1,0));
    }

    private void genLeafNode(BlockPos origin) {
        leafBlockList.addAll(GenerationUtilities.GenerateCircle(origin, 1, GenerationUtilities.GenType.FULL));
        leafBlockList.addAll(GenerationUtilities.GenerateCircle(origin.add(0,1,0), 3, GenerationUtilities.GenType.FULL));
        leafBlockList.add(origin.add(0,2, 0));
        leafBlockList.add(origin.add(1,2,0));
        leafBlockList.add(origin.add(-1,2,0));
        leafBlockList.add(origin.add(0,2,1));
        leafBlockList.add(origin.add(0,2,-1));
        leafBlockList.add(origin.add(0,3,0));
    }
}
