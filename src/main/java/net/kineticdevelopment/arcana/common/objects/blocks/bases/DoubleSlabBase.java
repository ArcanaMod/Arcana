package net.kineticdevelopment.arcana.common.objects.blocks.bases;

/**
 * Basic Double Slabs, all double slabs should either be this, or extend it
 *
 * @author Tea
 *
 */
public class DoubleSlabBase extends SlabBase {

    public DoubleSlabBase(String name) {
        super(name);
    }

    @Override
    public boolean isDouble() {
        return true;
    }
}
