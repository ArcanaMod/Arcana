package net.arcanamod.aspects.handlers;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface AspectHandler extends INBTSerializable<CompoundNBT>{
	
	/** Returns the list of holders. */
	List<AspectHolder> getHolders();
	
	/** Returns the number of holders in this handler. */
	default int countHolders(){
		return getHolders().size();
	}
	/** Checks if there is a non-null holder at that index. */
	default boolean hasHolder(int index){
		return countHolders() > index && getHolders().get(index) != null;
	}
	/** Returns the holder at that index, or null if it's out of range. */
	default AspectHolder getHolder(int index){
		return hasHolder(index) ? getHolders().get(index) : null;
	}
	/** Returns the first holder that matches the condition, or null if there are none. */
	default AspectHolder findFirstHolderMatching(Predicate<AspectHolder> test){
		AspectHolder ret = null;
		for(AspectHolder holder : getHolders())
			if(test.test(holder)){
				ret = holder;
				break;
			}
		return ret;
	}
	/** Returns the first non-empty holder, or null if there are none. */
	default AspectHolder findFirstFullHolder(){
		return findFirstHolderMatching(holder -> holder.getStack().getAmount() > 0);
	}
	/** Returns the first empty holder, or null if there are none. */
	default AspectHolder findFirstEmptyHolder(){
		return findFirstHolderMatching(holder -> holder.getStack().isEmpty());
	}
	/** Returns the first holder containing the given aspect, or null if there are none. */
	default AspectHolder findFirstHolderContaining(Aspect aspect){
		return findFirstHolderMatching(holder -> holder.getStack().getAspect() == aspect);
	}
	/** Returns all holders containing a particular aspect. */
	default List<AspectHolder> getAllHoldersContaining(Aspect aspect){
		return getHolders().stream()
				.filter(holder -> holder.getStack().getAspect() == aspect)
				.collect(Collectors.toList());
	}
	/** Returns a stream of all holders containing a particular aspect. */
	default Stream<AspectHolder> streamAllHoldersContaining(Aspect aspect){
		return getHolders().stream()
				.filter(holder -> holder.getStack().getAspect() == aspect);
	}
	
	/** Clears a holder (if it exists) and sets its size and whitelist. */
	default void resetHolder(int index, int size, List<Aspect> whitelist){
		if(hasHolder(index)){
			resetHolder(index, size);
			getHolder(index).setWhitelist(whitelist);
		}
	}
	/** Clears a holder (if it exists) and sets its size. */
	default void resetHolder(int index, int size){
		if(hasHolder(index)){
			AspectHolder holder2 = getHolder(index);
			holder2.empty();
			holder2.setCapacity(size);
		}
	}
	
	/** Removes all handlers and adds empty holders with a given size. */
	default void initHolders(int sizes, int numHolders){
		getHolders().clear();
		for(int i = 0; i < numHolders; i++){
			AspectHolder holder2 = new AspectCell();
			holder2.setCapacity(sizes);
			getHolders().add(holder2);
		}
	}
	/** Removes all handlers and adds empty holders with a given size and whitelist. */
	default void initHolders(int sizes, int numHolders, List<Aspect> whitelist){
		initHolders(sizes, numHolders);
		for(AspectHolder holder : getHolders())
			holder.setWhitelist(whitelist);
	}
	
	/**
	 Inserts some or all of the input AspectStack into this AspectHandler, returning the amount that is actually inserted.
	 Aspects will try to input into holders with that aspect first, then empty holders whitelisted to that aspect, then voiding holders, then non-whitelisted empty holders.
	 Parts of the input stack may be inserted into different holders.
	*/
	default float insert(AspectStack stack){
		return insert(stack.getAspect(), stack.getAmount());
	}
	/**
	 Inserts some or all of the input AspectStack into this AspectHandler, returning the amount that is actually inserted.
	 Aspects will try to input into holders with that aspect first, then empty holders whitelisted to that aspect, then voiding holders, then non-whitelisted empty holders.
	 Parts of the input stack may be inserted into different holders.
	 */
	default float insert(Aspect in, float amount){
		int transferred = 0;
		if(amount > 0){
			List<AspectHolder> holders = new ArrayList<>(getHolders());
			// move void cells, then empty cells to the end
			holders.sort(VisUtils.INPUT_PRIORITY_SORTER2);
			for(AspectHolder toHolder : holders){
				if(amount == 0)
					break;
				if((toHolder.getStack().getAspect() == in || toHolder.getStack().isEmpty()) && toHolder.canStore(in))
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
						toHolder.insert(toInsert, in, false);
					}
			}
		}
		return transferred;
	}
	
	/**
	 Attempts to remove the input AspectStack from this AspectHandler, returning the amount that is actually drained.
	 Holders will be drained in index order. Multiple holders may be drained.
	*/
	default float drain(AspectStack stack){
		return drain(stack.getAspect(), stack.getAmount());
	}
	/**
	 Attempts to remove the input AspectStack from this AspectHandler, returning the amount that is actually drained.
	 Holders will be drained in index order. Multiple holders may be drained.
	 */
	default float drain(Aspect aspect, float amount){
		float transferred = 0;
		if(amount > 0)
			for(AspectHolder holder : getHolders()){
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
	
	/**
	 Attempts to remove an amount of aspects from this AspectHandler.
	 The aspect in the first non-empty holder is used. Otherwise, this acts the same as drain.
	 If every holder is empty, returns an empty stack.
	*/
	default AspectStack drainAny(float amount){
		AspectHolder holder = findFirstFullHolder();
		if(holder == null)
			return AspectStack.EMPTY;
		Aspect aspect = holder.getStack().getAspect();
		return new AspectStack(aspect, drain(aspect, amount));
	}
	
	default void clear(){
		getHolders().clear();
	}
	
	// Serialization
	CompoundNBT serializeNBT();
	void deserializeNBT(CompoundNBT data);
	
	// Statics
	static LazyOptional<AspectHandler> getOptional(@Nullable ICapabilityProvider holder){
		if(holder == null)
			return LazyOptional.empty();
		return holder.getCapability(AspectHandlerCapability.ASPECT_HANDLER, null);
	}
	
	@SuppressWarnings("ConstantConditions")
	@Nullable
	static AspectHandler getFrom(@Nullable ICapabilityProvider holder){
		if(holder == null)
			return null;
		return holder.getCapability(AspectHandlerCapability.ASPECT_HANDLER, null).orElse(null);
	}
}