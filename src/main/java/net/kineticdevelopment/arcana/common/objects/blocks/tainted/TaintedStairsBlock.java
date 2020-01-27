package net.kineticdevelopment.arcana.common.objects.blocks.tainted;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.kineticdevelopment.arcana.common.init.BlockStateInit;
import net.kineticdevelopment.arcana.common.objects.blocks.bases.BlockBase;
import net.kineticdevelopment.arcana.common.objects.blocks.bases.StairsBase;
import net.kineticdevelopment.arcana.common.objects.blocks.tainted.bases.TaintedBlockBase;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.kineticdevelopment.arcana.utilities.taint.TaintHandler;
import net.kineticdevelopment.arcana.utilities.taint.TaintLevelHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Basic Tainted Stairs Block, all tainted stairs block should either be this, or extend it
 *
 * @author Atlas
 * @see StairsBase
 */

public class TaintedStairsBlock extends StairsBase implements IHasModel {
    public static final PropertyBool FULLYTAINTED = BlockStateInit.FULLYTAINTED;

    public TaintedStairsBlock(String name, IBlockState state) {
        super(name, state);
        this.setDefaultState(this.getDefaultState().withProperty(BlockStateInit.FULLYTAINTED, false));
        this.setTickRandomly(true);
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        int h = 0;

        int f = (int) TaintLevelHandler.getTaintLevel(worldIn);

        if (f >= 5 && f <= 9) {
            h = ThreadLocalRandom.current().nextInt(0, 10);
        } else if (f >= 10 && f <= 19) {
            h = ThreadLocalRandom.current().nextInt(0, 9);
        } else if (f >= 20 && f <= 29) {
            h = ThreadLocalRandom.current().nextInt(0, 8);
        } else if (f >= 30 && f <= 39) {
            h = ThreadLocalRandom.current().nextInt(0, 7);
        } else if (f >= 40 && f <= 49) {
            h = ThreadLocalRandom.current().nextInt(0, 6);
        } else if (f >= 50 && f <= 59) {
            h = ThreadLocalRandom.current().nextInt(0, 5);
        } else if (f >= 60 && f <= 69) {
            h = ThreadLocalRandom.current().nextInt(0, 4);
        } else if (f >= 70 && f <= 79) {
            h = ThreadLocalRandom.current().nextInt(0, 3);
        } else if (f >= 80 && f <= 89) {
            h = ThreadLocalRandom.current().nextInt(0, 2);
        } else if (f >= 90 && f <= 99) {
            h = ThreadLocalRandom.current().nextInt(0, 1);
        } else if (f >= 100) {
            h = 1;
        }
        if (h == 1) {
            TaintHandler.spreadTaint(worldIn, pos);
        }

        boolean surrounded = true;

        for (int x = -1; x < 2; x++) {

            for (int y = -1; y < 2; y++) {

                for (int z = -1; z < 2; z++) {

                    BlockPos nPos = pos.add(x, y, z);

                    Block b = worldIn.getBlockState(nPos).getBlock();

                    if(!(b instanceof TaintedBlockBase) && !(b instanceof BlockAir)) {
                        surrounded = false;
                    }
                }
            }
        }

        if(surrounded == true) {

            worldIn.setBlockState(pos, state.withProperty(BlockStateInit.FULLYTAINTED, true));
            this.setTickRandomly(false);
        }
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        boolean tainted = state.getValue(FULLYTAINTED);

        if(tainted) {
            i = 1;
        } else {
            i = 0;
        }

        return i;
    }

    public IBlockState getStateFromMeta(int meta) {
        boolean tainted = false;
        int i = meta;

        if(i == 1) {
            tainted = true;
        } else if(i == 0) {
            tainted = false;
        }

        return this.getDefaultState().withProperty(FULLYTAINTED, tainted);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FULLYTAINTED});
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
