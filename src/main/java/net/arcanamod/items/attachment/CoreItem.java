// Not an attachment, should refactor
// TODO: Why do we have common.items AND common.objects.items? I think we should go with the first only tbh, objects is meaningless.
package net.arcanamod.items.attachment;

import net.arcanamod.items.WandPart;
import net.minecraft.util.ResourceLocation;

public class CoreItem extends WandPart implements Core{
	
	private final int maxVis;
	private final int level;
	private final ResourceLocation textureLocation;
	
	public CoreItem(Properties properties, int maxVis, int level, ResourceLocation location){
		super(properties);
		this.maxVis = maxVis;
		this.level = level;
		textureLocation = location;
	}
	
	public int getID(){
		return Core.CORES.indexOf(this);
	}
	
	public int maxVis(){
		return maxVis;
	}
	
	public int level(){
		return level;
	}
	
	public ResourceLocation getTextureLocation(){
		return textureLocation;
	}
}