package net.kineticdevelopment.arcana.common.objects.blocks.saplings;

import net.kineticdevelopment.arcana.common.objects.blocks.bases.SaplingBase;
import net.kineticdevelopment.arcana.common.worldgen.trees.TaintedAcaciaGenerator;
import net.kineticdevelopment.arcana.common.worldgen.trees.TaintedLargeOakGenerator;
import net.kineticdevelopment.arcana.common.worldgen.trees.TaintedOakGenerator;
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
 * Used to grow all variations of the tainted acacia tree
 */
public class TaintedAcaciaSapling extends SaplingBase {
    boolean untainted;
    public TaintedAcaciaSapling(String name, boolean untainted) {
        super(name);
        this.untainted = untainted;
    }

    @Override
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;

        WorldGenerator worldgenerator = untainted ? new TaintedAcaciaGenerator(true, true, true) : new TaintedAcaciaGenerator(true, true, false);

        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);

        if(!worldgenerator.generate(worldIn, rand, pos)) {
            worldIn.setBlockState(pos, state, 4);
        }
    }
}
