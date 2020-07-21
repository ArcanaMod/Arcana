package net.arcanamod.util.recipes;

import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.UndecidedAspectStack;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.util.inventories.AspectCraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

public interface IArcaneCraftingRecipe extends IRecipe<AspectCraftingInventory> {
	default IRecipeType<?> getType() {
		return ArcanaRecipes.Types.ARCANE_CRAFTING_SHAPED;
	}

	@Override
	default ItemStack getIcon() {
		return new ItemStack(ArcanaBlocks.ARCANE_CRAFTING_TABLE.get(),1);
	}

	UndecidedAspectStack[] getAspectStacks();
}