package net.kineticdevelopment.arcana.common.objects.blocks.bases.untainted;

import net.kineticdevelopment.arcana.common.objects.blocks.bases.StairsBase;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

/**
 * Basic Untainted Stairs Block, all untainted stairs block should either be this, or extend it
 *
 * @author Mozaran
 * @see StairsBase
 */
public class UntaintedStairBase extends StairsBase implements IHasModel {
    public UntaintedStairBase(String name, IBlockState state) {
        super(name, state);
        this.setDefaultState(this.getDefaultState());
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
