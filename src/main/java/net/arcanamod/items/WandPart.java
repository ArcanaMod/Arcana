package net.arcanamod.items;

import net.minecraft.item.Item;

/**
 * Item Attachment Utility Class
 *
 * @author Merijn
 */
public abstract class WandPart extends Item{
	
	public WandPart(Properties properties){
		super(properties);
	}
	
	public abstract int getID();
}