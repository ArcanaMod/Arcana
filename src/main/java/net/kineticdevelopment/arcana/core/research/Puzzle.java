package net.kineticdevelopment.arcana.core.research;

import com.google.gson.JsonObject;
import net.kineticdevelopment.arcana.common.containers.AspectSlot;
import net.kineticdevelopment.arcana.common.containers.ResearchTableContainer;
import net.kineticdevelopment.arcana.core.aspects.AspectHandler;
import net.kineticdevelopment.arcana.core.research.impls.Chemistry;
import net.kineticdevelopment.arcana.core.research.impls.Fieldwork;
import net.kineticdevelopment.arcana.core.research.impls.Guesswork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
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
		String desc = passData.getString("desc");
		ResourceLocation key = new ResourceLocation(passData.getString("key"));
		ResourceLocation icon = new ResourceLocation(passData.getString("icon"));
		NBTTagCompound data = passData.getCompoundTag("data");
		if(deserializers.get(type) != null){
			Puzzle puzzle = deserializers.get(type).apply(data);
			puzzle.key = key;
			puzzle.desc = desc;
			puzzle.icon = icon;
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
	
	public abstract List<Pair<Integer, Integer>> getItemSlotLocations(EntityPlayer player);
	
	public abstract List<AspectSlot> getAspectSlots(Supplier<AspectHandler> returnInv);
	
	public abstract boolean validate(List<AspectSlot> aspectSlots, List<Slot> itemSlots);
	
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
		passData.setString("desc", getDesc() != null ? getDesc() : "null");
		passData.setString("icon", getIcon().toString());
		passData.setTag("data", getData());
		return passData;
	}
	
	public ResourceLocation getKey(){
		return key;
	}
	
	int guiLeft(int screenWidth){
		return (screenWidth - ResearchTableContainer.WIDTH) / 2;
	}
	
	int guiTop(int screenHeight){
		return (screenHeight - ResearchTableContainer.HEIGHT) / 2;
	}
	
	protected int paperLeft(int screenWidth){
		return guiLeft(screenWidth) + 141;
	}
	
	protected int paperTop(int screenHeight){
		return guiTop(screenHeight) + 35;
	}
}