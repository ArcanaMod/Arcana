package net.arcanamod.containers;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.StoreSlotVis;
import net.arcanamod.aspects.VisHandler;

import java.util.function.Supplier;

public class AspectStoreSlot extends AspectSlot{
	
	Supplier<VisHandler> returnInv;
	StoreSlotVis holder;
	
	public AspectStoreSlot(Supplier<VisHandler> returnInv, int x, int y){
		super(null, null, x, y, true);
		this.returnInv = returnInv;
		holder = new StoreSlotVis(10);
	}
	
	public AspectStoreSlot(Supplier<VisHandler> returnInv, int x, int y, int capacity){
		super(null, null, x, y, true);
		this.returnInv = returnInv;
		holder = new StoreSlotVis(capacity);
	}
	
	public Supplier<VisHandler> getInventory(){
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
	
	public VisHandler getHolder(){
		return holder;
	}
}