package net.kineticdevelopment.arcana.common.objects.blocks.saplings;

import net.kineticdevelopment.arcana.common.objects.blocks.bases.SaplingBase;
import net.kineticdevelopment.arcana.common.worldgen.trees.SilverwoodGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public class SilverwoodSapling extends SaplingBase {
    public SilverwoodSapling(String name) {
        super(name);
    }

    @Override
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;

        WorldGenerator worldgenerator = new SilverwoodGenerator(true);

        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);

        if(!worldgenerator.generate(worldIn, rand, pos)) {
            worldIn.setBlockState(pos, state, 4);
        }
    }

}
