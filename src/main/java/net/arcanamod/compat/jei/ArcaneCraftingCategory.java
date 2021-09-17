package net.arcanamod.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.items.recipes.ArcaneCraftingShapedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

import static net.arcanamod.ArcanaVariables.arcLoc;
import static net.arcanamod.compat.ArcanaJeiPlugin.ARCANE_WORKBENCH_UUID;

public class ArcaneCraftingCategory implements IRecipeCategory<ArcaneCraftingShapedRecipe> {
    private final IJeiHelpers jeiHelpers;

    public ArcaneCraftingCategory(IJeiHelpers jeiHelpers) {
        this.jeiHelpers = jeiHelpers;
    }

    @Override
    public ResourceLocation getUid() {
        return ARCANE_WORKBENCH_UUID;
    }

    @Override
    public Class<? extends ArcaneCraftingShapedRecipe> getRecipeClass() {
        return ArcaneCraftingShapedRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Arcane Crafting";
    }

    @Override
    public IDrawable getBackground() {
        return jeiHelpers.getGuiHelper().createDrawable(arcLoc("textures/gui/compat/arcanew.png"),0,0,130,130);
    }

    @Override
    public IDrawable getIcon() {
        return jeiHelpers.getGuiHelper().createDrawableIngredient(new ItemStack(ArcanaBlocks.ARCANE_CRAFTING_TABLE.get()));
    }

    @Override
    public void setIngredients(ArcaneCraftingShapedRecipe recipe, IIngredients iIngredients) {
        iIngredients.setInputLists(VanillaTypes.ITEM,JEIIngredientStackListBuilder.make(recipe.getIngredients().toArray(new Ingredient[recipe.getIngredients().size()])).build());
        iIngredients.setInputIngredients(recipe.getIngredients());
        iIngredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, ArcaneCraftingShapedRecipe recipe, IIngredients iIngredients) {
        IGuiItemStackGroup igroup = iRecipeLayout.getItemStacks();
        for (int i = 0; i < recipe.getRecipeHeight(); i++) {
            for (int j = 0; j < recipe.getRecipeWidth(); j++) {
                int n = (i*recipe.getRecipeWidth())+j;
                igroup.init(n,true,22+(j*23),33+(i*23));
                if (n < recipe.getIngredients().size())
                    igroup.set(n, Arrays.asList(recipe.getIngredients().get(n).getMatchingStacks()));
            }
        }
        igroup.init(10,false,107,56);
        igroup.set(10,recipe.getRecipeOutput());
    }
}