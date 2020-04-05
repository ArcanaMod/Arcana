package net.kineticdevelopment.arcana.client.gui;

import net.kineticdevelopment.arcana.core.aspects.Aspect;
import net.kineticdevelopment.arcana.core.aspects.AspectHandler;

import java.util.function.Supplier;

public class AspectSlot{
	
	public Aspect aspect;
	public final Supplier<AspectHandler> inventory;
	
	public int x, y;
	
	/**
	 * If true, this slot will act more like an item stack: when empty, any aspect can be inserted
	 * and it will change to that aspect, and when fully drained it will lose its aspect.
	 */
	public boolean storeSlot = false;
	
	public AspectSlot(Aspect aspect, Supplier<AspectHandler> inventory, int x, int y){
		this.aspect = aspect;
		this.inventory = inventory;
		this.x = x;
		this.y = y;
	}
	
	public int getAmount(){
		if(inventory.get() != null)
			return inventory.get().getCurrentVis(aspect);
		else
			return -1;
	}
	
	public void markDirty(){
	
	}
}