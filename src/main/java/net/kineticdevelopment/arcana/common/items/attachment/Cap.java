package net.kineticdevelopment.arcana.common.items.attachment;

import net.kineticdevelopment.arcana.api.wand.EnumAttachmentType;
import net.kineticdevelopment.arcana.common.items.ItemAttachment;

public class Cap extends ItemAttachment {

    private int id;

    public static Cap DEFAULT = new Cap("iron_cap").setId(0);

    public Cap(String name) {
        super(name);
    }

    @Override
    public EnumAttachmentType getType() {
        return EnumAttachmentType.CAP;
    }

    @Override
    public int getID() {
        return id;
    }

    public Cap setId(int id) {
        this.id = id;
        return this;
    }
}
