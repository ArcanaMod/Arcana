package net.arcanamod.aspects.handlers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AspectBattery implements AspectHandler, ICapabilityProvider{
	
	private List<AspectHolder> holders = new ArrayList<>();
	
	public List<AspectHolder> getHolders(){
		return holders;
	}
	
	public CompoundNBT serializeNBT(){
		CompoundNBT compound = new CompoundNBT();
		CompoundNBT storedCells = new CompoundNBT();
		holders.forEach(holder -> storedCells.put("holder_" + holders.indexOf(holder), holder.serializeNBT()));
		compound.put("holders", storedCells);
		return compound;
	}
	
	public void deserializeNBT(CompoundNBT data){
		CompoundNBT storedCells = data.getCompound("holders");
		int i = 0;
		for(String s : storedCells.keySet()){
			if(i >= holders.size())
				holders.add(AspectCell.fromNbt(storedCells.getCompound(s)));
			else
				holders.set(Integer.parseInt(s.substring(7)), AspectCell.fromNbt(storedCells.getCompound(s)));
			i++;
		}
	}
	
	@SuppressWarnings("UnusedReturnValue")
	public static AspectBattery merge(AspectBattery defaultBattery, AspectBattery... batteries){
		for(AspectBattery battery : batteries)
			defaultBattery.getHolders().addAll(battery.getHolders());
		return defaultBattery;
	}
	
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing){
		return capability == AspectHandlerCapability.ASPECT_HANDLER;
	}
	
	@SuppressWarnings("unchecked")
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing){
		return capability == AspectHandlerCapability.ASPECT_HANDLER ? LazyOptional.of(() -> (T)this) : LazyOptional.empty();
	}
}