package net.arcanamod.aspects.handlers;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.Aspects;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface AspectHandler2 extends INBTSerializable<CompoundNBT>{
	
	// Returns the list of holders.
	List<AspectHolder2> getHolders();
	
	// Returns the number of holders in this handler.
	default int countHolders(){
		return getHolders().size();
	}
	// Checks if there is a non-null holder at that index.
	default boolean hasHolder(int index){
		return countHolders() > index && getHolders().get(index) != null;
	}
	// Returns the holder at that index, or null if it's out of range.
	default AspectHolder2 getHolder(int index){
		return hasHolder(index) ? getHolders().get(index) : null;
	}
	// Returns the first holder that matches the condition, or null if there are none.
	default AspectHolder2 findFirstHolderMatching(Predicate<AspectHolder2> test){
		AspectHolder2 ret = null;
		for(AspectHolder2 holder : getHolders())
			if(test.test(holder)){
				ret = holder;
				break;
			}
		return ret;
	}
	// Returns the first non-empty holder, or null if there are none.
	default AspectHolder2 findFirstFullHolder(){
		return findFirstHolderMatching(holder -> holder.getStack().getAmount() > 0);
	}
	// Returns the first holder containing the given aspect, or null if there are none.
	default AspectHolder2 findFirstHolderContaining(Aspect aspect){
		return findFirstHolderMatching(holder -> holder.getStack().getAspect() == aspect);
	}
	// Returns all holders containing a particular aspect.
	default List<AspectHolder2> getAllHoldersContaining(Aspect aspect){
		return getHolders().stream()
				.filter(holder -> holder.getStack().getAspect() == aspect)
				.collect(Collectors.toList());
	}
	
	// Clears a holder (if it exists) and sets its size (and whitelist if specified).
	default void resetHolder(int index, int size, List<Aspect> whitelist){
		if(hasHolder(index)){
			resetHolder(index, size);
			getHolder(index).setWhitelist(whitelist);
		}
	}
	default void resetHolder(int index, int size){
		if(hasHolder(index)){
			AspectHolder2 holder2 = getHolder(index);
			holder2.empty();
			holder2.setCapacity(size);
		}
	}
	
	// Removes all handlers and adds empty holders with a given size (and whitelist if specified).
	default void initHolders(int sizes, int numHolders){
		getHolders().clear();
		for(int i = 0; i < numHolders; i++){
			AspectHolder2 holder2 = new AspectCell2();
			holder2.setCapacity(sizes);
			getHolders().add(holder2);
		}
	}
	default void initHolders(int sizes, int numHolders, List<Aspect> whitelist){
		initHolders(sizes, numHolders);
		for(AspectHolder2 holder : getHolders())
			holder.setWhitelist(whitelist);
	}
	
	// Inserts some or all of the input AspectStack into this AspectHandler, returning the amount that is actually inserted.
	// Aspects will try to input into holders with that aspect first, then empty holders whitelisted to that aspect, then voiding holders, then non-whitelisted empty holders.
	// Parts of the input stack may be inserted into different holders.
	// If simulate is set to true, holders will not be mutated; the amount that *would* be inserted is returned.
	default float insert(AspectStack stack){
		return insert(stack.getAspect(), stack.getAmount());
	}
	default float insert(Aspect in, float amount){
		int transferred = 0;
		if(amount > 0){
			List<AspectHolder2> holders = new ArrayList<>(getHolders());
			// move void cells, then empty cells to the end
			holders.sort(VisUtils.INPUT_PRIORITY_SORTER2);
			for(AspectHolder2 toHolder : holders){
				if(amount == 0)
					break;
				if((toHolder.getStack().getAspect() == in || toHolder.getStack().getAspect() == Aspects.EMPTY) && toHolder.canStore(in))
					if(toHolder.canInsert() && (toHolder.getCapacity() > toHolder.getStack().getAmount() || toHolder.voids())){
						float toInsert = (float)Math.floor(amount);
						if(!toHolder.voids())
							toInsert = Math.min(toInsert, toHolder.getCapacity() - toHolder.getStack().getAmount());
						if(toInsert >= amount){
							transferred += amount;
							amount = 0;
						}else{
							transferred += toInsert;
							amount -= toInsert;
						}
						toHolder.insert(toInsert, false);
					}
			}
		}
		return transferred;
	}
	
	// Attempts to remove the input AspectStack from this AspectHandler, returning the amount that is actually drained.
	// Holders will be drained in index order. Multiple holders may be drained.
	// If simulate is set to true, holders will not be mutated; the amount that *would* be drained is returned.
	default float drain(AspectStack stack){
		return drain(stack.getAspect(), stack.getAmount());
	}
	default float drain(Aspect aspect, float amount){
		float transferred = 0;
		if(amount > 0)
			for(AspectHolder2 holder : getHolders()){
				if(amount == 0)
					break;
				if(holder.getStack().getAspect() == aspect){
					float drained = holder.drain(amount, false);
					amount -= drained;
					transferred += drained;
				}
			}
		return transferred;
	}
	
	// Attempts to remove an amount of aspects from this AspectHandler.
	// The aspect in the first non-empty holder is used. Otherwise, this acts the same as drain.
	// If every holder is empty, returns an empty stack.
	default AspectStack drainAny(float amount){
		Aspect aspect = findFirstFullHolder().getStack().getAspect();
		return new AspectStack(aspect, drain(aspect, amount));
	}
	
	// Serialization
	CompoundNBT serializeNBT();
	void deserializeNBT(CompoundNBT data);
}