package net.arcanamod.containers;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.StoreSlotAspect;
import net.arcanamod.aspects.IAspectHandler;

import java.util.function.Supplier;

public class AspectStoreSlot extends AspectSlot{
	
	Supplier<IAspectHandler> returnInv;
	StoreSlotAspect holder;
	
	public AspectStoreSlot(Supplier<IAspectHandler> returnInv, int x, int y){
		super(null, null, x, y, true);
		this.returnInv = returnInv;
		holder = new StoreSlotAspect(10);
	}
	
	public AspectStoreSlot(Supplier<IAspectHandler> returnInv, int x, int y, int capacity){
		super(null, null, x, y, true);
		this.returnInv = returnInv;
		holder = new StoreSlotAspect(capacity);
	}
	
	public Supplier<IAspectHandler> getInventory(){
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
	
	public IAspectHandler getHolder(){
		return holder;
	}
}