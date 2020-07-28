package net.arcanamod.items.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.AspectInfluencingRecipe;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.ItemAspectRegistry;
import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.ResearchEntry;
import net.arcanamod.capabilities.Researcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.arcanamod.Arcana.arcLoc;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlchemyRecipe implements IRecipe<AlchemyInventory>, AspectInfluencingRecipe{
	
	// vanilla registry
	public static IRecipeType<AlchemyRecipe> ALCHEMY = Registry.register(Registry.RECIPE_TYPE, arcLoc("alchemy"), new IRecipeType<AlchemyRecipe>(){
		public String toString(){
			return arcLoc("alchemy").toString();
		}
	});
	
	Ingredient in;
	ItemStack out;
	List<AspectStack> aspectsIn;
	ResourceLocation required;
	ResourceLocation id;
	
	public AlchemyRecipe(Ingredient in, ItemStack out, List<AspectStack> aspectsIn, @Nullable ResourceLocation required, ResourceLocation id){
		this.in = in;
		this.out = out;
		this.aspectsIn = aspectsIn;
		this.required = required;
		this.id = id;
	}
	
	public boolean matches(AlchemyInventory inv, World world){
		ResearchEntry research = required != null ? ResearchBooks.getEntry(required) : null;
		// correct item
		return in.test(inv.stack)
				// and correct research
				&& (research == null || Researcher.getFrom(inv.getCrafter()).entryStage(research) >= research.sections().size())
				// and correct aspects
				&& aspectsIn.stream().allMatch(stack -> inv.getAspectMap().containsKey(stack.getAspect()) && inv.getAspectMap().get(stack.getAspect()).getAmount() >= stack.getAmount());
	}
	
	public ItemStack getCraftingResult(AlchemyInventory inv){
		return out.copy();
	}
	
	public boolean canFit(int width, int height){
		return true;
	}
	
	public ItemStack getRecipeOutput(){
		return out;
	}
	
	public ResourceLocation getId(){
		return id;
	}
	
	public List<AspectStack> getAspects(){
		return aspectsIn;
	}
	
	public NonNullList<Ingredient> getIngredients(){
		NonNullList<Ingredient> list = NonNullList.create();
		list.add(in);
		return list;
	}
	
	public IRecipeSerializer<?> getSerializer(){
		return ArcanaRecipes.Serializers.ALCHEMY.get();
	}
	
	public IRecipeType<?> getType(){
		return ALCHEMY;
	}
	
	public void influence(List<AspectStack> in){
		in.addAll(aspectsIn.stream().map(stack -> new AspectStack(stack.getAspect(), (int)(stack.getAmount() * ArcanaConfig.ALCHEMY_ASPECT_CARRY_FRACTION.get()))).collect(Collectors.toList()));
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AlchemyRecipe>{
		
		public AlchemyRecipe read(ResourceLocation recipeId, JsonObject json){
			Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "in"));
			ItemStack out = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "out"));
			List<AspectStack> aspects = ItemAspectRegistry.parseAspectStackList(recipeId, JSONUtils.getJsonArray(json, "aspects")).orElseThrow(() -> new JsonSyntaxException("Missing aspects in " + recipeId + "!"));
			String researchName = JSONUtils.getString(json, "research", "null");
			ResourceLocation research = !researchName.equals("null") ? new ResourceLocation(researchName) : null;
			return new AlchemyRecipe(ingredient, out, aspects, research, recipeId);
		}
		
		public AlchemyRecipe read(ResourceLocation recipeId, PacketBuffer buffer){
			Ingredient ingredient = Ingredient.read(buffer);
			ItemStack out = buffer.readItemStack();
			int size = buffer.readVarInt();
			List<AspectStack> aspects = new ArrayList<>(size);
			for(int i = 0; i < size; i++)
				aspects.add(new AspectStack(AspectUtils.getAspectByName(buffer.readString()), buffer.readVarInt()));
			String s = buffer.readString();
			ResourceLocation research = s.equals("null") ? null : new ResourceLocation(s);
			return new AlchemyRecipe(ingredient, out, aspects, research, recipeId);
		}
		
		public void write(PacketBuffer buffer, AlchemyRecipe recipe){
			recipe.in.write(buffer);
			buffer.writeItemStack(recipe.out);
			buffer.writeVarInt(recipe.aspectsIn.size());
			for(AspectStack stack : recipe.aspectsIn){
				buffer.writeString(stack.getAspect().name());
				buffer.writeVarInt(stack.getAmount());
			}
			buffer.writeString(recipe.required != null ? recipe.required.toString() : "null");
		}
	}
}