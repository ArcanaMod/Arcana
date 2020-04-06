package net.kineticdevelopment.arcana.common.worldgen.trees;

import net.kineticdevelopment.arcana.common.init.BlockInit;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.awt.*;
import java.util.Random;

public class WillowGenerator extends WorldGenAbstractTree {
    private static final IBlockState DEFAULT_TRUNK = BlockInit.WILLOW_LOG.getDefaultState();
    private static final IBlockState DEFAULT_TAINTED_TRUNK = BlockInit.TAINTED_WILLOW_LOG.getDefaultState();
    private static final IBlockState DEFAULT_UNTAINTED_TRUNK = BlockInit.UNTAINTED_WILLOW_LOG.getDefaultState();
    private static final IBlockState DEFAULT_LEAVES = BlockInit.WILLOW_LEAVES.getDefaultState();
    private static final IBlockState DEFAULT_TAINTED_LEAVES = BlockInit.TAINTED_WILLOW_LEAVES.getDefaultState();
    private static final IBlockState DEFAULT_UNTAINTED_LEAVES = BlockInit.UNTAINTED_WILLOW_LEAVES.getDefaultState();

    private final IBlockState metaWood;
    private final IBlockState metaLeaves;

    public WillowGenerator(boolean notify, boolean tainted) {
        this(notify, tainted, false);
    }

