package net.arcanamod.aspects;

import net.minecraft.nbt.CompoundNBT;

import java.util.*;

public class AspectCell implements IAspectHolder {

	private AspectStack stored = AspectStack.EMPTY;
	private Aspect optionalWhitelist = null;
	private float capacity;
	public boolean ignoreFullness = false, canInput = true;

	public AspectCell(){
		this(100);
	}

	public AspectCell(float capacity){
		this.capacity = capacity;
	}
	
	public AspectCell(Aspect optionalWhitelist){
		this(100, optionalWhitelist);
	}
	
	public AspectCell(float capacity, Aspect optionalWhitelist){
		this.capacity = capacity;
		this.optionalWhitelist = optionalWhitelist;
	}

	public float insert(AspectStack stack, boolean simulate){
		if(optionalWhitelist != null && optionalWhitelist != stack.getAspect())
			return stack.getAmount();
		float capacityRemaining = getCapacity(stack.getAspect()) == -1 ? -1 : Math.max(getCapacity(stack.getAspect()) - getCurrentVis(), 0);
		if(capacityRemaining == -1 || stack.getAmount() <= capacityRemaining){
			if(!simulate)
				stored = new AspectStack(stack.getAspect(), getCurrentVis() + stack.getAmount());
			return 0;
		}else{
			if(!simulate)
				stored = new AspectStack(stack.getAspect(), getCapacity(stack.getAspect()));
			this.onInsertWhenFull(stack);
			if (ignoreFullness) return 0;
			else return stack.getAmount() - capacityRemaining;
		}
	}

	public float drain(AspectStack stack, boolean simulate){
		if(optionalWhitelist != null && optionalWhitelist != stack.getAspect())
			return 0;
		// if amount >= left, return left & left = 0
		// if amount < left, return amount & left = left - amount
		float vis = getCurrentVis();
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

	public float getCurrentVis(){
		return stored.getAmount();
	}

	public boolean canInsert(Aspect aspect){
		return (getCapacity(aspect) == -1 || getCurrentVis() < getCapacity(aspect)) && (optionalWhitelist == null || optionalWhitelist == aspect);
	}

	public boolean canStore(Aspect aspect){
		return optionalWhitelist == null || optionalWhitelist == aspect;
	}

	public float getCapacity(Aspect aspect){
		return (optionalWhitelist != null && optionalWhitelist != aspect) ? 0 : capacity;
	}

	public float getCapacity(){
		return capacity;
	}

	public void lockWithAspect(Aspect aspect){
		optionalWhitelist = aspect;
	}

	public void unlockCell(Aspect aspect){
		optionalWhitelist = null;
	}

	public Set<Aspect> getAllowedAspects(){
		// insertion order matters!
		return optionalWhitelist == null ? new LinkedHashSet<>(Aspects.getWithoutEmpty()) : Collections.singleton(optionalWhitelist);
	}

	public CompoundNBT toNBT(){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putFloat("amount", stored.getAmount());
		nbt.putFloat("capacity", getCapacity());
		nbt.putString("aspect", stored.getAspect().name().toLowerCase());
		nbt.putString("whitelisted", optionalWhitelist != null ? optionalWhitelist.name().toLowerCase() : "null");
		nbt.putBoolean("ignoreFullness", ignoreFullness);
		nbt.putBoolean("canInput", canInput);
		return nbt;
	}

	public static AspectCell fromNBT(CompoundNBT compoundNBT){
		int capacity = compoundNBT.getInt("capacity");
		int amount = compoundNBT.getInt("amount");
		Aspect aspect = Aspects.valueOf(compoundNBT.getString("aspect").toUpperCase());
		String whitelisted = compoundNBT.getString("whitelisted");
		Aspect whitelist = whitelisted.equals("null") ? null : Aspects.valueOf(whitelisted.toUpperCase());
		boolean ignoresFullness = compoundNBT.getBoolean("ignoreFullness");
		// getter returns false by default, but we want this to be true by default
		boolean canInput = !compoundNBT.contains("canInput") || compoundNBT.getBoolean("canInput");
		AspectCell cell = new AspectCell(capacity != 0 ? capacity : 100, whitelist);
		cell.ignoreFullness = ignoresFullness;
		cell.canInput = canInput;
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
	public void setCapacity(float defaultCellSize) {
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
	
	public boolean canInput(){
		return canInput;
	}
	
	public void setCanInput(boolean canInput){
		this.canInput = canInput;
	}
	
	public void setWhitelist(Aspect whitelist){
		this.optionalWhitelist = whitelist;
	}
	
	@Override
	public String toString() {
		return "VisBattery{" +
				"stored=" + stored +
				", capacity=" + capacity +
				", whitelisted=" + optionalWhitelist +
				", ignoresFullness=" + ignoreFullness +
				", canInput=" + canInput +
				'}';
	}
}