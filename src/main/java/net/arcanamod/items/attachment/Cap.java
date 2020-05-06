package net.arcanamod.items.attachment;

import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.ItemAttachment;
import net.arcanamod.wand.EnumAttachmentType;
import net.minecraftforge.fml.RegistryObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Wand Cap Attachment
 *
 * @author Merijn
 * @see ItemAttachment
 * @see Focus
 */
public class Cap extends ItemAttachment{
	
	private int level;
	
	public static List<Cap> CAPS = new ArrayList<>();
	
	public Cap(Properties properties){
		super(properties);
		CAPS.add(this);
	}
	
	@Override
	public EnumAttachmentType getType(){
		return EnumAttachmentType.CAP;
	}
	
	public int getID(){
		return CAPS.indexOf(this);
	}
	
	public int getLevel(){
		return level;
	}
	
	public Cap setLevel(int level){
		this.level = level;
		return this;
	}
}