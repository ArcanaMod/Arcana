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
	
	public static Focus NONE = new Focus(new Properties()).setId(0);
	public static Focus DEFAULT = new Focus(new Properties()).setId(1);
	
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
	
	protected boolean shouldRegister(){
		return this != NONE;
	}
}