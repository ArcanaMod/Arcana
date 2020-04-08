package net.kineticdevelopment.arcana.common.objects.blocks.saplings;

import net.kineticdevelopment.arcana.common.objects.blocks.bases.SaplingBase;
import net.kineticdevelopment.arcana.common.worldgen.trees.DairGenerator;
import net.kineticdevelopment.arcana.common.worldgen.trees.TaintedOakGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public class DairSapling extends SaplingBase {
    boolean tainted;
    boolean untainted;

    public DairSapling(String name, boolean tainted, boolean untainted) {
        super(name);
        this.tainted = tainted;
        this.untainted = untainted;
    }

    @Override
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;

        WorldGenerator worldgenerator;
        if (tainted) {
            worldgenerator = untainted ? new DairGenerator(true, true, true) : new DairGenerator(true, true, false);
        } else {
            worldgenerator = new DairGenerator(true, false);
        }

        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);

        if(!worldgenerator.generate(worldIn, rand, pos)) {
            worldIn.setBlockState(pos, state, 4);
        }
    }
}
