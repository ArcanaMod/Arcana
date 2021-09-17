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
    public void setIngredients(ArcaneCraftingShapedRecipe arcaneCraftingShapedRecipe, IIngredients iIngredients) {
        iIngredients.setInputLists(VanillaTypes.ITEM,JEIIngredientStackListBuilder.make(arcaneCraftingShapedRecipe.getIngredients().toArray(new Ingredient[arcaneCraftingShapedRecipe.getIngredients().size()])).build());
        iIngredients.setInputIngredients(arcaneCraftingShapedRecipe.getIngredients());
        iIngredients.setOutput(VanillaTypes.ITEM, arcaneCraftingShapedRecipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, ArcaneCraftingShapedRecipe arcaneCraftingShapedRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup igroup = iRecipeLayout.getItemStacks();
        for (int i = 0; i < 10; i++) {
            igroup.init(i,true,18+(i*12),20+(i/3*12));
        }
        igroup.init(10,true,10,20);
    }
}
