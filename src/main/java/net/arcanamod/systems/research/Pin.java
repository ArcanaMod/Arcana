package net.arcanamod.systems.research;

import net.minecraft.item.Item;

import javax.annotation.Nullable;

/**
 * A quick reference to a specific page in a research book, which may point to an item's recipe.
 */
public class Pin{
	
	@Nullable Item result;
	ResearchEntry entry;
	int stage;
	Icon icon;
	
	public Pin(@Nullable Item result, ResearchEntry entry, int stage, Icon icon){
		this.result = result;
		this.entry = entry;
		this.stage = stage;
		this.icon = icon;
	}
	
	public ResearchEntry getEntry(){
		return entry;
	}
	
	public int getStage(){
		return stage;
	}
	
	@Nullable
	public Item getResult(){
		return result;
	}
	
	public Icon getIcon(){
		return icon;
	}
}