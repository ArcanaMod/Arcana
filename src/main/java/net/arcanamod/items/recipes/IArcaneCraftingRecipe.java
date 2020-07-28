package net.arcanamod.items.recipes;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.UndecidedAspectStack;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface IArcaneCraftingRecipe extends IRecipe<AspectCraftingInventory> {
	
	default IRecipeType<?> getType() {
		return ArcanaRecipes.Types.ARCANE_CRAFTING_SHAPED;
	}

	@Override
	default ItemStack getIcon() {
		return new ItemStack(ArcanaBlocks.ARCANE_CRAFTING_TABLE.get());
	}
	
	boolean matchesIgnoringAspects(AspectCraftingInventory inv, World worldIn);
	
	UndecidedAspectStack[] getAspectStacks();
}