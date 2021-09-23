package net.arcanamod.systems.research;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.systems.research.impls.ImageLayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BackgroundLayer{
	
	////////// static stuff
	
	private static Map<ResourceLocation, Supplier<BackgroundLayer>> factories = new LinkedHashMap<>();
	private static Map<ResourceLocation, Function<CompoundNBT, BackgroundLayer>> deserializers = new LinkedHashMap<>();
	
	public static BackgroundLayer makeLayer(ResourceLocation type, JsonObject content, ResourceLocation file, float speed, float vanishZoom){
		if(getBlank(type) != null){
			BackgroundLayer layer = getBlank(type).get();
			layer.setSpeed(speed);
			layer.setVanishZoom(vanishZoom);
			layer.load(content, file);
			return layer;
		}else
			return null;
	}
	
	public static BackgroundLayer deserialize(CompoundNBT passData){
		ResourceLocation type = new ResourceLocation(passData.getString("type"));
		CompoundNBT data = passData.getCompound("data");
		float speed = passData.getFloat("speed");
		float vanishZoom = passData.getFloat("vanishZoom");
		if(deserializers.get(type) != null){
			BackgroundLayer layer = deserializers.get(type).apply(data);
			layer.setSpeed(speed).setVanishZoom(vanishZoom);
			return layer;
		}
		return null;
	}
	
	public static Supplier<BackgroundLayer> getBlank(ResourceLocation type){
		return factories.get(type);
	}
	
	public static void init(){
		factories.put(ImageLayer.TYPE, ImageLayer::new);
		deserializers.put(ImageLayer.TYPE, nbt -> new ImageLayer(nbt.getString("image")));
	}
	
	///////// instance stuff
	
	protected float speed = 0.5f, vanishZoom = -1;
	
	public float speed(){
		return speed;
	}
	
	public float vanishZoom(){
		return vanishZoom;
	}
	
	public BackgroundLayer setSpeed(float speed){
		this.speed = speed;
		return this;
	}
	
	public BackgroundLayer setVanishZoom(float vanishZoom){
		this.vanishZoom = vanishZoom;
		return this;
	}
	
	public CompoundNBT getPassData(){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("type", type().toString());
		nbt.put("data", data());
		nbt.putFloat("speed", speed());
		nbt.putFloat("vanishZoom", vanishZoom());
		return nbt;
	}
	
	public abstract ResourceLocation type();
	
	public abstract CompoundNBT data();
	
	public abstract void load(JsonObject data, ResourceLocation file);
	
	public abstract void render(MatrixStack stack, int x, int y, int width, int height, float xPan, float yPan, float parallax, float xOff, float yOff, float zoom);
}