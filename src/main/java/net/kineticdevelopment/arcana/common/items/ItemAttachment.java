package net.kineticdevelopment.arcana.common.items;

import net.kineticdevelopment.arcana.api.wand.EnumAttachmentType;
import net.minecraft.item.Item;

public abstract class ItemAttachment extends Item {

    public ItemAttachment(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
    }


    public static ItemAttachment[][] buildDefaultArray() {
        ItemAttachment[][] attachments = new ItemAttachment[EnumAttachmentType.values().length][];

        for(int i = 0; i < attachments.length; ++i)
        {
            ItemAttachment attachment = EnumAttachmentType.getSlot(i).getDefault();
            attachments[i] = new ItemAttachment[] {attachment};
        }

        return attachments;
    }

    public abstract EnumAttachmentType getType();

    public abstract int getID();


}
