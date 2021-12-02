package net.arcanamod.containers.slots;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.handlers.AspectBattery;
import net.arcanamod.aspects.handlers.AspectHandler;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class AspectStoreSlot extends AspectSlot{
	
	Supplier<AspectHandler> returnInv;
	AspectHandler holder;
	
	public AspectStoreSlot(Supplier<AspectHandler> returnInv, int x, int y){
		super(Aspects.EMPTY, null, x, y, true);
		this.returnInv = returnInv;
		holder = new AspectBattery();
		holder.initHolders(100, 1);
	}
	
	public AspectStoreSlot(Supplier<AspectHandler> returnInv, int x, int y, int capacity){
		super(Aspects.EMPTY, null, x, y, true);
		this.returnInv = returnInv;
		holder = new AspectBattery();
		holder.initHolders(capacity, 1);
	}
	
	public Supplier<AspectHandler> getInventory(){
		return () -> holder;
	}
	
	public void onClose(){
		super.onClose();
		if(returnInv != null && returnInv.get() != null && holder.getHolder(0).getStack().getAspect() != null)
			holder.getHolders().forEach(holder -> returnInv.get().insert(holder.getStack()));
	}
	
	public Aspect getAspect(){
		return holder.getHolder(0).getStack().getAspect();
	}
	
	public void setAspect(@Nonnull Aspect aspect){
		if(holder != null)
			holder.getHolder(0).getStack().setAspect(aspect);/*.setStack(new AspectStack(aspect, holder.getHolder(0).getStack().getAmount()))*/;
	}
	
	public float getAmount(){
		if(holder != null)
			return holder.getHolder(0).getStack().getAmount();
		else
			return 0;
	}
	
	@Override
	public boolean shouldShowAmount(){
		return holder.getHolder(0).getCapacity() > 1;
	}
	
	public AspectHandler getHolder(){
		return holder;
	}
}