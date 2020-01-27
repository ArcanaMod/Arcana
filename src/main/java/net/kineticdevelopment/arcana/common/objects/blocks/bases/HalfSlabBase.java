package net.kineticdevelopment.arcana.common.objects.blocks.bases;
import net.kineticdevelopment.arcana.common.objects.blocks.bases.SlabBase;

/**
 * Basic Half Slab, all half slabs should either be this, or extend it
 *
 * @author Tea
 *
 */
public class HalfSlabBase extends SlabBase {

    public HalfSlabBase(String name) {
        super(name);

    }

    @Override
    public boolean isDouble() {
        return false;
    }

}
