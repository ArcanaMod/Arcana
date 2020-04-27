package net.kineticdevelopment.arcana.common.objects.blocks.saplings;

import net.kineticdevelopment.arcana.common.init.BlockInit;
import net.kineticdevelopment.arcana.common.objects.blocks.bases.SaplingBase;
import net.kineticdevelopment.arcana.common.worldgen.trees.GreatwoodGenerator;
import net.kineticdevelopment.arcana.common.worldgen.trees.TaintedDarkOakGenerator;
import net.kineticdevelopment.arcana.common.worldgen.trees.TaintedSpruceGenerator;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

/**
 * @author Mozaran
 *
 * Used to grow all variations of the greatwood tree
 */
public class GreatwoodSapling extends SaplingBase {
    boolean tainted;
    boolean untainted;
    public GreatwoodSapling(String name, boolean tainted, boolean untainted) {
        super(name);
        this.tainted = tainted;
        this.untainted = untainted;
    }

    @Override
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;

        int x = 0;
        int z = 0;
        boolean flag = false;

        WorldGenerator worldgenerator = new GreatwoodGenerator(true, false, false);

        IBlockState iblockstate2 = Blocks.AIR.getDefaultState();

        for (x = 0; x >= -1; --x)
        {
            for (z = 0; z >= -1; --z)
            {
                if (this.isTwoByTwoOfType(worldIn, pos, x, z))
                {
                    if(tainted) {
                        worldgenerator = untainted ? new GreatwoodGenerator(true, true, true) :
                                new GreatwoodGenerator(true, true, false);
                    } else {
                        worldgenerator = new GreatwoodGenerator(true, false, false);
                    }
                    flag = true;
                    break;

                }
            }
            if (flag) break;
        }

        if (!flag)
        {
            return;
        }

        worldIn.setBlockState(pos.add(x, 0, z), iblockstate2, 4);
        worldIn.setBlockState(pos.add(x + 1, 0, z), iblockstate2, 4);
        worldIn.setBlockState(pos.add(x, 0, z + 1), iblockstate2, 4);
        worldIn.setBlockState(pos.add(x + 1, 0, z + 1), iblockstate2, 4);


        if (!worldgenerator.generate(worldIn, rand, pos.add(x, 0, z)))
        {
            worldIn.setBlockState(pos.add(x, 0, z), state, 4);
            worldIn.setBlockState(pos.add(x + 1, 0, z), state, 4);
            worldIn.setBlockState(pos.add(x, 0, z + 1), state, 4);
            worldIn.setBlockState(pos.add(x + 1, 0, z + 1), state, 4);
        }
    }

    private boolean isTwoByTwoOfType(World worldIn, BlockPos pos, int x, int z)
    {
        BlockPos pos1 = pos.add(x, 0, z);
        boolean valid1 = this.isTypeAt(worldIn, pos1);
        BlockPos pos2 = pos.add(x + 1, 0, z);
        boolean valid2 = this.isTypeAt(worldIn, pos2);
        BlockPos pos3 = pos.add(x, 0, z + 1);
        boolean valid3 = this.isTypeAt(worldIn, pos3);
        BlockPos pos4 = pos.add(x + 1, 0, z + 1);
        boolean valid4 = this.isTypeAt(worldIn, pos4);
        return valid1 && valid2 && valid3 && valid4;
    }

    public boolean isTypeAt(World worldIn, BlockPos pos)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock() == this;
    }
}
