package net.arcanamod.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.items.recipes.AlchemyRecipe;
import net.arcanamod.items.recipes.ArcaneCraftingShapedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

import static net.arcanamod.ArcanaVariables.arcLoc;
import static net.arcanamod.compat.ArcanaJeiPlugin.ALCHEMY_UUID;

public class AlchemyCategory implements IRecipeCategory<AlchemyRecipe> {
	private final IJeiHelpers jeiHelpers;

	public AlchemyCategory(IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}

	@Override
	public ResourceLocation getUid() {
		return ALCHEMY_UUID;
	}

	@Override
	public Class<? extends AlchemyRecipe> getRecipeClass() {
		return AlchemyRecipe.class;
	}

	@Override
	public String getTitle() {
		return "Alchemy";
	}

	@Override
	public IDrawable getBackground() {
		return jeiHelpers.getGuiHelper().createDrawable(arcLoc("textures/gui/compat/alchemy.png"),0,0,102,78);
	}

	@Override
	public IDrawable getIcon() {
		return jeiHelpers.getGuiHelper().createDrawableIngredient(new ItemStack(ArcanaBlocks.CRUCIBLE.get()));
	}

	@Override
	public void setIngredients(AlchemyRecipe recipe, IIngredients iIngredients) {
		iIngredients.setInputLists(VanillaTypes.ITEM,JEIIngredientStackListBuilder.make(recipe.getIngredients().toArray(new Ingredient[recipe.getIngredients().size()])).build());
		iIngredients.setInputIngredients(recipe.getIngredients());
		iIngredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
	}

	@Override
	public void setRecipe(IRecipeLayout iRecipeLayout, AlchemyRecipe recipe, IIngredients iIngredients) {
		IGuiItemStackGroup igroup = iRecipeLayout.getItemStacks();
		igroup.init(0,true,13,1);
		igroup.set(0, Arrays.asList(recipe.getIngredients().get(0).getMatchingStacks()));
		igroup.init(1,false,60,1);
		igroup.set(1,recipe.getRecipeOutput());
	}
}