package net.arcanamod.items.attachment;

import net.arcanamod.items.WandPart;
import net.minecraft.util.ResourceLocation;

/**
 * Wand Cap Attachment
 *
 * @author Merijn
 * @see WandPart
 * @see FocusItem
 */
public class CapItem extends WandPart implements Cap{
	
	private final int power, maxEffects, level;
	private final ResourceLocation modelLocation;
	
	public CapItem(Properties properties, int power, int maxEffects, int level, ResourceLocation location){
		super(properties);
		this.power = power;
		this.maxEffects = maxEffects;
		this.level = level;
		modelLocation = location;
		CAPS.add(this);
	}
	
	public int getID(){
		return CAPS.indexOf(this);
	}
	
	public int getLevel(){
		return level;
	}
	
	public int power(){
		return power;
	}
	
	public int maxEffects(){
		return maxEffects;
	}
	
	public int level(){
		return level;
	}
	
	public ResourceLocation getTextureLocation(){
		return modelLocation;
	}
}