    public WillowGenerator(boolean notify, boolean tainted, boolean untainted) {
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
        int minTreeHeight = 5;
        int treeHeight = minTreeHeight + rand.nextInt(2);
        int treeRadius = 3;
        boolean flag = true;

        // Check Placement Valid
        if (position.getY() >= 1 && position.getY() + treeHeight + 1 <= worldIn.getHeight()) {
            // Check trunk placement
            for (int y = position.getY(); y <= position.getY() + 1 + treeHeight; ++y)
            {
                int k = 1;

                if (y == position.getY())
                {
                    k = 0;
                }

                if (y >= position.getY() + 1 + treeHeight)
                {
                    k = treeRadius;
                }

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                // Check around trunk
                for (int x = position.getX() - k; x <= position.getX() + k && flag; ++x)
                {
                    for (int z = position.getZ() - k; z <= position.getZ() + k && flag; ++z)
                    {
                        if (y >= 0 && y < worldIn.getHeight())
                        {
                            if (!this.isReplaceable(worldIn,blockpos$mutableblockpos.setPos(x, y, z)))
                            {
                                flag = false;
                            }
                        }
                        else
                        {
                            flag = false;
                        }
                    }
                }
            }
            if (!flag)
            {
                // Placement Invalid
                return false;
            }
            else
            {
                // Placement Valid
                IBlockState state = worldIn.getBlockState(position.down());

                if (state.getBlock().canSustainPlant(state, worldIn, position.down(), EnumFacing.UP,
                        (BlockSapling) Blocks.SAPLING) && position.getY() < worldIn.getHeight() - treeHeight - 1)
                {
                    state.getBlock().onPlantGrow(state, worldIn, position.down(), position);

                    // Generate Leaves
                    int leavesWidth = 0;
                    for (int y = position.getY() + treeHeight; y > position.getY() + treeHeight - 3; --y)
                    {
                        leavesWidth += 1;

                        for (int x = position.getX() - leavesWidth; x <= position.getX() + leavesWidth; ++x)
                        {
                            int xRel = x - position.getX();

                            for (int z = position.getZ() - leavesWidth; z <= position.getZ() + leavesWidth; ++z)
                            {
                                int zRel = z - position.getZ();
                                if(!((Math.abs(xRel) == 3 && Math.abs(zRel) == 2) || (Math.abs(xRel) == 2 && Math.abs(zRel) == 3))) {
                                    if (Math.abs(xRel) != Math.abs(zRel) || Math.abs(xRel) != leavesWidth || Math.abs(zRel) != leavesWidth) {
                                        BlockPos blockpos = new BlockPos(x, y, z);
                                        state = worldIn.getBlockState(blockpos);

                                        if (state.getBlock().isAir(state, worldIn, blockpos)
                                                || state.getBlock().isLeaves(state, worldIn, blockpos)
                                                || state.getMaterial() == Material.VINE) {
                                            this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Generate Leaves ring
                    for (int x = position.getX() - leavesWidth; x <= position.getX() + leavesWidth; ++x)
                    {
                        int y = position.getY() + treeHeight - 3;
                        int xRel = x - position.getX();

                        for (int z = position.getZ() - leavesWidth; z <= position.getZ() + leavesWidth; ++z)
                        {
                            int zRel = z - position.getZ();
                            if(!((Math.abs(xRel) == 3 && Math.abs(zRel) == 2) || (Math.abs(xRel) == 2 && Math.abs(zRel) == 3))) {
                                if (Math.abs(xRel) != Math.abs(zRel) || Math.abs(xRel) != leavesWidth || Math.abs(zRel) != leavesWidth) {
                                    if (Math.abs(xRel) == 3 || Math.abs(zRel) == 3 || (Math.abs(xRel) == 2 && Math.abs(zRel) == 2)) {
                                        BlockPos blockpos = new BlockPos(x, y, z);
                                        state = worldIn.getBlockState(blockpos);

                                        if (state.getBlock().isAir(state, worldIn, blockpos)
                                                || state.getBlock().isLeaves(state, worldIn, blockpos)
                                                || state.getMaterial() == Material.VINE) {
                                            this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Gen Droopy Leaves
                    GenDroops(true, treeRadius, position.getY() + treeHeight - 4,  position, worldIn, rand);
                    GenDroops(false, treeRadius, position.getY() + treeHeight - 4,  position, worldIn, rand);

                    // Generate trunk
                    for (int y = 0; y < treeHeight; ++y)
                    {
                        BlockPos upN = position.up(y);
                        state = worldIn.getBlockState(upN);

                        if (state.getBlock().isAir(state, worldIn, upN)
                                || state.getBlock().isLeaves(state, worldIn, upN)
                                || state.getMaterial() == Material.VINE)
                        {
                            this.setBlockAndNotifyAdequately(worldIn, position.up(y), this.metaWood);
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }


    void GenDroops(boolean xAxis, int treeRadius, int y, BlockPos position, World worldIn, Random rand) {

        boolean leftLong = rand.nextInt(2) == 0;
        int xRel = xAxis ? treeRadius : 1;
        int zRel = xAxis ? 1 : treeRadius;
        int x = position.getX() - xRel;
        int z = position.getZ() - zRel;

        if(xAxis) {
            if(leftLong) zRel *= -1;
            z = position.getZ() - zRel;
            for (int i = 0; i < 2; ++i) {
                BlockPos blockpos = new BlockPos(x, y - i, z);
                this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
            }
            zRel *= -1;
            z = position.getZ() - zRel;
            BlockPos blockpos = new BlockPos(x, y, z);
            this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
            xRel *= -1;
            x = position.getX() - xRel;
            for (int i = 0; i < 2; ++i) {
                blockpos = new BlockPos(x, y - i, z);
                this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
            }
            zRel *= -1;
            z = position.getZ() - zRel;
            blockpos = new BlockPos(x, y, z);
            this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
        } else {
            if(leftLong) xRel *= -1;
            x = position.getX() - xRel;
            for (int i = 0; i < 2; ++i) {
                BlockPos blockpos = new BlockPos(x, y - i, z);
                this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
            }
            xRel *= -1;
            x = position.getX() - xRel;
            BlockPos blockpos = new BlockPos(x, y, z);
            this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
            zRel *= -1;
            z = position.getZ() - zRel;
            for (int i = 0; i < 2; ++i) {
                blockpos = new BlockPos(x, y - i, z);
                this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
            }
            xRel *= -1;
            x = position.getX() - xRel;
            blockpos = new BlockPos(x, y, z);
            this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
        }
    }
}
