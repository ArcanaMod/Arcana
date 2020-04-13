package net.kineticdevelopment.arcana.core.aspects;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Simple implementation of {@link AspectHandler} that can store a single aspect up to a set amount.
 */
public class StoreSlotVis implements AspectHandler, ICapabilityProvider{
	
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing){
		return capability == AspectHandlerCapability.ASPECT_HANDLER;
	}
	
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing){
		return capability == AspectHandlerCapability.ASPECT_HANDLER ? (T)this : null;
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
		System.out.println("Inserting " + aspect + " x" + amount + ": " + simulate + "!");
		/*Aspect _stored = stored;
		if(_stored == null)
			_stored = aspect;
		if(_stored != aspect)
			return amount;
		int ret = amount + held - capacity;
		if(ret < 0)
			ret = 0;
		if(!simulate){
			stored = _stored;
			held = ret;
		}
		return ret;*/
		
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
		System.out.println("Draining " + aspect + " x" + amount + ": " + simulate + "!");
		/*if(stored == null || stored != aspect)
			return 0;
		int ret = Math.min(held, amount);
		if(!simulate){
			held = held - ret;
			if(held <= 0){
				held = 0;
				stored = null;
			}
		}
		return ret;*/
		
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
	
	public NBTTagCompound serializeNBT(){
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagCompound storedAspects = new NBTTagCompound();
		storedAspects.setInteger(stored != null ? stored.name().toLowerCase() : "null", held);
		compound.setTag("stored", storedAspects);
		return compound;
	}
	
	public void deserializeNBT(NBTTagCompound data){
		NBTTagCompound storedAspects = data.getCompoundTag("stored");
		for(String s : storedAspects.getKeySet()){
			if(s != null && !s.equals("null")){
				stored = Aspect.valueOf(s.toUpperCase());
				held = storedAspects.getInteger(s);
			}
		}
	}
}