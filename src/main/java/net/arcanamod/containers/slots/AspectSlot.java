package net.arcanamod.containers.slots;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.handlers.AspectHandler;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class AspectSlot {
	protected Aspect aspect = Aspects.EMPTY;
	private final Supplier<AspectHandler> inventory;
	
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
	
	public AspectSlot(@Nonnull Aspect aspect, @Nonnull Supplier<AspectHandler> inventory, int x, int y) {
		this.setAspect(aspect);
		this.inventory = inventory;
		this.x = x;
		this.y = y;
	}
	
	public AspectSlot(@Nonnull Aspect aspect, Supplier<AspectHandler> inventory, int x, int y, boolean storeSlot) {
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
		if(symbolic)
			return 1;
		AspectHandler handler = getInventory().get();
		if(handler != null) {
			return (float)handler.streamAllHoldersContaining(aspect)
					.mapToDouble(holder -> holder.getStack().getAmount())
					.sum();
		} else {
			return -1;
		}
	}
	
	public void onChange() {
		if(storeSlot && getAmount() == 0)
			aspect = Aspects.EMPTY;
	}
	
	public Supplier<AspectHandler> getInventory(){
		return inventory;
	}
	
	public Aspect getAspect() {
		if(aspect == null)
			return Aspects.EMPTY;
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
	public float drain(@Nonnull Aspect aspect, float amount) {
		float result = 0;
		if(symbolic)
			result = amount;
		else{
			AspectHandler handler = getInventory().get();
			if(handler != null)
				result = handler.drain(aspect, amount);
			onChange();
		}
		return result;
	}
	
	/**
	 * Inserts into this slot's underlying container, updates this slot, and returns the result.
	 *
	 * @return The result of inserting into the underlying inventory.
	 */
	public float insert(@Nonnull Aspect aspect, float amount) {
		float result = amount;
		if(!symbolic){
			AspectHandler handler = getInventory().get();
			if(handler != null)
				result = handler.insert(aspect, amount);
			onChange();
		}
		return result;
	}
	
	public void onClose() {
	}
}