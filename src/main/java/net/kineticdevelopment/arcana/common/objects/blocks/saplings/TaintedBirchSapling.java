package net.kineticdevelopment.arcana.common.objects.blocks.saplings;

import net.kineticdevelopment.arcana.common.objects.blocks.bases.SaplingBase;
import net.kineticdevelopment.arcana.common.worldgen.trees.TaintedAcaciaGenerator;
import net.kineticdevelopment.arcana.common.worldgen.trees.TaintedBirchGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public class TaintedBirchSapling extends SaplingBase {
    boolean untainted;
    boolean useExtraRandomHeight;
    public TaintedBirchSapling(String name, boolean useExtraRandomHeight, boolean untainted) {
        super(name);
        this.untainted = untainted;
        this.useExtraRandomHeight = useExtraRandomHeight;
    }

    @Override
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;

        WorldGenerator worldgenerator = untainted ?
                new TaintedBirchGenerator(true, useExtraRandomHeight, true, true) :
                new TaintedBirchGenerator(true, useExtraRandomHeight, true, false);

        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);

        worldgenerator.generate(worldIn, rand, pos);
    }
}
