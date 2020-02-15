package net.kineticdevelopment.arcana.common.objects.blocks.saplings;

import net.kineticdevelopment.arcana.common.objects.blocks.bases.SaplingBase;
import net.kineticdevelopment.arcana.common.worldgen.DairTreeGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public class DairSapling extends SaplingBase {
    public DairSapling(String name) {
        super(name);
    }

    @Override
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;
        WorldGenerator worldgenerator = new DairTreeGenerator(true);

        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);

        worldgenerator.generate(worldIn, rand, pos);
    }
}
