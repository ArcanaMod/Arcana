package net.arcanamod.items.attachment;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.casts.ICast;
import net.arcanamod.systems.spell.Spell;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FocusItem extends Item implements Focus{
	
	private int numStyles;
	private final List<ResourceLocation> modelLocations;
	
	public FocusItem(Properties properties, int numStyles, ResourceLocation... locations){
		super(properties);
		this.numStyles = numStyles;
		modelLocations = Arrays.asList(locations);
		Focus.FOCI.add(this);
		addPropertyOverride(new ResourceLocation("style"), (stack, world, entity) -> stack.getOrCreateTag().getInt("style"));
	}
	
	public ResourceLocation getModelLocation(CompoundNBT nbt){
		return modelLocations.get(Math.min(nbt.getInt("style"), modelLocations.size() - 1));
	}
	
	public List<ResourceLocation> getAllModelLocations(){
		return modelLocations;
	}
	
	public int numStyles(){
		return numStyles;
	}
	
	public Optional<Item> getAssociatedItem(){
		return Optional.of(this);
	}

	public Spell getSpell(ItemStack stack) {
		return Spell.getSerializer().deserializeNBT(stack.getOrCreateTag().getCompound("focusData").getCompound("Spell"));
	}

	public static Aspect getColourAspect(ItemStack stack) {
		@Nullable Spell spell = Spell.getSerializer().deserializeNBT(stack.getOrCreateTag().getCompound("Spell"));
		if (spell != null)
			return spell.getSpellColor();
		else return Aspects.EMPTY;
	}
}