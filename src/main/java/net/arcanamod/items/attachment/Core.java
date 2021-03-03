package net.arcanamod.items.attachment;

import net.arcanamod.Arcana;
import net.arcanamod.systems.spell.MDModifier;
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
	
	Impl ERROR_WAND_CORE = new Impl(0,0, 0, new MDModifier.Empty(), Arcana.arcLoc("error_wand"));
	
	static Optional<Core> getCoreById(ResourceLocation id){
		return Optional.ofNullable(CORES.getOrDefault(id, null));
	}
	
	static Core getCoreOrError(ResourceLocation id){
		return getCoreById(id).orElse(ERROR_WAND_CORE);
	}
	
	int maxVis();
	
	int level();

	int difficulty();

	MDModifier modifier();
	
	default String getCoreTranslationKey(){
		return "item." + getId().getNamespace() + "." + getId().getPath();
	}
	
	default ResourceLocation getTextureLocation(){
		return new ResourceLocation(getId().getNamespace(), "models/wands/materials/" + getId().getPath());
	}

	default ResourceLocation getGuiTexture(){
		return new ResourceLocation(getId().getNamespace(), "textures/gui/hud/wand/core/" + getId().getPath() + ".png");
	}
	
	default boolean capAllowed(Cap cap){
		return level() >= cap.level();
	}
	
	ResourceLocation getId();
	
	class Impl implements Core{
		
		int maxVis, level, difficulty;
		MDModifier mod;
		ResourceLocation id;
		
		public Impl(int maxVis, int difficulty, int level, MDModifier mod, ResourceLocation id){
			this.maxVis = maxVis;
			this.difficulty = difficulty;
			this.level = level;
			this.mod = mod;
			this.id = id;
			CORES.put(getId(), this);
		}

		public int maxVis(){
			return maxVis;
		}
		
		public int level(){
			return level;
		}

		public int difficulty(){
			return difficulty;
		}

		@Override
		public MDModifier modifier() {
			return mod;
		}

		public ResourceLocation getId(){
			return id;
		}
	}
}