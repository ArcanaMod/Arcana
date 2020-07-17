package net.arcanamod.aspects;

import net.minecraft.nbt.CompoundNBT;

import java.util.*;

public class AspectCell implements IAspectHolder {

	private AspectStack stored = AspectStack.EMPTY;
	private Aspect optionalWhitelist = null;
	private int capacity;

	public boolean ignoreFullness = false;

	public AspectCell(){
		this(100);
	}

	public AspectCell(int capacity){
		this.capacity = capacity;
	}
	
	public AspectCell(Aspect optionalWhitelist){
		this(100, optionalWhitelist);
	}
	
	public AspectCell(int capacity, Aspect optionalWhitelist){
		this.capacity = capacity;
		this.optionalWhitelist = optionalWhitelist;
	}

	public int insert(AspectStack stack, boolean simulate){
		if(optionalWhitelist != null && optionalWhitelist != stack.getAspect())
			return stack.getAmount();
		int capacityRemaining = getCapacity(stack.getAspect()) == -1 ? -1 : Math.max(getCapacity(stack.getAspect()) - getCurrentVis(), 0);
		if(capacityRemaining == -1 || stack.getAmount() <= capacityRemaining){
			if(!simulate)
				stored = new AspectStack(stack.getAspect(), getCurrentVis() + stack.getAmount());
			return 0;
		}else{
			if(!simulate)
				stored = new AspectStack(stack.getAspect(), getCapacity(stack.getAspect()));
			this.onInsertWhenFull(stack);
			return stack.getAmount() - capacityRemaining;
		}
	}

	public int drain(AspectStack stack, boolean simulate){
		if(optionalWhitelist != null && optionalWhitelist != stack.getAspect())
			return 0;
		// if amount >= left, return left & left = 0
		// if amount < left, return amount & left = left - amount
		int vis = getCurrentVis();
		if(stack.getAmount() >= vis){
			if(!simulate)
				stored = AspectStack.EMPTY;
			return vis;
		}else{
			if(!simulate)
				stored = new AspectStack(stack.getAspect(), vis - stack.getAmount());
			return stack.getAmount();
		}
	}

	public int getCurrentVis(){
		return stored.getAmount();
	}

	public boolean canInsert(Aspect aspect){
		return (getCapacity(aspect) == -1 || getCurrentVis() < getCapacity(aspect)) && (optionalWhitelist == null || optionalWhitelist == aspect);
	}

	public boolean canStore(Aspect aspect){
		return optionalWhitelist == null || optionalWhitelist == aspect;
	}

	public int getCapacity(Aspect aspect){
		return (optionalWhitelist != null && optionalWhitelist != aspect) ? 0 : capacity;
	}

	public int getCapacity(){
		return capacity;
	}

	public Set<Aspect> getAllowedAspects(){
		// insertion order matters!
		return optionalWhitelist == null ? new LinkedHashSet<>(Aspects.getWithoutEmpty()) : Collections.singleton(optionalWhitelist);
	}

	public CompoundNBT toNBT(){
		CompoundNBT compoundNBT = new CompoundNBT();
		compoundNBT.putInt("amount", stored.getAmount());
		compoundNBT.putInt("capacity", getCapacity());
		compoundNBT.putString("aspect", stored.getAspect().name().toLowerCase());
		compoundNBT.putString("whitelisted", optionalWhitelist != null ? optionalWhitelist.name().toLowerCase() : "null");
		return compoundNBT;
	}

	public static AspectCell fromNBT(CompoundNBT compoundNBT){
		int capacity = compoundNBT.getInt("capacity");
		int amount = compoundNBT.getInt("amount");
		Aspect aspect = Aspects.valueOf(compoundNBT.getString("aspect").toUpperCase());
		String whitelisted = compoundNBT.getString("whitelisted");
		Aspect whitelist = whitelisted.equals("null") ? null : Aspects.valueOf(whitelisted.toUpperCase());
		AspectCell cell = new AspectCell(capacity != 0 ? capacity : 100, whitelist);
		cell.insert(new AspectStack(aspect, amount), false);
		return cell;
	}

	public AspectStack getContainedAspectStack(){
		return stored;
	}

	public Aspect getContainedAspect(){
		return optionalWhitelist != null ? optionalWhitelist : stored.getAspect();
	}

	@Override
	public void setCapacity(int defaultCellSize) {
		capacity = defaultCellSize;
	}

	public void onInsertWhenFull(AspectStack stack) {

	}

	@Override
	public void clear() {
		stored = AspectStack.EMPTY;
	}

	@Override
	public boolean isIgnoringFullness() {
		return ignoreFullness;
	}

	public void setIgnoreFullness(boolean ignoreFullness) {
		this.ignoreFullness = ignoreFullness;
	}

	@Override
	public String toString() {
		return "VisBattery{" +
				"stored=" + stored +
				", capacity=" + capacity +
				", whitelisted=" + optionalWhitelist +
				'}';
	}
}