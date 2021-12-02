package net.arcanamod.aspects;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Objects;

public class AspectStack{
	
	// TODO: replace with a method that creates a new one, or make AspectStack immutable
	public static final AspectStack EMPTY = new AspectStack(Aspects.EMPTY, 0);
	
	private float amount;
	private Aspect aspect;
	
	public boolean isEmpty(){
		return amount == 0 || aspect == Aspects.EMPTY;
	}
	
	public void setAspect(Aspect aspect){
		this.aspect = aspect;
	}
	
	public float getAmount(){
		return amount;
	}
	
	public void setAmount(float amount){
		this.amount = amount;
		if(amount <= 0)
			this.amount = 0;
	}
	
	public Aspect getAspect(){
		return aspect;
	}
	
	public AspectStack(){
		this(Aspects.EMPTY, 0);
	}
	
	public AspectStack(Aspect aspect){
		this(aspect, 1);
	}
	
	public AspectStack(Aspect aspect, float amount){
		boolean isEmpty = amount <= 0 || aspect == Aspects.EMPTY;
		
		this.aspect = isEmpty ? Aspects.EMPTY : aspect;
		this.amount = isEmpty ? 0 : amount;
	}
	
	@Override
	public String toString(){
		return "AspectStack{" +
				"amount=" + amount +
				", aspect=" + aspect +
				'}';
	}
	
	@Nonnull
	public CompoundNBT toNbt(){
		CompoundNBT tag = new CompoundNBT();
		tag.putString("aspect", getAspect().toResourceLocation().toString());
		tag.putFloat("amount", getAmount());
		return tag;
	}
	
	@Nonnull
	public static AspectStack fromNbt(@Nonnull CompoundNBT tag){
		return new AspectStack(Aspects.ASPECTS.get(new ResourceLocation(tag.getString("aspect"))), tag.getFloat("amount"));
	}
	
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		AspectStack stack = (AspectStack)o;
		return Float.compare(stack.amount, amount) == 0 &&
				Objects.equals(aspect, stack.aspect);
	}
	
	public int hashCode(){
		return Objects.hash(amount, aspect);
	}
}