package net.arcanamod.containers.slots;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.handlers.AspectHandler;
import net.arcanamod.aspects.handlers.AspectHolder;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

public class AspectSlot {
	private Aspect aspect = Aspects.EMPTY;
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
		if(symbolic) {
			return 1;
		}
		if(getInventory().get() != null) {
			/*int amount = 0;
			int[] aspectIndexes = getInventory().get().findIndexesFromAspectInHolders(getAspect());
			for(int index : aspectIndexes)
				amount += getInventory().get().getHolder(index).getCurrentVis();*/
			return (float)getInventory().get().streamAllHoldersContaining(getAspect())
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
				/*int[] aspectIndexes = getInventory().get().findIndexesFromAspectInHolders(getAspect());
				result = getInventory().get().drain(aspectIndexes[0], new AspectStack(aspect, amount), simulate);*/
				getInventory().get().findFirstHolderContaining(getAspect()).drain(amount, simulate);
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
				List<AspectHolder> holders = getInventory().get().getAllHoldersContaining(getAspect());
				boolean isInserted = false;
				if(holders.size() == 0){
					AspectHolder holder = getInventory().get().findFirstEmptyHolder();
					result = holder.insert(amount, aspect, simulate);
					isInserted = true;
				}
				for(AspectHolder holder : holders){
					if(holder.getStack().getAmount() < holder.getCapacity()){
						result = holder.insert(amount, aspect, simulate);
						isInserted = true;
						break;
					}
				}
				if(!isInserted)
					result = getInventory().get().findFirstEmptyHolder().insert(amount, aspect, simulate);
			}
			onChange();
		}
		return result;
	}
	
	public void onClose() {
	}
}