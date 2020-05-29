package net.arcanamod.items.attachment;

import net.arcanamod.Arcana;
import net.minecraft.util.ResourceLocation;

import java.util.*;

/**
 * Represents a type of wand cap. Provides access to relevant stats, such as the cap's
 * maximum power, number of effects, or required wand core level.
 *
 * @author Luna
 * @see Core
 * @see CapItem
 * @see Impl
 */
public interface Cap{
	
	// Diet registry, lol
	Map<ResourceLocation, Cap> CAPS = new LinkedHashMap<>();
	
	Impl ERROR_CAP = new Impl(0, 0, 0, Arcana.arcLoc("error_cap"));
	
	static Optional<Cap> getCapById(ResourceLocation id){
		return Optional.ofNullable(CAPS.getOrDefault(id, null));
	}
	
	static Cap getCapOrError(ResourceLocation id){
		return getCapById(id).orElse(ERROR_CAP);
	}
	
	int power();
	
	int maxEffects();
	
	int level();
	
	default String getPrefixTranslationKey(){
		return "item." + getId().getNamespace() + "." + getId().getPath() + ".prefix";
	}
	
	default ResourceLocation getTextureLocation(){
		return new ResourceLocation(getId().getNamespace(), "models/wands/caps/" + getId().getPath());
	}
	
	ResourceLocation getId();
	
	/**
	 * An object that implements {@link Cap}. Allows for registering a cap type without an item - currently
	 * only used for invalid caps, but may be useful for e.g. a special prebuilt wand type that doesn't have a
	 * separate cap and core.
	 *
	 * @author Luna
	 * @see Cap
	 */
	class Impl implements Cap{
		
		int power;
		int maxEffects;
		int level;
		ResourceLocation id;
		
		public Impl(int power, int maxEffects, int level, ResourceLocation id){
			this.power = power;
			this.maxEffects = maxEffects;
			this.level = level;
			this.id = id;
			CAPS.put(getId(), this);
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
		
		public ResourceLocation getId(){
			return id;
		}
	}
}