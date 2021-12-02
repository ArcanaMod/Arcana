package net.arcanamod.systems.research.impls;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.handlers.AspectHandler;
import net.arcanamod.containers.ResearchTableContainer;
import net.arcanamod.containers.slots.AspectSlot;
import net.arcanamod.systems.research.Puzzle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Supplier;

public class Guesswork extends Puzzle{
	
	private static final ResourceLocation ICON = new ResourceLocation(Arcana.MODID, "textures/item/research_note.png");
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String TYPE = "guesswork", BG_NAME = "arcana:gui/container/unknown_slot";
	
	// Check RecipeSectionRenderer for how non-crafting recipes are handled.
	protected ResourceLocation recipe;
	protected Map<ResourceLocation, String> hints;
	
	public Guesswork(){
	}
	
	public Guesswork(ResourceLocation recipe, Map<ResourceLocation, String> hints){
		this.recipe = recipe;
		this.hints = hints;
	}
	
	public void load(JsonObject data, ResourceLocation file){
		ResourceLocation recipe = new ResourceLocation(data.get("recipe").getAsString());
		JsonArray hints = data.getAsJsonArray("hints");
		Map<ResourceLocation, String> hintMap = new LinkedHashMap<>();
		for(JsonElement hint : hints){
			if(hint.isJsonPrimitive()){
				String hintSt = hint.getAsString();
				if(hintSt.contains("=")){
					ResourceLocation key = new ResourceLocation(hintSt.split("=")[0]);
					String value = hintSt.split("=")[1];
					hintMap.put(key, value);
				}else
					LOGGER.error("String not containing \"=\" found in puzzle in " + file + "!");
			}else
				LOGGER.error("Non-String found in hints array in puzzle in " + file + "!");
		}
		this.recipe = recipe;
		this.hints = hintMap;
	}
	
	public CompoundNBT getData(){
		CompoundNBT compound = new CompoundNBT();
		CompoundNBT hints = new CompoundNBT();
		getHints().forEach((location, s) -> hints.putString(location.toString(), s));
		compound.putString("recipe", getRecipe().toString());
		compound.put("hints", hints);
		return compound;
	}
	
	public static Guesswork fromNBT(CompoundNBT passData){
		ResourceLocation recipe = new ResourceLocation(passData.getString("recipe"));
		Map<ResourceLocation, String> hints = new LinkedHashMap<>();
		CompoundNBT serialHints = passData.getCompound("hints");
		for(String s : serialHints.keySet())
			hints.put(new ResourceLocation(s), serialHints.getString(s));
		return new Guesswork(recipe, hints);
	}
	
	public String type(){
		return TYPE;
	}
	
	public ResourceLocation getRecipe(){
		return recipe;
	}
	
	public Map<ResourceLocation, String> getHints(){
		return Collections.unmodifiableMap(hints);
	}
	
	public String getDefaultDesc(){
		return "requirement.guesswork";
	}
	
	public ResourceLocation getDefaultIcon(){
		return ICON;
	}
	
	public List<Puzzle.SlotInfo> getItemSlotLocations(PlayerEntity player){
		List<Puzzle.SlotInfo> ret = new ArrayList<>();
		IRecipe<?> recipe = player.world.getRecipeManager().getRecipe(getRecipe()).orElse(null);
		if(recipe != null)
			for(int y = 0; y < 3; y++)
				for(int x = 0; x < 3; x++){
					int xx = x * 23;
					int yy = y * 23;
					int scX = xx + 141 + 75;
					int scY = yy + 35 + 54;
					if(recipe.getIngredients().size() > (x + y * 3) && recipe.getIngredients().get(x + y * 3).getMatchingStacks().length > 0)
						ret.add(new SlotInfo(scX, scY, 1, BG_NAME));
					else
						ret.add(new SlotInfo(scX, scY));
				}
		return ret;
	}
	
	public List<AspectSlot> getAspectSlots(Supplier<AspectHandler> returnInv){
		return Collections.emptyList();
	}
	
	public boolean validate(List<AspectSlot> aspectSlots, List<Slot> itemSlots, PlayerEntity player, ResearchTableContainer container){
		if(player == null)
			return false;
		IRecipe<?> recipe = player.world.getRecipeManager().getRecipe(getRecipe()).orElse(null);
		if(!(recipe instanceof ICraftingRecipe))
			return false;
		CraftingInventory inv = new CraftingInventory(container, 3, 3);
		for(int i = 0; i < itemSlots.size(); i++){
			Slot slot = itemSlots.get(i);
			if (slot.getStack()!=null)
				inv.setInventorySlotContents(i, slot.getStack());
		}
		return ((ICraftingRecipe)recipe).matches(inv, player.world);
	}
	
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(!(o instanceof Guesswork))
			return false;
		Guesswork guesswork = (Guesswork)o;
		return getRecipe().equals(guesswork.getRecipe()) && getHints().equals(guesswork.getHints());
	}
	
	public int hashCode(){
		return Objects.hash(getRecipe(), getHints());
	}
}