package net.arcanamod.containers;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.VisBattery;
import net.arcanamod.util.Pair;

import javax.annotation.Nullable;

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
		return Aspect.getCompound(Pair.of(leftStoreSlot.getAspect(), rightStoreSlot.getAspect()));
	}
	
	public int drain(Aspect aspect, int amount, boolean simulate){
		return getAspect() != null ? Math.min(leftStoreSlot.drain(leftStoreSlot.getAspect(), amount, simulate), rightStoreSlot.drain(rightStoreSlot.getAspect(), amount, simulate)) : 0;
	}
	
	public int insert(Aspect aspect, int amount, boolean simulate){
		return amount;
	}
}