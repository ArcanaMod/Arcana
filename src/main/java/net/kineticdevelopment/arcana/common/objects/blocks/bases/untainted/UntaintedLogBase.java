package net.kineticdevelopment.arcana.common.objects.blocks.bases.untainted;

import net.kineticdevelopment.arcana.common.init.BlockStateInit;
import net.kineticdevelopment.arcana.common.objects.blocks.bases.LogBase;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;

/**
 * Basic Untainted Log, all untainted log block should either be this, or extend it
 *
 * @author Mozaran
 * @see LogBase
 */
public class UntaintedLogBase extends LogBase implements IHasModel {
    public static final PropertyBool FULLYTAINTED = BlockStateInit.FULLYTAINTED;

    public UntaintedLogBase(String name, Material material) {
        super(name, material);
        this.setDefaultState(this.getStateFromMeta(0));
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
