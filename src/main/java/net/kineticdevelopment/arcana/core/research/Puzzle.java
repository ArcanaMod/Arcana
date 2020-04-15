package net.kineticdevelopment.arcana.core.research;

import com.google.gson.JsonObject;
import net.kineticdevelopment.arcana.core.research.impls.Chemistry;
import net.kineticdevelopment.arcana.core.research.impls.Fieldwork;
import net.kineticdevelopment.arcana.core.research.impls.Guesswork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Puzzle{
	
	////////////////////// STATIC STUFF
	
	// I can't bother to add addon support for custom puzzles,
	// if anyone wants to do that, *then* I'll add it
	private static Map<String, Supplier<Puzzle>> factories = new LinkedHashMap<>();
	private static Map<String, Function<NBTTagCompound, Puzzle>> deserializers = new LinkedHashMap<>();
	
	public static Puzzle makePuzzle(String type, @Nullable String desc, ResourceLocation key, @Nullable ResourceLocation icon, JsonObject content, ResourceLocation file){
		if(getBlank(type) != null){
			Puzzle puzzle = getBlank(type).get();
			puzzle.key = key;
			puzzle.desc = desc != null ? desc : puzzle.getDefaultDesc();
			puzzle.icon = icon != null ? icon : puzzle.getDefaultIcon();
			puzzle.load(content, file);
			return puzzle;
		}else
			return null;
	}
	
	public static Supplier<Puzzle> getBlank(String type){
		return factories.get(type);
	}
	
	public static Puzzle deserialize(NBTTagCompound passData){
		String type = passData.getString("type");
		ResourceLocation key = new ResourceLocation(passData.getString("key"));
		NBTTagCompound data = passData.getCompoundTag("data");
		if(deserializers.get(type) != null){
			Puzzle puzzle = deserializers.get(type).apply(data);
			puzzle.key = key;
			return puzzle;
		}else
			return null;
	}
	
	public static void init(){
		factories.put(Guesswork.TYPE, Guesswork::new);
		deserializers.put(Guesswork.TYPE, Guesswork::fromNBT);
		factories.put(Fieldwork.TYPE, Fieldwork::new);
		deserializers.put(Fieldwork.TYPE, __ -> new Fieldwork());
		factories.put(Chemistry.TYPE, Chemistry::new);
		deserializers.put(Chemistry.TYPE, Chemistry::fromNBT);
	}
	
	////////////////////// INSTANCE STUFF
	
	ResourceLocation key, icon;
	String desc;
	
	public abstract void load(JsonObject data, ResourceLocation file);
	
	public abstract String type();
	
	public abstract NBTTagCompound getData();
	
	public abstract String getDefaultDesc();
	public abstract ResourceLocation getDefaultIcon();
	
	public String getDesc(){
		return desc;
	}
	
	public ResourceLocation getIcon(){
		return icon;
	}
	
	public NBTTagCompound getPassData(){
		NBTTagCompound passData = new NBTTagCompound();
		passData.setString("type", type());
		passData.setString("key", getKey().toString());
		passData.setString("desc", getDesc());
		passData.setString("icon", getIcon().toString());
		passData.setTag("data", getData());
		return passData;
	}
	
	public ResourceLocation getKey(){
		return key;
	}
}