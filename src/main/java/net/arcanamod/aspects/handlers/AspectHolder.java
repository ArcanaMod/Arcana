package net.arcanamod.aspects.handlers;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.Aspects;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.function.Consumer;

public interface AspectHolder extends INBTSerializable<CompoundNBT>{
	
	// Returns the stored stack.
	AspectStack getStack();
	// Returns the capacity of this holder.
	float getCapacity();
	// If this holder has a whitelist, returns the whitelist; otherwise returns null.
	List<Aspect> getWhitelist();
	// Returns whether this holder accepts aspects when full (and voids excess aspects).
	boolean voids();
	// Returns whether this holder can be inserted into.
	boolean canInsert();
	// Returns the (nonnull) callback to be run when inserting into this holder when full.
	Consumer<Float> overfillingCallback();
	
	// Sets the contained stack.
	void setStack(AspectStack stack);
	// Sets the maximum amount of aspects that this holder can contain.
	void setCapacity(float capacity);
	// Sets the whitelist of holder.
	void setWhitelist(List<Aspect> whitelist);
	// Sets whether this holder should void excess aspects.
	void setVoids(boolean voids);
	// Sets whether this holder can be inserted into.
	void setCanInsert(boolean canInsert);
	// Sets a callback to be run when inserting into this holder when full. Passing in null will set it to `__ -> {}`.
	void setOverfillingCallback(Consumer<Float> callback);
	
	// Returns whether this holder can contain that aspect.
	// False if the aspect is empty or dummy, or if this holder has a whitelist that doesn't contain that aspect.
	default boolean canStore(Aspect aspect){
		if(aspect == Aspects.EMPTY)
			return false;
		return getWhitelist() == null || getWhitelist().contains(aspect);
	}
	// Returns the contained aspect, if there is one; otherwise, returns the first aspect in the whitelist if there is one; otherwise returns null.
	default Aspect getLabelAspect(){
		if(getStack().getAspect() != Aspects.EMPTY)
			return getStack().getAspect();
		if(getWhitelist() != null && getWhitelist().size() > 0)
			return getWhitelist().get(0);
		return null;
	}
	
	// Empties this holder, setting the aspect to empty and the amount to 0.
	default void empty(){
		setStack(AspectStack.EMPTY);
	}
	// Removes the whitelist, allowing any aspect to be inserted.
	default void removeWhitelist(){
		setWhitelist(null);
	}
	
	// Removes an amount of aspects from this holder, returning the amount that was removed.
	// If simulate is true, the contents of this holder will no be changed; the amount that *would* be removed is returned.
	default float drain(float amount, boolean simulate){
		// if amount >= left, return left & left = 0
		// if amount < left, return amount & left = left - amount
		float vis = getStack().getAmount();
		if(amount >= vis){
			if(!simulate)
				empty();
			return vis;
		}else{
			if(!simulate)
				setStack(new AspectStack(getStack().getAspect(), vis - amount));
			return amount;
		}
	}
	// Inserts an amount of aspects from this holder, returning the left-over amount.
	// If simulate is true, the contents of this holder will no be changed; the amount that *would* be left-over is returned.
	default float insert(float amount, boolean simulate){
		float capacityRemaining = getCapacity() == -1 ? -1 : Math.max(getCapacity() - getStack().getAmount(), 0);
		if(capacityRemaining == -1 || amount <= capacityRemaining){
			if(!simulate)
				setStack(new AspectStack(getStack().getAspect(), getStack().getAmount() + amount));
			return 0;
		}else{
			if(!simulate)
				setStack(new AspectStack(getStack().getAspect(), getCapacity()));
			float ret = amount - capacityRemaining;
			overfillingCallback().accept(ret);
			if(voids())
				return 0;
			else
				return ret;
		}
	}
	default float insert(float amount, Aspect aspect, boolean simulate){
		//Aspect old = getStack().getAspect();
		//getStack().setAspect(aspect);
		AspectStack old = getStack();
		setStack(new AspectStack(aspect, old.getAmount()));
		float ret = insert(amount, simulate);
		if(simulate)
			setStack(old);
		return ret;
	}
	default float insert(AspectStack stack, boolean simulate){
		return insert(stack.getAmount(), stack.getAspect(), simulate);
	}
	
	// Serialization
	CompoundNBT serializeNBT();
	void deserializeNBT(CompoundNBT data);
}