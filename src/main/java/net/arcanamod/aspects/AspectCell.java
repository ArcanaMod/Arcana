package net.arcanamod.aspects;

import net.minecraft.nbt.CompoundNBT;

import java.util.*;

public class AspectCell implements IAspectHolder {

	private AspectStack stored = AspectStack.EMPTY;;
	private int capacity;

	public AspectCell(){
		this(100);
	}

	public AspectCell(int capacity){
		this.capacity = capacity;
	}

	public int insert(AspectStack stack, boolean simulate){
		int capacityRemaining = getCapacity(stack.getAspect()) - getCurrentVis();
		if(stack.getAmount() <= capacityRemaining){
			if(!simulate)
				stored = new AspectStack(stack.getAspect(), getCurrentVis() + stack.getAmount());
			return 0;
		}else{
			if(!simulate)
				stored = new AspectStack(stack.getAspect(), getCapacity(stack.getAspect()));
			return stack.getAmount() - capacityRemaining;
		}
	}

	public int drain(AspectStack stack, boolean simulate){
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
		return getCurrentVis() < getCapacity(aspect);
	}

	public boolean canStore(Aspect aspect){
		return true;
	}

	public int getCapacity(Aspect aspect){
		return capacity;
	}

	public int getCapacity(){
		return capacity;
	}

	public Set<Aspect> getAllowedAspects(){
		// insertion order matters!
		return new LinkedHashSet<>(Aspect.aspects);
	}

	public CompoundNBT toNBT()
	{
		CompoundNBT compoundNBT = new CompoundNBT();
		compoundNBT.putInt("amount",stored.getAmount());
		compoundNBT.putInt("capacity",getCapacity());
		compoundNBT.putString("aspect",stored.getAspect().name().toLowerCase());
		return compoundNBT;
	}

	public static AspectCell fromNBT(CompoundNBT compoundNBT)
	{
		int capacity = compoundNBT.getInt("capacity");
		int amount = compoundNBT.getInt("amount");
		Aspect aspect = Aspect.valueOf(compoundNBT.getString("aspect").toUpperCase());
		AspectCell cell = new AspectCell(capacity != 0 ? capacity : 100);
		cell.insert(new AspectStack(aspect,amount),false);
		return cell;
	}

	public AspectStack getContainedAspectStack(){
		return stored;
	}

	public Aspect getContainedAspect(){
		return stored.getAspect();
	}

	@Override
	public void setCapacity(int defaultCellSize) {
		capacity = defaultCellSize;
	}

	@Override
	public String toString() {
		return "VisBattery{" +
				"stored=" + stored +
				", capacity=" + capacity +
				'}';
	}
}