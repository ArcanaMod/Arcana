package net.arcanamod.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;

import static net.arcanamod.ArcanaVariables.arcLoc;
import static net.arcanamod.compat.ArcanaJeiPlugin.CRYSTAL_UUID;

public class CrystalStudyCategory implements IRecipeCategory<DummyRecipe> {
    private final IJeiHelpers jeiHelpers;

    public CrystalStudyCategory(IJeiHelpers jeiHelpers) {
        this.jeiHelpers = jeiHelpers;
    }

    @Override
    public ResourceLocation getUid() {
        return CRYSTAL_UUID;
    }

    @Override
    public Class<? extends DummyRecipe> getRecipeClass() {
        return DummyRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Crystal Study";
    }

    @Override
    public IDrawable getBackground() {
        return jeiHelpers.getGuiHelper().createDrawable(arcLoc("textures/gui/compat/crystal.png"),0,0,70,41);
    }

    @Override
    public IDrawable getIcon() {
        return jeiHelpers.getGuiHelper().createDrawableIngredient(new ItemStack(ArcanaBlocks.FIRE_CLUSTER.get()));
    }

    @Override
    public void setIngredients(DummyRecipe recipe, IIngredients iIngredients) {
        iIngredients.setInputLists(VanillaTypes.ITEM,JEIIngredientStackListBuilder.make(Ingredient.fromStacks(new ItemStack(Items.WRITABLE_BOOK))).build());
        iIngredients.setInputIngredients(Collections.singletonList(Ingredient.fromStacks(new ItemStack(Items.WRITABLE_BOOK))));
        iIngredients.setOutput(VanillaTypes.ITEM, new ItemStack(ArcanaItems.ARCANUM.get()));
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, DummyRecipe recipe, IIngredients iIngredients) {
        IGuiItemStackGroup igroup = iRecipeLayout.getItemStacks();
        igroup.init(0,true,3,22);
        igroup.set(0,new ItemStack(Items.WRITABLE_BOOK));
        igroup.init(1,false,49,22);
        igroup.set(1,new ItemStack(ArcanaItems.ARCANUM.get()));
    }
}