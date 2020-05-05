package net.arcanamod.aspects;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Simple implementation of {@link VisHandler} that stores any aspect up to the given amount.
 */
public class VisBattery implements VisHandler, ICapabilityProvider{
	
	private Map<Aspect, Integer> stored = new HashMap<>();
	private int capacity;
	
	public VisBattery(){
		this(100);
	}
	
	public VisBattery(int capacity){
		this.capacity = capacity;
	}
	
	public int insert(Aspect aspect, int amount, boolean simulate){
		int capacityRemaining = getCapacity(aspect) - getCurrentVis(aspect);
		if(amount <= capacityRemaining){
			if(!simulate)
				stored.put(aspect, getCurrentVis(aspect) + amount);
			return 0;
		}else{
			if(!simulate)
				stored.put(aspect, getCapacity(aspect));
			return amount - capacityRemaining;
		}
	}
	
	public int drain(Aspect aspect, int amount, boolean simulate){
		// if amount >= left, return left & left = 0
		// if amount < left, return amount & left = left - amount
		int vis = getCurrentVis(aspect);
		if(amount >= vis){
			if(!simulate)
				stored.remove(aspect);
			return vis;
		}else{
			if(!simulate)
				stored.put(aspect, vis - amount);
			return amount;
		}
	}
	
	public int getCurrentVis(Aspect aspect){
		return stored.getOrDefault(aspect, 0);
	}
	
	public boolean canInsert(Aspect aspect){
		return getCurrentVis(aspect) < getCapacity(aspect);
	}
	
	public boolean canStore(Aspect aspect){
		return true;
	}
	
	public int getCapacity(Aspect aspect){
		return capacity;
	}
	
	public int getCapacity(){
		return capacity;
	}
	
	public Set<Aspect> getAllowedAspects(){
		// insertion order matters!
		return new LinkedHashSet<>(Aspect.aspects);
	}
	
	public Set<Aspect> getContainedAspects(){
		return Collections.unmodifiableSet(stored.keySet());
	}
	
	public CompoundNBT serializeNBT(){
		CompoundNBT compound = new CompoundNBT();
		CompoundNBT storedAspects = new CompoundNBT();
		stored.forEach((aspect, amount) -> storedAspects.putInt(aspect.name().toLowerCase(), amount));
		compound.put("stored", storedAspects);
		return compound;
	}
	
	public void deserializeNBT(CompoundNBT data){
		Map<Aspect, Integer> dat = new HashMap<>();
		CompoundNBT storedAspects = data.getCompound("stored");
		for(String s : storedAspects.keySet())
			dat.put(Aspect.valueOf(s.toUpperCase()), storedAspects.getInt(s));
		stored = dat;
	}
	
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing){
		return capability == VisHandlerCapability.ASPECT_HANDLER;
	}
	
	@Nullable
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing){
		return capability == VisHandlerCapability.ASPECT_HANDLER ? LazyOptional.of(() -> (T)this) : LazyOptional.empty();
	}
}