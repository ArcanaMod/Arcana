package net.arcanamod.items.attachment;

import net.arcanamod.items.WandPart;
import net.minecraft.util.ResourceLocation;

/**
 * Wand Focus Attachment
 *
 * @author Merijn
 * @see CapItem
 * @see WandPart
 */
public class FocusItem extends WandPart implements Focus{
	
	private final ResourceLocation modelLocation;
	
	public FocusItem(Properties properties, ResourceLocation location){
		super(properties);
		modelLocation = location;
		Focus.FOCI.add(this);
	}
	
	@Override
	public int getID(){
		return Focus.FOCI.indexOf(this);
	}
	
	public ResourceLocation getModelLocation(){
		return modelLocation;
	}
}