package net.arcanamod.items.attachment;

import net.arcanamod.Arcana;
import net.arcanamod.systems.spell.Spell;
import net.arcanamod.systems.spell.casts.ICast;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.ToIntFunction;

/**
 *
 */
public interface Focus{
	
	List<Focus> FOCI = new ArrayList<>();
	Impl NO_FOCUS = new Impl(Arcana.arcLoc("no_focus"));
	
	static Optional<Focus> getFocusById(int id){
		if(id < 0 || id >= FOCI.size())
			return Optional.empty();
		else
			return Optional.of(FOCI.get(id));
	}
	
	// Let me imagine five cases.
	// NO_FOCUS, which represents the lack of a focus.
	// The default wand focus item, which can hold a spell and have multiple styles, with a model based on that.
	// An addon wand focus, with a different number of styles and holds a spell.
	// A hardcoded focus that has a single style and has a *hardcoded* spell with special behaviour.
	// A wand with hardcoded components that don't have item counterparts - think crystal wand from Horizons.
	
	// So,
	// Foci *may* have a spell, and *may* be able to contain a new one.
	// They need context for their models.
	// They will usually be an item, but not always.
	// Context comes from NBT compounds.
	// Since textures need stitching, all possible textures must be listed.
	
	/**
	 * Returns the resource location for the model added to the wand item model.
	 * Does not affect the focus item.
	 *
	 * @return The focus model.
	 */
	ResourceLocation getModelLocation(CompoundNBT nbt);
	
	/**
	 * Returns a list of resource locations for every model that this focus might add to a wand model.
	 * Does not affect the focus item.
	 *
	 * @return All possible focus models..
	 */
	List<ResourceLocation> getAllModelLocations();
	
	/**
	 * Returns the number of styles supported by this focus.
	 * This is used for the preview/selection in the foci forge.
	 * Style is stored as an NBT tag.
	 *
	 * @return The number of styles supported by this focus.
	 */
	int numStyles();
	
	/**
	 * Optionally returns the item associated with this focus. Used in the wand HUD, and given to a player when they remove this focus.
	 * The item stack's tag is set to the wand's focusData tag.
	 *
	 * @return The item associated with this focus.
	 */
	Optional<Item> getAssociatedItem();
	
	// TODO: switch to CompoundNBT
	Spell getSpell(ItemStack stack);
	
	class Impl implements Focus{
		
		List<ResourceLocation> modelLocations;
		ToIntFunction<CompoundNBT> modelChooser = __ -> 0;
		
		public Impl(ResourceLocation... modelLocations){
			this.modelLocations = Arrays.asList(modelLocations);
			FOCI.add(this);
		}
		
		public Impl(ToIntFunction<CompoundNBT> modelChooser, ResourceLocation... modelLocations){
			this.modelChooser = modelChooser;
			this.modelLocations = Arrays.asList(modelLocations);
			FOCI.add(this);
		}
		
		public ResourceLocation getModelLocation(CompoundNBT nbt){
			return modelLocations.get(modelChooser.applyAsInt(nbt));
		}
		
		public List<ResourceLocation> getAllModelLocations(){
			return modelLocations;
		}
		
		// Doesn't have an item and cannot be used in foci forge.
		public int numStyles(){
			return 0;
		}
		
		// Still has no item.
		public Optional<Item> getAssociatedItem(){
			return Optional.empty();
		}
		
		public Spell getSpell(ItemStack stack){
			return null;
		}
	}
}