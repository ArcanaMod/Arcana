package net.arcanamod.items.attachment;

import net.arcanamod.items.ArcanaItems;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface Core{
	
	List<Core> CORES = new ArrayList<>();
	
	static Optional<Core> getCoreById(int id){
		if(id < 0 || id >= CORES.size())
			return Optional.empty();
		else
			return Optional.of(CORES.get(id));
	}
	
	static Core getCoreOrError(int id){
		return getCoreById(id).orElse(ArcanaItems.ERROR_WAND_CORE);
	}
	
	int maxVis();
	
	int level();
	
	String getCoreTranslationKey();
	
	ResourceLocation getTextureLocation();
	
	class Impl implements Core{
		
		int maxVis, level;
		String translationKey;
		ResourceLocation textureLocation;
		
		public Impl(int maxVis, int level, String translationKey, ResourceLocation textureLocation){
			this.maxVis = maxVis;
			this.level = level;
			this.translationKey = translationKey;
			this.textureLocation = textureLocation;
			CORES.add(this);
		}
		
		public int maxVis(){
			return maxVis;
		}
		
		public int level(){
			return level;
		}
		
		public String getCoreTranslationKey(){
			return translationKey;
		}
		
		public ResourceLocation getTextureLocation(){
			return textureLocation;
		}
	}
}