package net.kineticdevelopment.arcana.common.worldgen.trees;

import net.kineticdevelopment.arcana.common.init.BlockInit;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

/**
 * @author Mozaran
 *
 * Used to generate dair trees
 */
public class DairGenerator extends WorldGenAbstractTree {
    private static final IBlockState DEFAULT_TRUNK = BlockInit.DAIR_LOG.getDefaultState();
    private static final IBlockState DEFAULT_TAINTED_TRUNK = BlockInit.TAINTED_DAIR_LOG.getDefaultState();
    private static final IBlockState DEFAULT_UNTAINTED_TRUNK = BlockInit.UNTAINTED_DAIR_LOG.getDefaultState();
    private static final IBlockState DEFAULT_LEAVES = BlockInit.DAIR_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
    private static final IBlockState DEFAULT_TAINTED_LEAVES = BlockInit.TAINTED_DAIR_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
    private static final IBlockState DEFAULT_UNTAINTED_LEAVES = BlockInit.UNTAINTED_DAIR_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);

    private final IBlockState metaWood;
    private final IBlockState metaLeaves;

    public DairGenerator(boolean notify, boolean tainted) {
        this(notify, tainted, false);
    }

    public DairGenerator(boolean notify, boolean tainted, boolean untainted) {
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
        int minTreeHeight = 8;
        int treeHeight = rand.nextInt(3) + minTreeHeight;
        boolean flag = true;

        // Check Placement Valid
        if (position.getY() >= 1 && position.getY() + treeHeight + 1 <= worldIn.getHeight())
        {
            // Check trunk placement
            for (int y = position.getY(); y <= position.getY() + 1 + treeHeight; ++y)
            {
                int k = 1;

                if (y == position.getY())
                {
                    k = 0;
                }

                if (y >= position.getY() + 1 + treeHeight - 2)
                {
                    k = 2;
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

                    // Generate leaves
                    for (int y = position.getY() - 3 + treeHeight; y <= position.getY() + treeHeight; ++y)
                    {
                        int adjHeight = y - (position.getY() + treeHeight);
                        int leavesWidth = 1 - adjHeight / 2;

                        for (int x = position.getX() - leavesWidth; x <= position.getX() + leavesWidth; ++x)
                        {
                            int l1 = x - position.getX();

                            for (int z = position.getZ() - leavesWidth; z <= position.getZ() + leavesWidth; ++z)
                            {
                                int j2 = z - position.getZ();

                                if (Math.abs(l1) != leavesWidth || Math.abs(j2) != leavesWidth
                                        || rand.nextInt(2) != 0 && adjHeight != 0)
                                {
                                    BlockPos blockpos = new BlockPos(x, y, z);
                                    state = worldIn.getBlockState(blockpos);

                                    if (state.getBlock().isAir(state, worldIn, blockpos)
                                            || state.getBlock().isLeaves(state, worldIn, blockpos)
                                            || state.getMaterial() == Material.VINE)
                                    {
                                        this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
                                    }
                                }
                            }
                        }
                    }

                    int leavesWidth = 1;
                    for (int x = position.getX() - leavesWidth; x <= position.getX() + leavesWidth; ++x)
                    {
                        for (int z = position.getZ() - leavesWidth; z <= position.getZ() + leavesWidth; ++z)
                        {
                            BlockPos blockpos = new BlockPos(x, position.getY() - 4 + treeHeight, z);
                            state = worldIn.getBlockState(blockpos);

                            if (state.getBlock().isAir(state, worldIn, blockpos)
                                    || state.getBlock().isLeaves(state, worldIn, blockpos)
                                    || state.getMaterial() == Material.VINE)
                            {
                                this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
                            }

                        }
                    }
                    BlockPos blockpos = new BlockPos(position.getX(), position.getY() - 5 + treeHeight, position.getZ() - 1);
                    this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
                    blockpos = new BlockPos(position.getX(), position.getY() - 5 + treeHeight, position.getZ() + 1);
                    this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
                    blockpos = new BlockPos(position.getX() - 1, position.getY() - 5 + treeHeight, position.getZ());
                    this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
                    blockpos = new BlockPos(position.getX() + 1, position.getY() - 5 + treeHeight, position.getZ());
                    this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);

                    // Generate Trunk
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
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }
}
