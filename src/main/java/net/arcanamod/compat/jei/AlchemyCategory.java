package net.arcanamod.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.arcanamod.aspects.AspectStack;
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
		IGuiIngredientGroup<AspectIngredient> agroup = iRecipeLayout.getIngredientsGroup(AspectIngredient.TYPE);
		igroup.init(0,true,13,1);
		igroup.set(0, Arrays.asList(recipe.getIngredients().get(0).getMatchingStacks()));
		igroup.init(1,false,60,1);
		igroup.set(1,recipe.getRecipeOutput());
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 3; j++) {
				int n = (i*3)+j;
				int sizedHeight = recipe.getAspects().size() > 3 ? 30 : 39;
				agroup.init(n,true,42+(j*18),sizedHeight+(i*18));
				if (n < recipe.getAspects().size())
					agroup.set(n, AspectIngredient.fromStack(recipe.getAspects().get(n)));
			}
		}
	}
}