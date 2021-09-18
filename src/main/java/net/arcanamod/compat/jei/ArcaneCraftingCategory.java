package net.arcanamod.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.UndecidedAspectStack;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.capabilities.Researcher;
import net.arcanamod.items.recipes.ArcaneCraftingShapedRecipe;
import net.arcanamod.systems.research.ResearchBooks;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

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

    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    @Override
    public void setIngredients(ArcaneCraftingShapedRecipe recipe, IIngredients iIngredients) {
        iIngredients.setInputLists(AspectIngredient.TYPE, Collections.singletonList(Arrays.stream(recipe.getAspectStacks()).map(AspectIngredient::fromUndecidedStack).collect(Collectors.toList())));
        iIngredients.setInputLists(VanillaTypes.ITEM,JEIIngredientStackListBuilder.make(recipe.getIngredients().toArray(new Ingredient[recipe.getIngredients().size()])).build());
        iIngredients.setInputIngredients(recipe.getIngredients());
        iIngredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, ArcaneCraftingShapedRecipe recipe, @Nonnull IIngredients iIngredients) {
        //Researcher.getFrom(Minecraft.getInstance().player).isPuzzleCompleted(ResearchBooks.puzzles.get(Arcana.arcLoc("flux_build_research")));
        //recipe.
        
        IGuiItemStackGroup igroup = iRecipeLayout.getItemStacks();
        IGuiIngredientGroup<AspectIngredient> agroup = iRecipeLayout.getIngredientsGroup(AspectIngredient.TYPE);
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
        
        for (UndecidedAspectStack stack : recipe.getAspectStacks()){
            Aspect aspect = stack.stack.getAspect();
            if (aspect == Aspects.AIR){
                agroup.init(0,true,46,8); // Air
                agroup.set(0, AspectIngredient.fromStack(stack.stack));
            }
            if (aspect == Aspects.FIRE){
                agroup.init(1,true,3,32); // Fire
                agroup.set(1, AspectIngredient.fromStack(stack.stack));
            }
            if (aspect == Aspects.EARTH){
                agroup.init(2,true,3,82); // Earth
                agroup.set(2, AspectIngredient.fromStack(stack.stack));
            }
            if (aspect == Aspects.WATER){
                agroup.init(3,true,89,32); // Water
                agroup.set(3, AspectIngredient.fromStack(stack.stack));
            }
            if (aspect == Aspects.ORDER){
                agroup.init(4,true,89,82); // Order
                agroup.set(4, AspectIngredient.fromStack(stack.stack));
            }
            if (aspect == Aspects.CHAOS){
                agroup.init(5,true,46,106); // Chaos
                agroup.set(5, AspectIngredient.fromStack(stack.stack));
            }
            if (stack.any){
                agroup.init(6,true,3,106); // Any
                agroup.set(6, AspectIngredient.fromStack(new AspectStack(Aspects.EXCHANGE,stack.stack.getAmount())));
            }
        }
    }
}