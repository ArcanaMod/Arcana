package net.arcanamod.util.recipes;

import net.arcanamod.Arcana;
import net.arcanamod.items.RecipeWands;
import net.minecraft.item.crafting.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.arcanamod.Arcana.MODID;

@SuppressWarnings("unused")
public class ArcanaRecipes{
	public static class Serializers{
		public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

		public static final RegistryObject<IRecipeSerializer<RecipeWands>> CRAFING_WANDS = SERIALIZERS.register("crafting_special_wands", () -> new SpecialRecipeSerializer<>(RecipeWands::new));
		public static final RegistryObject<IRecipeSerializer<ArcaneCraftingRecipe>> ARCANE_SHAPED = SERIALIZERS.register("arcane_crafting_shaped", ArcaneCraftingRecipe.Serializer::new);
	}
	public static class Types{
		public static final IRecipeType<ArcaneCraftingRecipe> ARCANE_CRAFTING = register("arcane_crafting");

		static <T extends IRecipe<?>> IRecipeType<T> register(final String key) {
			return Registry.register(Registry.RECIPE_TYPE, Arcana.arcLoc(key), new IRecipeType<T>() {
				public String toString() {
					return key;
				}
			});
		}
	}
}