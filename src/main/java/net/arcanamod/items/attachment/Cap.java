package net.arcanamod.items.attachment;

import net.arcanamod.items.ArcanaItems;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface Cap{
	
	List<Cap> CAPS = new ArrayList<>();
	
	static Optional<Cap> getCapById(int id){
		if(id < 0 || id >= CAPS.size())
			return Optional.empty();
		else
			return Optional.of(CAPS.get(id));
	}
	
	static Cap getCapOrError(int id){
		return getCapById(id).orElse(ArcanaItems.ERROR_CAP);
	}
	
	int power();
	
	int maxEffects();
	
	int level();
	
	String getTranslationKey();
	
	ResourceLocation getTextureLocation();
	
	class Impl implements Cap{
		
		int power;
		int maxEffects;
		int level;
		String translationKey;
		ResourceLocation modelLocation;
		
		public Impl(int power, int maxEffects, int level, String translationKey, ResourceLocation modelLocation){
			this.power = power;
			this.maxEffects = maxEffects;
			this.level = level;
			this.translationKey = translationKey;
			this.modelLocation = modelLocation;
			CAPS.add(this);
		}
		
		public int power(){
			return power;
		}
		
		public int maxEffects(){
			return maxEffects;
		}
		
		public int level(){
			return level;
		}
		
		public String getTranslationKey(){
			return translationKey;
		}
		
		public ResourceLocation getTextureLocation(){
			return modelLocation;
		}
	}
}