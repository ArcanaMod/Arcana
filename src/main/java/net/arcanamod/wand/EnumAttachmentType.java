package net.arcanamod.wand;

import net.arcanamod.items.ItemAttachment;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.Focus;

/**
 * Enumeration for Attachment Types
 *
 * @author Merijn
 */
public enum EnumAttachmentType{
	
	CAP("cap", 0, Cap.DEFAULT),
	FOCUS("focus", 1, Focus.NONE);
	
	private ItemAttachment _default;
	private int slot;
	private String key;
	
	EnumAttachmentType(String key, int slot, ItemAttachment _default){
		this._default = _default;
		this.slot = slot;
		this.key = key;
	}
	
	public int getSlot(){
		return slot;
	}
	
	public static EnumAttachmentType getSlot(int slot){
		return EnumAttachmentType.values()[slot];
	}
	
	public ItemAttachment getDefault(){
		return _default;
	}
	
	public String getKey(){
		return key;
	}
}
