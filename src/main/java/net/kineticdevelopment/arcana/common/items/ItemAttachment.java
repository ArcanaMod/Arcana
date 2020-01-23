package net.kineticdevelopment.arcana.common.items;

import net.kineticdevelopment.arcana.api.wand.EnumAttachmentType;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Item Attachment Utility Class
 * 
 * @author Merijn
 * @see Cap
 * @see Focus
 */
public abstract class ItemAttachment extends Item {

    public static List<ItemAttachment> ATTACHMENTS = new ArrayList<>();

    public ItemAttachment(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        ATTACHMENTS.add(this);
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
