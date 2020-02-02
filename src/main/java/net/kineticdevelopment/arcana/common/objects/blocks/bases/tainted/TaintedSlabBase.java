package net.kineticdevelopment.arcana.common.objects.blocks.bases.tainted;

import net.kineticdevelopment.arcana.common.objects.blocks.bases.DoubleSlabBase;
import net.kineticdevelopment.arcana.common.objects.blocks.bases.HalfSlabBase;
import net.kineticdevelopment.arcana.common.objects.blocks.bases.SlabBase;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.taint.TaintHandler;
import net.kineticdevelopment.arcana.utilities.taint.TaintLevelHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Tainted Slab Base. To be extended by tainted double slab base and tainted half slab base.
 *
 * @author Tea, Mozaran
 * @see TaintedHalfSlabBase
 * @see TaintedDoubleSlabBase
 */
public abstract class TaintedSlabBase extends SlabBase {
    private String name;

    public TaintedSlabBase(String name, Material material) {
        super(name, material);
        this.setTickRandomly(true);
        this.name = name;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
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
            this.setTickRandomly(false);
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        Block block = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(Main.MODID, "un" + name.replace("_double", "")));
        if(block != null) {
            Item item = Item.getItemFromBlock(block);
            drops.add(new ItemStack(item, isDouble() ? 2 : 1, this.damageDropped(state)));
        }
    }
}
