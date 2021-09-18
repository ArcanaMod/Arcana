package net.arcanamod.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;

import static net.arcanamod.ArcanaVariables.arcLoc;
import static net.arcanamod.compat.ArcanaJeiPlugin.ASPECT_CRYSTALLIZER_UUID;
import static net.arcanamod.compat.ArcanaJeiPlugin.CRYSTAL_UUID;

public class AspectCrystallizerCategory implements IRecipeCategory<AspectCrystallizerRecipeHandler> {
	private final IJeiHelpers jeiHelpers;
	
	public AspectCrystallizerCategory(IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}
	
	@Override
	public ResourceLocation getUid() {
		return ASPECT_CRYSTALLIZER_UUID;
	}
	
	@Override
	public Class<? extends AspectCrystallizerRecipeHandler> getRecipeClass() {
		return AspectCrystallizerRecipeHandler.class;
	}
	
	@Override
	public String getTitle() {
		return "Aspect Crystallizer";
	}
	
	@Override
	public IDrawable getBackground() {
		return jeiHelpers.getGuiHelper().createDrawable(arcLoc("textures/gui/compat/crystal.png"),0,43,94,34);
	}
	
	@Override
	public IDrawable getIcon() {
		return jeiHelpers.getGuiHelper().createDrawableIngredient(new ItemStack(ArcanaBlocks.ASPECT_CRYSTALLIZER.get()));
	}
	
	@Override
	public void setIngredients(AspectCrystallizerRecipeHandler recipe, IIngredients iIngredients) {
		iIngredients.setInputLists(AspectIngredient.TYPE,Collections.singletonList(Collections.singletonList(AspectIngredient.fromStack(new AspectStack(recipe.aspect,1)))));
		iIngredients.setOutput(VanillaTypes.ITEM, new ItemStack(AspectUtils.aspectCrystalItems.get(recipe.aspect)));
	}
	
	@Override
	public void setRecipe(IRecipeLayout iRecipeLayout, AspectCrystallizerRecipeHandler recipe, IIngredients iIngredients) {
		IGuiItemStackGroup igroup = iRecipeLayout.getItemStacks();
		IGuiIngredientGroup<AspectIngredient> agroup = iRecipeLayout.getIngredientsGroup(AspectIngredient.TYPE);
		agroup.init(0,true,8,9);
		agroup.set(0, AspectIngredient.fromStack(new AspectStack(recipe.aspect,1)));
		igroup.init(0,false,67,8);
		igroup.set(0,new ItemStack(AspectUtils.aspectCrystalItems.get(recipe.aspect),1));
	}
}
