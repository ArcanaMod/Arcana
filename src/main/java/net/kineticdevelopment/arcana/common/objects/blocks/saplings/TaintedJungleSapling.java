package net.kineticdevelopment.arcana.common.objects.blocks.saplings;

import net.kineticdevelopment.arcana.common.objects.blocks.bases.SaplingBase;
import net.kineticdevelopment.arcana.common.worldgen.trees.TaintedJungleGenerator;
import net.kineticdevelopment.arcana.common.worldgen.trees.TaintedMegaJungleGenerator;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public class TaintedJungleSapling extends SaplingBase {
    boolean untainted;
    public TaintedJungleSapling(String name, boolean untainted) {
        super(name);
        this.untainted = untainted;
    }

    @Override
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;
        WorldGenerator worldgenerator = new WorldGenTrees(true);
        int x = 0;
        int z = 0;
        boolean flag = false;

        for (x = 0; x >= -1; --x)
        {
            for (z = 0; z >= -1; --z)
            {
                if (this.isTwoByTwoOfType(worldIn, pos, x, z, BlockPlanks.EnumType.JUNGLE))
                {
                    worldgenerator = untainted ? new TaintedMegaJungleGenerator(true, 10, 20, true) :
                            new TaintedMegaJungleGenerator(true, 10, 20, false);
                    flag = true;
                    break;
                }
            }
            if(flag) break;
        }

        if (!flag)
        {
            x = 0;
            z = 0;
            worldgenerator = untainted ? new TaintedJungleGenerator(true, true, true) :
                    new TaintedJungleGenerator(true, true, false);
        }

        IBlockState iblockstate2 = Blocks.AIR.getDefaultState();

        if (flag)
        {
            worldIn.setBlockState(pos.add(x, 0, z), iblockstate2, 4);
            worldIn.setBlockState(pos.add(x + 1, 0, z), iblockstate2, 4);
            worldIn.setBlockState(pos.add(x, 0, z + 1), iblockstate2, 4);
            worldIn.setBlockState(pos.add(x + 1, 0, z + 1), iblockstate2, 4);
        }
        else
        {
            worldIn.setBlockState(pos, iblockstate2, 4);
        }

        if (!worldgenerator.generate(worldIn, rand, pos.add(x, 0, z)))
        {
            if (flag)
            {
                worldIn.setBlockState(pos.add(x, 0, z), state, 4);
                worldIn.setBlockState(pos.add(x + 1, 0, z), state, 4);
                worldIn.setBlockState(pos.add(x, 0, z + 1), state, 4);
                worldIn.setBlockState(pos.add(x + 1, 0, z + 1), state, 4);
            }
            else
            {
                worldIn.setBlockState(pos, state, 4);
            }
        }
    }

    private boolean isTwoByTwoOfType(World worldIn, BlockPos pos, int x, int z, BlockPlanks.EnumType type)
    {
        BlockPos pos1 = pos.add(x, 0, z);
        boolean valid1 = this.isTypeAt(worldIn, pos1, type);
        BlockPos pos2 = pos.add(x + 1, 0, z);
        boolean valid2 = this.isTypeAt(worldIn, pos2, type);
        BlockPos pos3 = pos.add(x, 0, z + 1);
        boolean valid3 = this.isTypeAt(worldIn, pos3, type);
        BlockPos pos4 = pos.add(x + 1, 0, z + 1);
        boolean valid4 = this.isTypeAt(worldIn, pos4, type);
        return valid1 && valid2 && valid3 && valid4;
    }

    public boolean isTypeAt(World worldIn, BlockPos pos, BlockPlanks.EnumType type)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock() == this;
    }
}
