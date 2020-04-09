package net.kineticdevelopment.arcana.common.worldgen.trees;

import net.kineticdevelopment.arcana.common.init.BlockInit;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenHugeTrees;

import java.util.Random;

public class TaintedMegaJungleGenerator extends WorldGenHugeTrees {
    private static final IBlockState DEFAULT_TAINTED_TRUNK = BlockInit.TAINTED_JUNGLE_LOG.getDefaultState();
    private static final IBlockState DEFAULT_UNTAINTED_TRUNK = BlockInit.UNTAINTED_JUNGLE_LOG.getDefaultState();
    private static final IBlockState DEFAULT_TAINTED_LEAVES = BlockInit.TAINTED_JUNGLE_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
    private static final IBlockState DEFAULT_UNTAINTED_LEAVES = BlockInit.UNTAINTED_JUNGLE_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);

    public TaintedMegaJungleGenerator(boolean notify, int baseHeightIn, int extraRandomHeightIn, boolean untainted)
    {
        super(notify, baseHeightIn, extraRandomHeightIn, untainted ? DEFAULT_UNTAINTED_TRUNK : DEFAULT_TAINTED_TRUNK,
                untainted ? DEFAULT_UNTAINTED_LEAVES : DEFAULT_TAINTED_LEAVES);
    }

    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        int i = this.getHeight(rand);

        if (!this.ensureGrowable(worldIn, rand, position, i))
        {
            return false;
        }
        else
        {
            this.createCrown(worldIn, position.up(i), 2);

            for (int j = position.getY() + i - 2 - rand.nextInt(4); j > position.getY() + i / 2; j -= 2 + rand.nextInt(4))
            {
                float f = rand.nextFloat() * ((float)Math.PI * 2F);
                int k = position.getX() + (int)(0.5F + MathHelper.cos(f) * 4.0F);
                int l = position.getZ() + (int)(0.5F + MathHelper.sin(f) * 4.0F);

                for (int i1 = 0; i1 < 5; ++i1)
                {
                    k = position.getX() + (int)(1.5F + MathHelper.cos(f) * (float)i1);
                    l = position.getZ() + (int)(1.5F + MathHelper.sin(f) * (float)i1);
                    this.setBlockAndNotifyAdequately(worldIn, new BlockPos(k, j - 3 + i1 / 2, l), this.woodMetadata);
                }

                int j2 = 1 + rand.nextInt(2);
                int j1 = j;

                for (int k1 = j - j2; k1 <= j1; ++k1)
                {
                    int l1 = k1 - j1;
                    this.growLeavesLayer(worldIn, new BlockPos(k, k1, l), 1 - l1);
                }
            }

            for (int i2 = 0; i2 < i; ++i2)
            {
                BlockPos blockpos = position.up(i2);

                if (this.isAirLeaves(worldIn,blockpos))
                {
                    this.setBlockAndNotifyAdequately(worldIn, blockpos, this.woodMetadata);

                    if (i2 > 0)
                    {
                        this.placeVine(worldIn, rand, blockpos.west(), BlockVine.EAST);
                        this.placeVine(worldIn, rand, blockpos.north(), BlockVine.SOUTH);
                    }
                }

                if (i2 < i - 1)
                {
                    BlockPos blockpos1 = blockpos.east();

                    if (this.isAirLeaves(worldIn,blockpos1))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, blockpos1, this.woodMetadata);

                        if (i2 > 0)
                        {
                            this.placeVine(worldIn, rand, blockpos1.east(), BlockVine.WEST);
                            this.placeVine(worldIn, rand, blockpos1.north(), BlockVine.SOUTH);
                        }
                    }

                    BlockPos blockpos2 = blockpos.south().east();

                    if (this.isAirLeaves(worldIn,blockpos2))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, blockpos2, this.woodMetadata);

                        if (i2 > 0)
                        {
                            this.placeVine(worldIn, rand, blockpos2.east(), BlockVine.WEST);
                            this.placeVine(worldIn, rand, blockpos2.south(), BlockVine.NORTH);
                        }
                    }

                    BlockPos blockpos3 = blockpos.south();

                    if (this.isAirLeaves(worldIn,blockpos3))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, blockpos3, this.woodMetadata);

                        if (i2 > 0)
                        {
                            this.placeVine(worldIn, rand, blockpos3.west(), BlockVine.EAST);
                            this.placeVine(worldIn, rand, blockpos3.south(), BlockVine.NORTH);
                        }
                    }
                }
            }

            return true;
        }
    }

    private void placeVine(World p_181632_1_, Random p_181632_2_, BlockPos p_181632_3_, PropertyBool p_181632_4_)
    {
        if (p_181632_2_.nextInt(3) > 0 && p_181632_1_.isAirBlock(p_181632_3_))
        {
            this.setBlockAndNotifyAdequately(p_181632_1_, p_181632_3_, Blocks.VINE.getDefaultState().withProperty(p_181632_4_, Boolean.TRUE));
        }
    }

    private void createCrown(World worldIn, BlockPos p_175930_2_, int p_175930_3_)
    {
        int i = 2;

        for (int j = -2; j <= 0; ++j)
        {
            this.growLeavesLayerStrict(worldIn, p_175930_2_.up(j), p_175930_3_ + 1 - j);
        }
    }

    //Helper macro
    private boolean isAirLeaves(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos);
    }
}
