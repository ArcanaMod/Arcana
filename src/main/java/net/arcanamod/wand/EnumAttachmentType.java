package net.arcanamod.wand;

import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.AttachmentItem;
import net.minecraftforge.fml.RegistryObject;

/**
 * Enumeration for Attachment Types
 *
 * @author Merijn
 */
public enum EnumAttachmentType{
	
	CAP("cap", 0, ArcanaItems.IRON_CAP),
	FOCUS("focus", 1, ArcanaItems.FOCUS_NONE);
	
	private RegistryObject<? extends AttachmentItem> _default;
	private int slot;
	private String key;
	
	EnumAttachmentType(String key, int slot, RegistryObject<? extends AttachmentItem> _default){
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
	
	public AttachmentItem getDefault(){
		return _default.get();
	}
	
	public String getKey(){
		return key;
	}
}
