package net.arcanamod.items.attachment;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * An item that implements {@link Cap}. Allows for registering an item and cap type together.
 *
 * @author Merijn, Luna
 * @see Cap
 */
public class CapItem extends Item implements Cap{
	
	private final int complexity, visStorage, level;
	private final ResourceLocation id;
	
	public CapItem(Properties properties, int visStorage, int complexity, int level, ResourceLocation id){
		super(properties);
		this.complexity = complexity;
		this.visStorage = visStorage;
		this.level = level;
		this.id = id;
		CAPS.put(getId(), this);
	}
	
	public int getLevel(){
		return level;
	}
	
	public int complexity(){
		return complexity;
	}
	
	public int visStorage(){
		return visStorage;
	}
	
	public int level(){
		return level;
	}
	
	public ResourceLocation getId(){
		return id;
	}
}