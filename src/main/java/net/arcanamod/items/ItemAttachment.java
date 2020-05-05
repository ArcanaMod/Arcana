package net.arcanamod.items;

import net.arcanamod.wand.EnumAttachmentType;

import java.util.ArrayList;
import java.util.List;

/**
 * Item Attachment Utility Class
 *
 * @author Merijn
 */
public abstract class ItemAttachment extends ItemBase{
	
	public static List<ItemAttachment> ATTACHMENTS = new ArrayList<>();
	
	public ItemAttachment(String name){
		super(name);
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