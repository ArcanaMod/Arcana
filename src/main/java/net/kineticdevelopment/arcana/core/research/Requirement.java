package net.kineticdevelopment.arcana.core.research;

import net.kineticdevelopment.arcana.core.research.impls.FieldworkRequirement;
import net.kineticdevelopment.arcana.core.research.impls.GuessworkRequirement;
import net.kineticdevelopment.arcana.core.research.impls.ItemRequirement;
import net.kineticdevelopment.arcana.core.research.impls.XpRequirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public abstract class Requirement{
	
	////////// static stuff
	
	private static Map<ResourceLocation, Function<List<String>, Requirement>> factories = new LinkedHashMap<>();
	private static Map<ResourceLocation, Function<NBTTagCompound, Requirement>> deserializers = new LinkedHashMap<>();
	
	public static Requirement makeRequirement(ResourceLocation type, List<String> content){
		if(factories.get(type) != null)
			return factories.get(type).apply(content);
		else
			return null;
	}
	
	public static Requirement deserialze(NBTTagCompound passData){
		ResourceLocation type = new ResourceLocation(passData.getString("type"));
		NBTTagCompound data = passData.getCompoundTag("data");
		int amount = passData.getInteger("amount");
		if(deserializers.get(type) != null){
			Requirement requirement = deserializers.get(type).apply(data);
			requirement.amount = amount;
			return requirement;
		}
		return null;
	}
	
	public static void init(){
		deserializers.put(ItemRequirement.TYPE, compound -> new ItemRequirement(ForgeRegistries.ITEMS.getValue(new ResourceLocation(compound.getString("itemType")))));
		deserializers.put(XpRequirement.TYPE, __ -> new XpRequirement());
		deserializers.put(GuessworkRequirement.TYPE, compound -> new GuessworkRequirement(compound.getInteger("id")));
		// item requirement creation is handled by ResearchLoader -- an explicit form may be useful though.
		factories.put(XpRequirement.TYPE, __ -> new XpRequirement());
		factories.put(GuessworkRequirement.TYPE, params -> new GuessworkRequirement(Integer.parseInt(params.get(0))));
		
		// TODO: fieldworks
		factories.put(FieldworkRequirement.TYPE, __ -> new FieldworkRequirement());
		deserializers.put(FieldworkRequirement.TYPE, __ -> new FieldworkRequirement());
	}
	
	////////// instance stuff
	
	protected int amount = 1;
	
	public int getAmount(){
		return amount;
	}
	
	public NBTTagCompound getPassData(){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("type", type().toString());
		nbt.setTag("data", data());
		nbt.setInteger("amount", getAmount());
		return nbt;
	}
	
	public Requirement setAmount(int amount){
		this.amount = amount;
		return this;
	}
	
	public abstract boolean satisfied(EntityPlayer player);
	public abstract void take(EntityPlayer player);
	public abstract ResourceLocation type();
	public abstract NBTTagCompound data();
	
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(!(o instanceof Requirement))
			return false;
		Requirement that = (Requirement)o;
		return getAmount() == that.getAmount() && type().equals(that.type());
	}
	
	public int hashCode(){
		return Objects.hash(getAmount(), type());
	}
}