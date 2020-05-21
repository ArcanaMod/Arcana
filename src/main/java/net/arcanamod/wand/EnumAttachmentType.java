package net.arcanamod.wand;

/**
 * Enumeration for Attachment Types
 *
 * @author Merijn
 */
public enum EnumAttachmentType{
	
	CAP("cap", 0),
	FOCUS("focus", 1);
	
	private int slot;
	private String key;
	
	EnumAttachmentType(String key, int slot){
		this.slot = slot;
		this.key = key;
	}
	
	public int getSlot(){
		return slot;
	}
	
	public String getKey(){
		return key;
	}
}
