package net.kineticdevelopment.arcana.core.aspects;

import net.kineticdevelopment.arcana.core.aspects.Aspect.AspectType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple implementation of {@link AspectHandler} that stores any aspect up to the given amount.
 */
public class VisBattery implements AspectHandler, ICapabilityProvider{
	
	private Map<AspectType, Integer> stored = new HashMap<>();
	private int capacity;
	
	public VisBattery(){
		this(100);
	}
	
	public VisBattery(int capacity){
		this.capacity = capacity;
	}
	
	public int insert(AspectType aspect, int amount, boolean simulate){
		int left = getCapacity(aspect) - getCurrentVis(aspect);
		if(!simulate)
			stored.put(aspect, getCurrentVis(aspect) + (Math.min(left, amount)));
		return amount - left;
	}
	
	public int drain(AspectType aspect, int amount, boolean simulate){
		// if amount >= left, return left & left = 0
		// if amount < left, return amount & left = left - amount
		int vis = getCurrentVis(aspect);
		if(amount >= vis){
			if(!simulate)
				stored.put(aspect, 0);
			return vis;
		}else{
			if(!simulate)
				stored.put(aspect, vis - amount);
			return amount;
		}
	}
	
	public int getCurrentVis(AspectType aspect){
		return stored.getOrDefault(aspect, 0);
	}
	
	public boolean canInsert(AspectType aspect){
		return getCurrentVis(aspect) < getCapacity(aspect);
	}
	
	public boolean canStore(AspectType aspect){
		return true;
	}
	
	public int getCapacity(AspectType aspect){
		return capacity;
	}
	
	public int getCapacity(){
		return capacity;
	}
	
	public NBTTagCompound serialize(){
		NBTTagCompound compound = new NBTTagCompound();
		stored.forEach((aspect, amount) -> compound.setInteger(aspect.name().toLowerCase(), amount));
		return compound;
	}
	
	public void deserialize(NBTTagCompound data){
		Map<AspectType, Integer> dat = new HashMap<>();
		for(String s : data.getKeySet())
			dat.put(AspectType.valueOf(s), data.getInteger(s));
		stored = dat;
	}
	
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing){
		return capability == AspectHandlerCapability.ASPECT_HANDLER;
	}
	
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing){
		return capability == AspectHandlerCapability.ASPECT_HANDLER ? (T)this : null;
	}
}