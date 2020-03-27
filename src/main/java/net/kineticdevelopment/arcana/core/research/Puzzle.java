package net.kineticdevelopment.arcana.core.research;

import com.google.gson.JsonObject;
import net.kineticdevelopment.arcana.core.research.impls.Fieldwork;
import net.kineticdevelopment.arcana.core.research.impls.Guesswork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Puzzle{
	
	// STATIC STUFF
	
	// I can't bother to add addon support for custom puzzles,
	// if anyone wants to do that *then* I'll add it
	private static Map<String, Supplier<Puzzle>> factories = new LinkedHashMap<>();
	private static Map<String, Function<NBTTagCompound, Puzzle>> deserializers = new LinkedHashMap<>();
	
	public static Puzzle makePuzzle(String type, JsonObject content){
		if(getBlank(type) != null){
			Puzzle puzzle = getBlank(type).get();
			puzzle.load(content);
			return puzzle;
		}else
			return null;
	}
	
	public static Supplier<Puzzle> getBlank(String type){
		return factories.get(type);
	}
	
	public static Puzzle deserialze(NBTTagCompound passData){
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
		factories.put("guesswork", Guesswork::new);
		deserializers.put("guesswork", Guesswork::deserialize);
		factories.put("fieldwork", Fieldwork::new);
		deserializers.put("fieldwork", __ -> new Fieldwork());
		factories.put("chemistry", null);
		deserializers.put("chemistry", null);
	}
	
	// INSTANCE STUFF
	
	ResourceLocation key;
	
	public abstract void load(JsonObject data);
	
	public abstract String type();
	
	public abstract NBTTagCompound getData();
	
	public NBTTagCompound getPassData(){
		NBTTagCompound passData = new NBTTagCompound();
		passData.setString("type", type());
		passData.setString("key", getKey().toString());
		passData.setTag("data", getData());
		return passData;
	}
	
	public ResourceLocation getKey(){
		return key;
	}
}