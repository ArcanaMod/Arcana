package net.kineticdevelopment.arcana.core.aspects;

import net.kineticdevelopment.arcana.core.aspects.Aspect.AspectType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static net.kineticdevelopment.arcana.utilities.StreamUtils.streamAndApply;

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
	
	public NBTTagCompound serialize(){
		NBTTagCompound compound = super.serialize();
		NBTTagList types = new NBTTagList();
		allowed.forEach((type) -> types.appendTag(new NBTTagString(type.name().toLowerCase())));
		compound.setTag("allowed", types);
		return compound;
	}
	
	public void deserialize(NBTTagCompound data){
		super.deserialize(data);
		this.allowed = streamAndApply(data.getTagList("allowed", 8), NBTTagString.class, NBTTagString::getString)
				.map(AspectType::valueOf)
				.collect(Collectors.toList());
	}
	
	public static TypedVisBattery primalBattery(){
		return new TypedVisBattery(Arrays.asList(Aspect.primalAspects));
	}
	
	public static TypedVisBattery primalBattery(int capacity){
		return new TypedVisBattery(capacity, Arrays.asList(Aspect.primalAspects));
	}
}