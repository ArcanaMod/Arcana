package net.arcanamod.containers;

import net.arcanamod.aspects.*;

import java.util.function.Supplier;

public class AspectSlot{
	
	private Aspect aspect;
	private final Supplier<IAspectHandler> inventory;
	
	public int x, y;
	
	/**
	 * If false, this slot will not render and cannot be interacted with. This is *not* validated
	 * server-side, and can safely be changed on the client; in other words, any invisible slots can *theoretically*
	 * be accessed by clients.
	 */
	public boolean visible = true;
	
	/**
	 * If true, this slot will act more like an item stack: when empty, any aspect can be inserted
	 * and it will change to that aspect, and when fully drained it will lose its aspect.
	 */
	public boolean storeSlot = false;
	
	public AspectSlot(Aspect aspect, Supplier<IAspectHandler> inventory, int x, int y){
		this.setAspect(aspect);
		this.inventory = inventory;
		this.x = x;
		this.y = y;
	}
	
	public AspectSlot(Aspect aspect, Supplier<IAspectHandler> inventory, int x, int y, boolean storeSlot){
		this.setAspect(aspect);
		this.inventory = inventory;
		this.x = x;
		this.y = y;
		this.storeSlot = storeSlot;
	}
	
	public int getAmount(){
		if(getInventory().get() != null) {
			IAspectHolder holder = getInventory().get().findAspectInHolders(getAspect());
			if (holder != null)
				return holder.getCurrentVis();
			else return 0;
		}
		else
			return -1;
	}
	
	public void onChange(){
		if(storeSlot && getAmount() == 0)
			aspect = null;
	}
	
	public Supplier<IAspectHandler> getInventory(){
		return inventory;
	}
	
	public Aspect getAspect(){
		return aspect;
	}
	
	public void setAspect(Aspect aspect){
		this.aspect = aspect;
	}
	
	public boolean shouldShowAmount(){
		return true;
	}
	
	/**
	 * Draws from this slots underlying container, updates this slot, and returns the result.
	 *
	 * @return The result of drawing from the underlying inventory.
	 */
	public int drain(Aspect aspect, int amount, boolean simulate){
		int result = 0;
		if(getInventory().get() != null) {
			int aspectIndex = getInventory().get().findIndexFromAspectInHolders(getAspect());
			result = getInventory().get().drain(aspectIndex == -1 ? Aspects.getEmptyCell(getInventory().get()) : aspectIndex,new AspectStack(aspect, amount), simulate); // BUG!!!
		}
		onChange();
		return result;
	}
	
	/**
	 * Inserts into this slot's underlying container, updates this slot, and returns the result.
	 *
	 * @return The result of inserting into the underlying inventory.
	 */
	public int insert(Aspect aspect, int amount, boolean simulate){
		int result = amount;
		if(getInventory().get() != null) {
			int aspectIndex = getInventory().get().findIndexFromAspectInHolders(getAspect());
			result = getInventory().get().insert(aspectIndex == -1 ? Aspects.getEmptyCell(getInventory().get()) : aspectIndex,new AspectStack(aspect, amount), simulate); // BUG!!!
		}
		onChange();
		return result;
	}
	
	public void onClose(){
	}
}