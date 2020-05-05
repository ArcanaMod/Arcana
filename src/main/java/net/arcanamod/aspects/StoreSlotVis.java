package net.arcanamod.aspects;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Simple implementation of {@link VisHandler} that can store a single aspect up to a set amount.
 */
public class StoreSlotVis implements VisHandler, ICapabilityProvider{
	
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing){
		return capability == VisHandlerCapability.ASPECT_HANDLER;
	}
	
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing){
		return capability == VisHandlerCapability.ASPECT_HANDLER ? LazyOptional.of(() -> (T)this) : LazyOptional.empty();
	}
	
	public Aspect stored;
	public int held;
	int capacity;
	
	public StoreSlotVis(){
		this(100);
	}
	
	public StoreSlotVis(int capacity){
		this.capacity = capacity;
	}
	
	public int insert(Aspect aspect, int amount, boolean simulate){
		Aspect _stored = stored;
		if(_stored == null)
			_stored = aspect;
		if(_stored != aspect)
			return amount;
		int capacityRemaining = getCapacity(aspect) - getCurrentVis(aspect);
		if(amount <= capacityRemaining){
			if(!simulate){
				held = getCurrentVis(aspect) + amount;
				stored = _stored;
			}
			return 0;
		}else{
			if(!simulate){
				held = getCapacity(aspect);
				stored = _stored;
			}
			return amount - capacityRemaining;
		}
	}
	
	public int getCurrentVis(Aspect aspect){
		if(aspect != stored)
			return 0;
		else
			return held;
	}
	
	public int drain(Aspect aspect, int amount, boolean simulate){
		int vis = getCurrentVis(aspect);
		if(amount >= vis){
			if(!simulate){
				held = 0;
				stored = null;
			}
			return vis;
		}else{
			if(!simulate)
				held = vis - amount;
			return amount;
		}
	}
	
	public boolean canInsert(Aspect aspect){
		return aspect == stored && held < capacity;
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
		return new LinkedHashSet<>(Aspect.aspects);
	}
	
	public Set<Aspect> getContainedAspects(){
		return Collections.singleton(stored);
	}
	
	public CompoundNBT serializeNBT(){
		CompoundNBT compound = new CompoundNBT();
		CompoundNBT storedAspects = new CompoundNBT();
		storedAspects.putInt(stored != null ? stored.name().toLowerCase() : "null", held);
		compound.put("stored", storedAspects);
		compound.putInt("capacity", capacity);
		return compound;
	}
	
	public void deserializeNBT(CompoundNBT data){
		CompoundNBT storedAspects = data.getCompound("stored");
		for(String s : storedAspects.keySet()){
			if(s != null && !s.equals("null")){
				stored = Aspect.valueOf(s.toUpperCase());
				held = storedAspects.getInt(s);
			}
		}
		capacity = data.getInt("capacity");
	}
}