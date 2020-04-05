package net.kineticdevelopment.arcana.common.objects.blocks.saplings;

import net.kineticdevelopment.arcana.common.objects.blocks.bases.SaplingBase;
import net.kineticdevelopment.arcana.common.worldgen.trees.TaintedLargeOakGenerator;
import net.kineticdevelopment.arcana.common.worldgen.trees.TaintedOakGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public class TaintedOakSapling extends SaplingBase {
    boolean untainted;
    public TaintedOakSapling(String name, boolean untainted) {
        super(name);
        this.untainted = untainted;
    }

    @Override
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        Random random = new Random();
        if (!TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;

        boolean bigTree = random.nextInt(10) == 0;
        WorldGenerator worldgenerator;
        if (untainted) {
            worldgenerator = bigTree ? new TaintedLargeOakGenerator(true, true) : new TaintedOakGenerator(true, true);
        } else {
            worldgenerator = bigTree ? new TaintedLargeOakGenerator(true, false) : new TaintedOakGenerator(true, false);
        }

        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);

        worldgenerator.generate(worldIn, rand, pos);
    }
}
