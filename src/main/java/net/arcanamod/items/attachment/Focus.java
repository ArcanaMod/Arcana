package net.arcanamod.items.attachment;

import net.arcanamod.items.ItemAttachment;
import net.arcanamod.wand.EnumAttachmentType;

/**
 * Wand Focus Attachment
 *
 * @author Merijn
 * @see Cap
 * @see ItemAttachment
 */
public class Focus extends ItemAttachment{
	
	private int id;
	
	public Focus(Properties properties){
		super(properties);
	}
	
	@Override
	public EnumAttachmentType getType(){
		return EnumAttachmentType.FOCUS;
	}
	
	@Override
	public int getID(){
		return id;
	}
	
	public Focus setId(int id){
		this.id = id;
		return this;
	}
}