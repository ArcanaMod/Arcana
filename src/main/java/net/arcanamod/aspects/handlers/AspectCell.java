package net.arcanamod.aspects.handlers;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.Aspects;
import net.minecraft.nbt.CompoundNBT;

import java.util.List;
import java.util.function.Consumer;

public class AspectCell implements AspectHolder{
	
	private AspectStack stored = AspectStack.EMPTY;
	private float capacity;
	private boolean voids = false, canInsert = false;
	// not serialized, set these yourself
	private Consumer<Float> overfillingCallback = __ -> {};
	private List<Aspect> whitelist = null;
	
	public AspectCell(){
		this(100);
	}
	
	public AspectCell(float capacity){
		this.capacity = capacity;
	}
	
	public AspectStack getStack(){
		return stored;
	}
	
	public float getCapacity(){
		return capacity;
	}
	
	public List<Aspect> getWhitelist(){
		return whitelist;
	}
	
	public boolean voids(){
		return voids;
	}
	
	public boolean canInsert(){
		return canInsert;
	}
	
	public Consumer<Float> overfillingCallback(){
		return overfillingCallback;
	}
	
	public void setStack(AspectStack stack){
		stored = stack;
	}
	
	public void setCapacity(float capacity){
		this.capacity = capacity;
	}
	
	public void setWhitelist(List<Aspect> whitelist){
		this.whitelist = whitelist;
	}
	
	public void setVoids(boolean voids){
		this.voids = voids;
	}
	
	public void setCanInsert(boolean canInsert){
		this.canInsert = canInsert;
	}
	
	public void setOverfillingCallback(Consumer<Float> callback){
		this.overfillingCallback = callback == null ? __ -> {} : callback;
	}
	
	public CompoundNBT serializeNBT(){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putFloat("amount", getStack().getAmount());
		nbt.putFloat("capacity", getCapacity());
		nbt.putString("aspect", getStack().getAspect().name().toLowerCase());
		nbt.putBoolean("voids", voids);
		nbt.putBoolean("canInsert", canInsert);
		return nbt;
	}
	
	public void deserializeNBT(CompoundNBT data){
		capacity = data.getInt("capacity");
		int amount = data.getInt("amount");
		Aspect aspect = Aspects.valueOf(data.getString("aspect").toUpperCase());
		voids = data.getBoolean("voids");
		// getter returns false by default, but we want this to be true by default
		canInsert = !data.contains("canInsert") || data.getBoolean("canInsert");
		setStack(new AspectStack(aspect, amount));
	}
	
	public static AspectCell fromNbt(CompoundNBT data){
		AspectCell ret = new AspectCell();
		ret.deserializeNBT(data);
		return ret;
	}
}