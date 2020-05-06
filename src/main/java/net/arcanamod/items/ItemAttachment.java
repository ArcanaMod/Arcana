package net.arcanamod.items;

import net.arcanamod.wand.EnumAttachmentType;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Item Attachment Utility Class
 *
 * @author Merijn
 */
public abstract class ItemAttachment extends Item{
	
	public static List<ItemAttachment> ATTACHMENTS = new ArrayList<>();
	
	public ItemAttachment(Properties properties){
		super(properties);
		ATTACHMENTS.add(this);
	}
	
	/**
	 * Builds an array with the default attachments.
	 *
	 * @return Array with the default attachments
	 */
	public static ItemAttachment[][] buildDefaultArray(){
		ItemAttachment[][] attachments = new ItemAttachment[EnumAttachmentType.values().length][];
		
		for(int i = 0; i < attachments.length; ++i){
			ItemAttachment attachment = EnumAttachmentType.getSlot(i).getDefault();
			attachments[i] = new ItemAttachment[]{attachment};
		}
		
		return attachments;
	}
	
	public abstract EnumAttachmentType getType();
	
	public abstract int getID();
}