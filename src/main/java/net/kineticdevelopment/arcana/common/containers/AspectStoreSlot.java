package net.kineticdevelopment.arcana.common.containers;

import net.kineticdevelopment.arcana.core.aspects.Aspect;
import net.kineticdevelopment.arcana.core.aspects.AspectHandler;
import net.kineticdevelopment.arcana.core.aspects.StoreSlotVis;

import java.util.function.Supplier;

public class AspectStoreSlot extends AspectSlot{
	
	Supplier<AspectHandler> returnInv;
	StoreSlotVis holder;
	
	public AspectStoreSlot(Supplier<AspectHandler> returnInv, int x, int y){
		super(null, null, x, y, true);
		this.returnInv = returnInv;
		holder = new StoreSlotVis(10);
	}
	
	public AspectStoreSlot(Supplier<AspectHandler> returnInv, int x, int y, int capacity){
		super(null, null, x, y, true);
		this.returnInv = returnInv;
		holder = new StoreSlotVis(capacity);
	}
	
	public Supplier<AspectHandler> getInventory(){
		return () -> holder;
	}
	
	public void onClose(){
		super.onClose();
		if(returnInv != null && returnInv.get() != null && holder.stored != null)
			holder.getContainedAspects().forEach(aspect -> returnInv.get().insert(aspect, holder.getCurrentVis(aspect), false));
	}
	
	public Aspect getAspect(){
		return holder.stored;
	}
	
	public void setAspect(Aspect aspect){
		if(holder != null)
			holder.stored = aspect;
	}
	
	public int getAmount(){
		if(holder != null)
			return holder.held;
		else
			return 0;
	}
	
	public boolean shouldShowAmount(){
		return holder.getCapacity() > 1;
	}
	
	public AspectHandler getHolder(){
		return holder;
	}
}