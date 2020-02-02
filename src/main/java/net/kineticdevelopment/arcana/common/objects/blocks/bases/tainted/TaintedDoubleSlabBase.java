package net.kineticdevelopment.arcana.common.objects.blocks.bases.tainted;

import net.minecraft.block.material.Material;

/**
 * Basic Tainted Double Slabs, all tainted double slabs should either be this, or extend it
 *
 * @author Mozaran
 * @see TaintedSlabBase
 */
public class TaintedDoubleSlabBase extends TaintedSlabBase {
    public TaintedDoubleSlabBase(String name, Material material) {
        super(name, material);
    }

    @Override
    public boolean isDouble() {
        return true;
    }
}
