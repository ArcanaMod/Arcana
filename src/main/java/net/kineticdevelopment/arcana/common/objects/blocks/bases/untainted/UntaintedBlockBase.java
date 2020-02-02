package net.kineticdevelopment.arcana.common.objects.blocks.bases.untainted;

import net.kineticdevelopment.arcana.common.init.BlockStateInit;
import net.kineticdevelopment.arcana.common.objects.blocks.bases.BlockBase;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

/**
 * Basic Untainted Block, all untainted block should either be this, or extend it
 *
 * @author Mozaran
 * @see BlockBase
 */
public class UntaintedBlockBase extends BlockBase implements IHasModel {
    public static final PropertyBool FULLYTAINTED = BlockStateInit.FULLYTAINTED;

    public UntaintedBlockBase(String name, Material material) {
        super(name, material);
        this.setDefaultState(this.getDefaultState().withProperty(BlockStateInit.FULLYTAINTED, false));
    }

    @Override
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

    @Override
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
