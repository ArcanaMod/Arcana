package net.kineticdevelopment.arcana.core.aspects;

import net.kineticdevelopment.arcana.core.aspects.Aspect.AspectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TypedVisBattery extends VisBattery{
	
	List<AspectType> allowed = new ArrayList<>();
	
	public TypedVisBattery(Collection<AspectType> allowed){
		this(100, allowed);
	}
	
	public TypedVisBattery(int capacity, Collection<AspectType> allowed){
		super(capacity);
		this.allowed.addAll(allowed);
	}
	
	public int insert(AspectType aspect, int amount, boolean simulate){
		if(allowed.contains(aspect))
			return super.insert(aspect, amount, simulate);
		return amount;
	}
	
	public int drain(AspectType aspect, int amount, boolean simulate){
		if(allowed.contains(aspect))
			return super.drain(aspect, amount, simulate);
		return 0;
	}
	
	public int getCurrentVis(AspectType aspect){
		if(allowed.contains(aspect))
			return super.getCurrentVis(aspect);
		return 0;
	}
	
	public boolean canInsert(AspectType aspect){
		return allowed.contains(aspect);
	}
	
	public boolean canStore(AspectType aspect){
		if(allowed.contains(aspect))
			return super.canInsert(aspect);
		return false;
	}
	
	public int getCapacity(AspectType aspect){
		if(allowed.contains(aspect))
		return super.getCapacity(aspect);
		return 0;
	}
	
	public static TypedVisBattery primalBattery(){
		return new TypedVisBattery(Arrays.asList(Aspect.primalAspects));
	}
	
	public static TypedVisBattery primalBattery(int capacity){
		return new TypedVisBattery(capacity, Arrays.asList(Aspect.primalAspects));
	}
}