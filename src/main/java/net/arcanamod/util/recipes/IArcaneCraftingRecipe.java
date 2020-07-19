package net.arcanamod.util.recipes;

import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;

public interface IArcaneCraftingRecipe extends ICraftingRecipe {
	default IRecipeType<?> getType() {
		return ArcanaRecipes.Types.ARCANE_CRAFTING_SHAPED;
	}
}