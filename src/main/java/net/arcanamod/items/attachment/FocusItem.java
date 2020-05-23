package net.arcanamod.items.attachment;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * Wand Focus Attachment
 *
 * @author Merijn
 * @see CapItem
 */
public class FocusItem extends Item implements Focus{
	
	private final ResourceLocation modelLocation;
	
	public FocusItem(Properties properties, ResourceLocation location){
		super(properties);
		modelLocation = location;
		Focus.FOCI.add(this);
	}
	
	
	public ResourceLocation getModelLocation(){
		return modelLocation;
	}
}