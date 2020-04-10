package net.kineticdevelopment.arcana.common.containers;

import net.kineticdevelopment.arcana.core.aspects.Aspect;
import net.kineticdevelopment.arcana.core.aspects.AspectHandler;

import java.util.function.Supplier;

public class AspectSlot{
	
	private Aspect aspect;
	private final Supplier<AspectHandler> inventory;
	
	public int x, y;
	public boolean visible = true;
	
	/**
	 * If true, this slot will act more like an item stack: when empty, any aspect can be inserted
	 * and it will change to that aspect, and when fully drained it will lose its aspect.
	 */
	public boolean storeSlot = false;
	
	public AspectSlot(Aspect aspect, Supplier<AspectHandler> inventory, int x, int y){
		this.setAspect(aspect);
		this.inventory = inventory;
		this.x = x;
		this.y = y;
	}
	
	public AspectSlot(Aspect aspect, Supplier<AspectHandler> inventory, int x, int y, boolean storeSlot){
		this.setAspect(aspect);
		this.inventory = inventory;
		this.x = x;
		this.y = y;
		this.storeSlot = storeSlot;
	}
	
	public int getAmount(){
		if(getInventory().get() != null)
			return getInventory().get().getCurrentVis(getAspect());
		else
			return -1;
	}
	
	public void sync(){
		if(storeSlot && getAmount() == 0)
			aspect = null;
	}
	
	public Supplier<AspectHandler> getInventory(){
		return inventory;
	}
	
	public Aspect getAspect(){
		return aspect;
	}
	
	public void setAspect(Aspect aspect){
		this.aspect = aspect;
	}
	
	/**
	 * Draws from this slots underlying container, updates this slot, and returns the result.
	 *
	 * @return The result of drawing from the underlying inventory.
	 */
	public int drain(Aspect aspect, int amount, boolean simulate){
		int result = 0;
		if(getInventory().get() != null)
			result = getInventory().get().drain(aspect, amount, simulate);
		sync();
		return result;
	}
	
	/**
	 * Inserts into this slot's underlying container, updates this slot, and returns the result.
	 *
	 * @return The result of inserting into the underlying inventory.
	 */
	public int insert(Aspect aspect, int amount, boolean simulate){
		int result = amount;
		if(getInventory().get() != null)
			result = getInventory().get().insert(aspect, amount, simulate);
		sync();
		return result;
	}
}