package net.kineticdevelopment.arcana.api.wand;

import net.kineticdevelopment.arcana.common.items.ItemAttachment;
import net.kineticdevelopment.arcana.common.items.attachment.Cap;
import net.kineticdevelopment.arcana.common.items.attachment.Focus;

public enum EnumAttachmentType {

    CAP("cap",0, Cap.DEFAULT),
    FOCUS("focus",1, Focus.DEFAULT);
    
    private ItemAttachment _default;
    private int slot;
    private String key;

    EnumAttachmentType(String key, int slot, ItemAttachment _default) {
        this._default = _default;
        this.slot = slot;
        this.key = key;
    }

    public int getSlot() {
        return slot;
    }

    public static EnumAttachmentType getSlot(int slot) {
        return EnumAttachmentType.values()[slot];
    }

    public ItemAttachment getDefault() {
        return _default;
    }

    public String getKey() {
        return key;
    }
}
