package net.arcanamod.util.recipes;

import net.arcanamod.util.inventories.AspectCraftingInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

public interface IArcaneCraftingRecipe extends IRecipe<AspectCraftingInventory> {
	default IRecipeType<?> getType() {
		return ArcanaRecipes.Types.ARCANE_CRAFTING_SHAPED;
	}
}