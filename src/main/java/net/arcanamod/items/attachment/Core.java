package net.arcanamod.items.attachment;

import net.arcanamod.Arcana;
import net.minecraft.util.ResourceLocation;

import java.util.*;

/**
 * Represents a type of wand core. Provides access to relevant stats, such as the core's
 * maximum vis or level.
 *
 * @author Luna
 * @see Cap
 * @see CoreItem
 * @see Core.Impl
 */
public interface Core{
	
	Map<ResourceLocation, Core> CORES = new LinkedHashMap<>();
	
	Impl ERROR_WAND_CORE = new Impl(0, 0, Arcana.arcLoc("error_wand"));
	
	static Optional<Core> getCoreById(ResourceLocation id){
		return Optional.ofNullable(CORES.getOrDefault(id, null));
	}
	
	static Core getCoreOrError(ResourceLocation id){
		return getCoreById(id).orElse(ERROR_WAND_CORE);
	}
	
	int maxVis();
	
	int level();
	
	default String getCoreTranslationKey(){
		return "item." + getId().getNamespace() + "." + getId().getPath();
	}
	
	default ResourceLocation getTextureLocation(){
		return new ResourceLocation(getId().getNamespace(), "models/wands/materials/" + getId().getPath());
	}
	
	default boolean capAllowed(Cap cap){
		return level() >= cap.level();
	}
	
	ResourceLocation getId();
	
	class Impl implements Core{
		
		int maxVis, level;
		ResourceLocation id;
		
		public Impl(int maxVis, int level, ResourceLocation id){
			this.maxVis = maxVis;
			this.level = level;
			this.id = id;
			CORES.put(getId(), this);
		}
		
		public int maxVis(){
			return maxVis;
		}
		
		public int level(){
			return level;
		}
		
		public ResourceLocation getId(){
			return id;
		}
	}
}