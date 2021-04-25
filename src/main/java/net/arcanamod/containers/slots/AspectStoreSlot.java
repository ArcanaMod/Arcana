package net.arcanamod.containers.slots;

import net.arcanamod.aspects.*;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class AspectStoreSlot extends AspectSlot{
	
	Supplier<IAspectHandler> returnInv;
	StoreSlotAspect holder;
	
	public AspectStoreSlot(Supplier<IAspectHandler> returnInv, int x, int y){
		super(null, null, x, y, true);
		this.returnInv = returnInv;
		holder = new StoreSlotAspect(100);
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
			holder.getContainedAspects().forEach(aspect -> returnInv.get().insert(0,new AspectStack(aspect, holder.getHolder(0).getCurrentVis()), false));
	}
	
	public Aspect getAspect(){
		return holder.stored;
	}
	
	public void setAspect(@Nonnull Aspect aspect){
		if(holder != null)
			holder.stored = aspect;
	}
	
	public float getAmount(){
		if(holder != null)
			return holder.held;
		else
			return 0;
	}

	@Override
	public boolean shouldShowAmount() {
		return holder.getCapacity() > 1;
	}
	
	public IAspectHandler getHolder(){
		return holder;
	}
}