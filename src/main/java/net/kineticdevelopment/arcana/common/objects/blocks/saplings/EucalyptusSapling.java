package net.kineticdevelopment.arcana.common.objects.blocks.saplings;

import net.kineticdevelopment.arcana.common.objects.blocks.bases.SaplingBase;
import net.kineticdevelopment.arcana.common.worldgen.trees.EucalyptusGenerator;
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
 * Used to grow all variations of the hawthorn tree
 */
public class EucalyptusSapling extends SaplingBase {
    boolean tainted;
    boolean untainted;

    public EucalyptusSapling(String name, boolean tainted, boolean untainted) {
        super(name);
        this.tainted = tainted;
        this.untainted = untainted;
    }

    @Override
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;

        WorldGenerator worldgenerator;
        if (tainted) {
            worldgenerator = untainted ? new EucalyptusGenerator(true, true, true) : new EucalyptusGenerator(true, true, false);
        } else {
            worldgenerator = new EucalyptusGenerator(true, false);
        }

        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);

        if(!worldgenerator.generate(worldIn, rand, pos)) {
            worldIn.setBlockState(pos, state, 4);
        }
    }
}
