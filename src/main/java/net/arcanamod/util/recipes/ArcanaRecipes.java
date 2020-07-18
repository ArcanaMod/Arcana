package net.arcanamod.util.recipes;

import net.arcanamod.items.recipes.AlchemyRecipe;
import net.arcanamod.items.recipes.WandsRecipe;
import net.minecraft.item.crafting.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.arcanamod.Arcana.MODID;

@SuppressWarnings("unused")
public class ArcanaRecipes{
	public static class Serializers{
		public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

		public static final RegistryObject<IRecipeSerializer<WandsRecipe>> CRAFTING_WANDS = SERIALIZERS.register("crafting_special_wands", () -> new SpecialRecipeSerializer<>(WandsRecipe::new));
		public static final RegistryObject<IRecipeSerializer<AlchemyRecipe>> ALCHEMY = SERIALIZERS.register("alchemy", AlchemyRecipe.Serializer::new);
		public static final RegistryObject<IRecipeSerializer<ArcaneCraftingShapedRecipe>> ARCANE_CRAFTING_SHAPED = SERIALIZERS.register("arcane_crafting", ArcaneCraftingShapedRecipe.Serializer::new);
	}
	public static class Types{
		public static final IRecipeType<IArcaneCraftingRecipe> ARCANE_CRAFTING_SHAPED = IRecipeType.register("arcana:arcane_crafting");
	}
}