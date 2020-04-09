package net.kineticdevelopment.arcana.common.containers;

import net.kineticdevelopment.arcana.core.aspects.Aspect;
import net.kineticdevelopment.arcana.core.aspects.VisBattery;

import javax.annotation.Nullable;

import static net.kineticdevelopment.arcana.utilities.Pair.of;

class CombinatorAspectSlot extends AspectSlot{
	
	private AspectSlot leftStoreSlot;
	private AspectSlot rightStoreSlot;
	
	public CombinatorAspectSlot(AspectSlot leftStoreSlot, AspectSlot rightStoreSlot, int x, int y){
		super(null, () -> new VisBattery(1), x, y);
		this.leftStoreSlot = leftStoreSlot;
		this.rightStoreSlot = rightStoreSlot;
	}
	
	public int getAmount(){
		return getAspect() != null ? Math.min(leftStoreSlot.getAmount(), rightStoreSlot.getAmount()) : 0;
	}
	
	@Nullable
	public Aspect getAspect(){
		return Aspect.getCompound(of(leftStoreSlot.getAspect(), rightStoreSlot.getAspect()));
	}
	
	public int drain(Aspect aspect, int amount, boolean simulate){
		return getAspect() != null ? Math.min(leftStoreSlot.drain(leftStoreSlot.getAspect(), amount, simulate), rightStoreSlot.drain(rightStoreSlot.getAspect(), amount, simulate)) : 0;
	}
	
	public int insert(Aspect aspect, int amount, boolean simulate){
		return amount;
	}
}
