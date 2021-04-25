package net.arcanamod.items.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.AspectInfluencingRecipe;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.ItemAspectRegistry;
import net.arcanamod.capabilities.Researcher;
import net.arcanamod.systems.research.Parent;
import net.arcanamod.util.StreamUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.arcanamod.ArcanaVariables.arcLoc;

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
	List<Parent> requiredResearch;
	ResourceLocation id;
	
	public AlchemyRecipe(Ingredient in, ItemStack out, List<AspectStack> aspectsIn, List<Parent> requiredResearch, ResourceLocation id){
		this.in = in;
		this.out = out;
		this.aspectsIn = aspectsIn;
		this.requiredResearch = requiredResearch;
		this.id = id;
	}
	
	public boolean matches(AlchemyInventory inv, World world){
		// correct item
		return in.test(inv.stack)
				// and correct research
				&& requiredResearch.stream().allMatch(parent -> parent.satisfiedBy(Researcher.getFrom(inv.getCrafter())))
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
			List<Parent> research = StreamUtils.toStream(JSONUtils.getJsonArray(json, "research", null))
					.map(JsonElement::getAsString)
					.map(Parent::parse)
					.collect(Collectors.toList());
			return new AlchemyRecipe(ingredient, out, aspects, research, recipeId);
		}
		
		public AlchemyRecipe read(ResourceLocation recipeId, PacketBuffer buffer){
			Ingredient ingredient = Ingredient.read(buffer);
			ItemStack out = buffer.readItemStack();
			int size = buffer.readVarInt();
			List<AspectStack> aspects = new ArrayList<>(size);
			for(int i = 0; i < size; i++)
				aspects.add(new AspectStack(AspectUtils.getAspectByName(buffer.readString()), buffer.readVarInt()));
			
			size = buffer.readVarInt();
			List<Parent> requiredResearch = new ArrayList<>(size);
			for(int i = 0; i < size; i++)
				requiredResearch.add(Parent.parse(buffer.readString()));
			
			return new AlchemyRecipe(ingredient, out, aspects, requiredResearch, recipeId);
		}
		
		public void write(PacketBuffer buffer, AlchemyRecipe recipe){
			recipe.in.write(buffer);
			buffer.writeItemStack(recipe.out);
			buffer.writeVarInt(recipe.aspectsIn.size());
			for(AspectStack stack : recipe.aspectsIn){
				buffer.writeString(stack.getAspect().name());
				buffer.writeFloat(stack.getAmount());
			}
			buffer.writeVarInt(recipe.requiredResearch.size());
			for(Parent research : recipe.requiredResearch)
				buffer.writeString(research.asString());
		}
	}
}