package net.kineticdevelopment.arcana.common.objects.items;

import net.minecraft.util.ResourceLocation;

public class ResearchBookItem extends ItemBase{
	
	ResourceLocation book;
	
	public ResearchBookItem(String name, ResourceLocation book){
		super(name);
		this.book = book;
	}
}