package net.arcanamod.systems.research.impls;

import net.arcanamod.systems.research.EntrySection;
import net.arcanamod.systems.research.Icon;
import net.arcanamod.systems.research.Pin;
import net.arcanamod.systems.research.ResearchEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractCraftingSection extends EntrySection{
	
	ResourceLocation recipe;
	
	public AbstractCraftingSection(ResourceLocation recipe){
		this.recipe = recipe;
	}
	
	public AbstractCraftingSection(String s){
		this(new ResourceLocation(s));
	}
	
	public CompoundNBT getData(){
		CompoundNBT compound = new CompoundNBT();
		compound.putString("recipe", recipe.toString());
		return compound;
	}
	
	public ResourceLocation getRecipe(){
		return recipe;
	}
	
	public Stream<Pin> getPins(int index, World world, ResearchEntry entry){
		// if the recipe exists,
		Optional<? extends IRecipe<?>> recipe = world.getRecipeManager().getRecipe(this.recipe);
		if(recipe.isPresent()){
			// get the item as the icon
			ItemStack output = recipe.get().getRecipeOutput();
			Icon icon = new Icon(output.getItem().getRegistryName(), output);
			// and return a pin that points to this
			return Stream.of(new Pin(output.getItem(), entry, index, icon));
		}
		return super.getPins(index, world, entry);
	}
}