package net.kineticdevelopment.arcana.common.objects.blocks.tainted;

import net.kineticdevelopment.arcana.common.init.BlockStateInit;
import net.kineticdevelopment.arcana.common.objects.blocks.bases.BlockBase;
import net.kineticdevelopment.arcana.common.objects.blocks.bases.LogBase;
import net.kineticdevelopment.arcana.common.objects.blocks.tainted.bases.TaintedBlockBase;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.kineticdevelopment.arcana.utilities.taint.TaintHandler;
import net.kineticdevelopment.arcana.utilities.taint.TaintLevelHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Basic Tainted Log, all tainted log block should either be this, or extend it
 *
 * @author Tea
 * @see LogBase
 */
public class TaintedLogBlock extends LogBase implements IHasModel {
    public static final PropertyBool FULLYTAINTED = BlockStateInit.FULLYTAINTED;

    public TaintedLogBlock(String name, Material material) {
        super(name, material);
        this.setDefaultState(this.getStateFromMeta(0));
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

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        boolean tainted = state.getValue(FULLYTAINTED);
        EnumFacing.Axis enumfacing$axis = (EnumFacing.Axis)state.getValue(AXIS);

        // Log Axis Variations are even
        if (enumfacing$axis == EnumFacing.Axis.X)
        {
            i = 2;
        }
        else if (enumfacing$axis == EnumFacing.Axis.Z)
        {
            i = 4;
        }

        // Adding Fully Tainted Variants are Odd
        if(tainted) {
            i += 1;
        }

        return i;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Y;
        boolean tainted = false;
        int i = meta;

        // Axis's are Even and Fully Tainted Variants are Odd
        if(i < 2) {
            tainted = (i % 2 == 1);
        } else if(i < 4) {
            enumfacing$axis = EnumFacing.Axis.X;
            tainted = (i % 2 == 1);
        } else if(i < 6) {
            enumfacing$axis = EnumFacing.Axis.Z;
            tainted = (i % 2 == 1);
        }

        return this.getDefaultState().withProperty(FULLYTAINTED, tainted).withProperty(AXIS, enumfacing$axis);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FULLYTAINTED, AXIS});
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}