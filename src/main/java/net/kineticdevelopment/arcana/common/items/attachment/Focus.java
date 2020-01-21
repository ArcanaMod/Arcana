package net.kineticdevelopment.arcana.common.items.attachment;

import net.kineticdevelopment.arcana.api.wand.EnumAttachmentType;
import net.kineticdevelopment.arcana.common.items.ItemAttachment;

public class Focus extends ItemAttachment {

    private int id;

    public static Focus DEFAULT = new Focus("wand_focus").setId(0);

    public Focus(String name) {
        super(name);
    }

    @Override
    public EnumAttachmentType getType() {
        return EnumAttachmentType.FOCUS;
    }

    @Override
    public int getID() {
        return id;
    }

    public Focus setId(int id) {
        this.id = id;
        return this;
    }
}
