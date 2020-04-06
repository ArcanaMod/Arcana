package net.kineticdevelopment.arcana.core.aspects;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.*;
import java.util.stream.Collectors;

import static net.kineticdevelopment.arcana.utilities.StreamUtils.streamAndApply;

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
	
	public NBTTagCompound serializeNBT(){
		NBTTagCompound compound = super.serializeNBT();
		NBTTagList types = new NBTTagList();
		allowed.forEach((type) -> types.appendTag(new NBTTagString(type.name().toLowerCase())));
		compound.setTag("allowed", types);
		return compound;
	}
	
	public void deserializeNBT(NBTTagCompound data){
		super.deserializeNBT(data);
		this.allowed = streamAndApply(data.getTagList("allowed", 8), NBTTagString.class, NBTTagString::getString)
				.map(String::toUpperCase)
				.map(Aspect::valueOf)
				.collect(Collectors.toList());
	}
	
	public static TypedVisBattery primalBattery(){
		return new TypedVisBattery(Arrays.asList(Aspects.primalAspects));
	}
	
	public static TypedVisBattery primalBattery(int capacity){
		return new TypedVisBattery(capacity, Arrays.asList(Aspects.primalAspects));
	}
}