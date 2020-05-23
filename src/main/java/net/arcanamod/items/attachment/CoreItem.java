// Not an attachment, should refactor
// TODO: Why do we have common.items AND common.objects.items? I think we should go with the first only tbh, objects is meaningless.
package net.arcanamod.items.attachment;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class CoreItem extends Item implements Core{
	
	private final int maxVis;
	private final int level;
	private final ResourceLocation id;
	
	public CoreItem(Properties properties, int maxVis, int level, ResourceLocation id){
		super(properties);
		this.maxVis = maxVis;
		this.level = level;
		this.id = id;
		CORES.put(getId(), this);
	}
	
	public int maxVis(){
		return maxVis;
	}
	
	public int level(){
		return level;
	}
	
	public ResourceLocation getId(){
		return id;
	}
}