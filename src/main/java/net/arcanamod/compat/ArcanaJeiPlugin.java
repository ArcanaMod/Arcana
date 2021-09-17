package net.arcanamod.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.compat.jei.ArcaneCraftingCategory;
import net.arcanamod.compat.jei.CrystalStudyCategory;
import net.arcanamod.compat.jei.DummyRecipe;
import net.arcanamod.items.recipes.ArcaneCraftingShapedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;

import static net.arcanamod.ArcanaVariables.arcLoc;

@JeiPlugin
public class ArcanaJeiPlugin implements IModPlugin {

    public static final ResourceLocation ARCANE_WORKBENCH_UUID = arcLoc("arcane_crafting_jei");
    public static final ResourceLocation CRYSTAL_UUID = arcLoc("crystal_study");

    @Override
    public ResourceLocation getPluginUid() {
        return arcLoc("jei");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IModPlugin.super.registerRecipes(registration);
        List<ArcaneCraftingShapedRecipe> castingTableRecipes = ArcaneCraftingShapedRecipe.RECIPES;
        registration.addRecipes(castingTableRecipes, ARCANE_WORKBENCH_UUID);
        registration.addRecipes(Collections.singletonList(new DummyRecipe()), CRYSTAL_UUID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IModPlugin.super.registerCategories(registration);
        registration.addRecipeCategories(new ArcaneCraftingCategory(registration.getJeiHelpers()));
        registration.addRecipeCategories(new CrystalStudyCategory(registration.getJeiHelpers()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        IModPlugin.super.registerRecipeCatalysts(registration);
        registration.addRecipeCatalyst(new ItemStack(ArcanaBlocks.ARCANE_CRAFTING_TABLE.get()),ARCANE_WORKBENCH_UUID);
        registration.addRecipeCatalyst(new ItemStack(ArcanaBlocks.AIR_CLUSTER.get()),CRYSTAL_UUID);
        registration.addRecipeCatalyst(new ItemStack(ArcanaBlocks.FIRE_CLUSTER.get()),CRYSTAL_UUID);
        registration.addRecipeCatalyst(new ItemStack(ArcanaBlocks.WATER_CLUSTER.get()),CRYSTAL_UUID);
        registration.addRecipeCatalyst(new ItemStack(ArcanaBlocks.EARTH_CLUSTER.get()),CRYSTAL_UUID);
        registration.addRecipeCatalyst(new ItemStack(ArcanaBlocks.CHAOS_CLUSTER.get()),CRYSTAL_UUID);
        registration.addRecipeCatalyst(new ItemStack(ArcanaBlocks.ORDER_CLUSTER.get()),CRYSTAL_UUID);
    }
}