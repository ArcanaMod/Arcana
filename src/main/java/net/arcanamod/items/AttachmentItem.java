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
public abstract class AttachmentItem extends Item{
	
	public static List<AttachmentItem> ATTACHMENTS = new ArrayList<>();
	
	public AttachmentItem(Properties properties){
		super(properties);
		ATTACHMENTS.add(this);
	}
	
	/**
	 * Builds an array with the default attachments.
	 *
	 * @return Array with the default attachments
	 */
	public static AttachmentItem[][] buildDefaultArray(){
		AttachmentItem[][] attachments = new AttachmentItem[EnumAttachmentType.values().length][];
		
		for(int i = 0; i < attachments.length; ++i){
			AttachmentItem attachment = EnumAttachmentType.getSlot(i).getDefault();
			attachments[i] = new AttachmentItem[]{attachment};
		}
		
		return attachments;
	}
	
	public abstract EnumAttachmentType getType();
	
	public abstract int getID();
}