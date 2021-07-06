package net.arcanamod.containers.slots;

import net.arcanamod.aspects.*;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class AspectSlot {
	private Aspect aspect = Aspects.EMPTY;
	private final Supplier<IAspectHandler> inventory;
	
	public int x, y;
	
	/**
	 * If false, this slot will not render and cannot be interacted with. This is *not* validated
	 * server-side, and can safely be changed on the client; in other words, any invisible slots can *theoretically*
	 * be accessed by clients.
	 */
	public boolean visible = true;
	
	/**
	 * If true, this slot will act similar to the creative menu. The slot will not display its capacity and
	 * aspects can be freely taken out.
	 */
	public boolean symbolic = false;
	
	/**
	 * If true, this slot will act more like an item stack: when empty, any aspect can be inserted
	 * and it will change to that aspect, and when fully drained it will lose its aspect.
	 */
	public boolean storeSlot = false;

	public String description = "";
	
	public AspectSlot(@Nonnull Aspect aspect, @Nonnull Supplier<IAspectHandler> inventory, int x, int y) {
		this.setAspect(aspect);
		this.inventory = inventory;
		this.x = x;
		this.y = y;
	}
	
	public AspectSlot(@Nonnull Aspect aspect, Supplier<IAspectHandler> inventory, int x, int y, boolean storeSlot) {
		this.setAspect(aspect);
		this.inventory = inventory;
		this.x = x;
		this.y = y;
		this.storeSlot = storeSlot;
	}
	
	public void setSymbolic(boolean state) {
		symbolic = state;
	}
	
	public float getAmount() {
		if(symbolic) {
			return 1;
		}
		if(getInventory().get() != null) {
			int amount = 0;
			int[] aspectIndexes = getInventory().get().findIndexesFromAspectInHolders(getAspect());
			for(int index : aspectIndexes){
				amount += getInventory().get().getHolder(index).getCurrentVis();
			}
			return amount;
		} else {
			return -1;
		}
	}
	
	public void onChange() {
		if(storeSlot && getAmount() == 0)
			aspect = Aspects.EMPTY;
	}
	
	public Supplier<IAspectHandler> getInventory(){
		return inventory;
	}
	
	public Aspect getAspect() {
		if(aspect == null)
			return Aspects.EMPTY; // Quick fix. TODO: Fix null problems
		return aspect;
	}
	
	public void setAspect(@Nonnull Aspect aspect) {
		this.aspect = aspect;
	}
	
	public boolean isSymbolic() {
		return symbolic;
	}
	
	public boolean shouldShowAmount() {
		return !symbolic;
	}
	
	/**
	 * Draws from this slots underlying container, updates this slot, and returns the result.
	 *
	 * @return The result of drawing from the underlying inventory.
	 */
	public float drain(@Nonnull Aspect aspect, float amount, boolean simulate) {
		float result = 0;
		if(symbolic){
			result = amount;
		}else{
			if(getInventory().get() != null){
				int[] aspectIndexes = getInventory().get().findIndexesFromAspectInHolders(getAspect());
				result = getInventory().get().drain(aspectIndexes[0], new AspectStack(aspect, amount), simulate);
			}
			onChange();
		}
		return result;
	}
	
	/**
	 * Inserts into this slot's underlying container, updates this slot, and returns the result.
	 *
	 * @return The result of inserting into the underlying inventory.
	 */
	public float insert(@Nonnull Aspect aspect, float amount, boolean simulate) {
		float result = amount;
		if(!symbolic){
			if(getInventory().get() != null){
				
				int[] aspectIndexes = getInventory().get().findIndexesFromAspectInHolders(getAspect());
				
				boolean isInserted = false;
				if(aspectIndexes.length == 0){
					result = getInventory().get().insert(AspectUtils.getEmptyCell(getInventory().get()), new AspectStack(aspect, amount), simulate);
					isInserted = true;
				}
				for(int index : aspectIndexes){
					if(getInventory().get().getHolder(index).getCurrentVis() < getInventory().get().getHolder(index).getCapacity()){
						result = getInventory().get().insert(index, new AspectStack(aspect, amount), simulate);
						isInserted = true;
						break;
					}
				}
				if(!isInserted)
					result = getInventory().get().insert(AspectUtils.getEmptyCell(getInventory().get()), new AspectStack(aspect, amount), simulate);
			}
			onChange();
		}
		return result;
	}
	
	public void onClose() {
	}
}