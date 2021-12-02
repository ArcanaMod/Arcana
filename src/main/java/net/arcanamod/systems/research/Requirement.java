package net.arcanamod.systems.research;

import net.arcanamod.systems.research.impls.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public abstract class Requirement{
	
	////////// static stuff
	
	private static Map<ResourceLocation, Function<List<String>, Requirement>> factories = new LinkedHashMap<>();
	private static Map<ResourceLocation, Function<CompoundNBT, Requirement>> deserializers = new LinkedHashMap<>();
	
	public static Requirement makeRequirement(ResourceLocation type, List<String> content){
		if(factories.get(type) != null)
			return factories.get(type).apply(content);
		else
			return null;
	}
	
	public static Requirement deserialize(CompoundNBT passData){
		ResourceLocation type = new ResourceLocation(passData.getString("type"));
		CompoundNBT data = passData.getCompound("data");
		int amount = passData.getInt("amount");
		if(deserializers.get(type) != null){
			Requirement requirement = deserializers.get(type).apply(data);
			requirement.amount = amount;
			return requirement;
		}
		return null;
	}
	
	public static void init(){
		// item and item tag requirement creation is handled by ResearchLoader -- an explicit form may be useful though.
		deserializers.put(ItemRequirement.TYPE, compound -> new ItemRequirement(ForgeRegistries.ITEMS.getValue(new ResourceLocation(compound.getString("itemType")))));
		deserializers.put(ItemTagRequirement.TYPE, compound -> new ItemTagRequirement(new ResourceLocation(compound.getString("itemTag"))));
		
		factories.put(XpRequirement.TYPE, __ -> new XpRequirement());
		deserializers.put(XpRequirement.TYPE, __ -> new XpRequirement());
		
		factories.put(PuzzleRequirement.TYPE, params -> new PuzzleRequirement(new ResourceLocation(params.get(0))));
		deserializers.put(PuzzleRequirement.TYPE, compound -> new PuzzleRequirement(new ResourceLocation(compound.getString("puzzle"))));
		
		factories.put(ResearchCompletedRequirement.TYPE, params -> new ResearchCompletedRequirement(params.get(0)));
		deserializers.put(ResearchCompletedRequirement.TYPE, compound -> new ResearchCompletedRequirement(compound.getString("requirement")));
		
		factories.put(PuzzlesCompletedRequirement.TYPE, __ -> new PuzzlesCompletedRequirement());
		deserializers.put(PuzzlesCompletedRequirement.TYPE, __ -> new PuzzlesCompletedRequirement());
	}
	
	////////// instance stuff
	
	protected int amount = 1;
	
	public int getAmount(){
		return amount;
	}
	
	public CompoundNBT getPassData(){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("type", type().toString());
		nbt.put("data", data());
		nbt.putInt("amount", getAmount());
		return nbt;
	}
	
	public Requirement setAmount(int amount){
		this.amount = amount;
		return this;
	}
	
	public abstract boolean satisfied(PlayerEntity player);
	
	public abstract void take(PlayerEntity player);
	
	public abstract ResourceLocation type();
	
	public abstract CompoundNBT data();
	
	public boolean onClick(ResearchEntry entry, PlayerEntity player){
		return false;
	}
	
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