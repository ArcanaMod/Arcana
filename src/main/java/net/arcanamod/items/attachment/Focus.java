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
	
	public static Focus NONE = new Focus("no_focus").setId(0);
	public static Focus DEFAULT = new Focus("wand_focus").setId(1);
	
	public Focus(String name){
		super(name);
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
	
	protected boolean shouldRegister(){
		return this != NONE;
	}
}