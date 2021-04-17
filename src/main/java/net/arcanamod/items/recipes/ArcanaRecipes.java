package net.arcanamod.items.recipes;

import net.minecraft.item.crafting.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.arcanamod.Arcana.MODID;

public class ArcanaRecipes{
	public static class Serializers{
		public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

		public static final RegistryObject<IRecipeSerializer<WandsRecipe>> CRAFTING_WANDS = SERIALIZERS.register("crafting_special_wands", () -> new SpecialRecipeSerializer<>(WandsRecipe::new));
		public static final RegistryObject<IRecipeSerializer<AlchemyRecipe>> ALCHEMY = SERIALIZERS.register("alchemy", AlchemyRecipe.Serializer::new);
		public static final RegistryObject<IRecipeSerializer<ArcaneCraftingShapedRecipe>> ARCANE_CRAFTING_SHAPED = SERIALIZERS.register("arcane_crafting_shaped", ArcaneCraftingShapedRecipe.Serializer::new);
	}
	public static class Types{
		public static final IRecipeType<IArcaneCraftingRecipe> ARCANE_CRAFTING_SHAPED = IRecipeType.register("arcana:arcane_crafting_shaped");
	}
}