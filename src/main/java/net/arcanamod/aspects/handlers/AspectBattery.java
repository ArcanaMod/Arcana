package net.arcanamod.aspects.handlers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
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
		ListNBT storedCells = new ListNBT();
		holders.forEach(holder -> storedCells.add(holder.serializeNBT()));
		compound.put("holders", storedCells);
		return compound;
	}
	
	public void deserializeNBT(CompoundNBT data){
		ListNBT cells = data.getList("holders", Constants.NBT.TAG_COMPOUND);
		holders.clear();
		for(INBT icell : cells){
			AspectCell cell = AspectCell.fromNbt((CompoundNBT)icell);
			holders.add(cell);
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