package net.arcanamod.aspects;

import net.arcanamod.util.StreamUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

import java.util.*;
import java.util.stream.Collectors;

public class TypedVisBattery extends VisBattery{
	
	List<Aspect> allowed = new ArrayList<>();
	
	public TypedVisBattery(Collection<Aspect> allowed){
		this(100, allowed);
	}
	
	public TypedVisBattery(int capacity, Collection<Aspect> allowed){
		super(capacity);
		this.allowed.addAll(allowed);
	}
	
	public int insert(Aspect aspect, int amount, boolean simulate){
		if(allowed.contains(aspect))
			return super.insert(aspect, amount, simulate);
		return amount;
	}
	
	public int drain(Aspect aspect, int amount, boolean simulate){
		if(allowed.contains(aspect))
			return super.drain(aspect, amount, simulate);
		return 0;
	}
	
	public int getCurrentVis(Aspect aspect){
		if(allowed.contains(aspect))
			return super.getCurrentVis(aspect);
		return 0;
	}
	
	public boolean canInsert(Aspect aspect){
		if(allowed.contains(aspect))
			return super.canInsert(aspect);
		return false;
	}
	
	public boolean canStore(Aspect aspect){
		return allowed.contains(aspect);
	}
	
	public Set<Aspect> getAllowedAspects(){
		return new HashSet<>(allowed);
	}
	
	public int getCapacity(Aspect aspect){
		if(allowed.contains(aspect))
			return super.getCapacity(aspect);
		return 0;
	}
	
	public CompoundNBT serializeNBT(){
		CompoundNBT compound = super.serializeNBT();
		ListNBT types = new ListNBT();
		allowed.forEach((type) -> types.add(StringNBT.valueOf(type.name().toLowerCase())));
		compound.put("allowed", types);
		return compound;
	}
	
	public void deserializeNBT(CompoundNBT data){
		super.deserializeNBT(data);
		this.allowed = StreamUtils.streamAndApply(data.getList("allowed", 8), StringNBT.class, StringNBT::getString).map(String::toUpperCase).map(Aspect::valueOf).collect(Collectors.toList());
	}
	
	public static TypedVisBattery primalBattery(){
		return new TypedVisBattery(Arrays.asList(Aspects.primalAspects));
	}
	
	public static TypedVisBattery primalBattery(int capacity){
		return new TypedVisBattery(capacity, Arrays.asList(Aspects.primalAspects));
	}
}