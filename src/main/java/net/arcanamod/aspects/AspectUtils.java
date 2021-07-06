package net.arcanamod.aspects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.arcanamod.Arcana;
import net.arcanamod.ArcanaVariables;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.AspectItem;
import net.arcanamod.items.CrystalItem;
import net.arcanamod.systems.spell.casts.Casts;
import net.arcanamod.systems.spell.casts.ICast;
import net.arcanamod.util.Pair;
import net.arcanamod.util.StreamUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains enum and util for aspects
 *
 * @author Atlas
 */
public class AspectUtils {
	
	public static final List<Item> aspectItems = new ArrayList<>();
	public static final Map<Aspect, Item> aspectCrystalItems = new HashMap<>();
	public static final Aspect[] primalAspects = new Aspect[]{Aspects.AIR, Aspects.CHAOS, Aspects.EARTH, Aspects.FIRE, Aspects.ORDER, Aspects.WATER};
	public static final Aspect[] sinAspects = new Aspect[]{Aspects.ENVY, Aspects.LUST, Aspects.SLOTH, Aspects.PRIDE, Aspects.GREED, Aspects.WRATH, Aspects.GLUTTONY};
	public static List<ItemStack> aspectStacks;

	@SuppressWarnings("deprecation")
	public static void register(){
		// Automatically register all aspects' items
		// Addons should be able to create an assets/arcana/... directory and declare their own model & textures, I think.
		for(Aspect aspect : Aspects.getAll())
			if(aspect != Aspects.EMPTY){
				AspectItem item = new AspectItem("aspect_" + aspect.name().toLowerCase());
				ArcanaItems.ITEMS.register("aspect_" + aspect.name().toLowerCase(), () -> item);
				aspectItems.add(item);
				Item crystal = new CrystalItem(new Item.Properties().group(Arcana.ITEMS), aspect);
				ArcanaItems.ITEMS.register(aspect.name().toLowerCase() + "_crystal", () -> crystal);
				aspectCrystalItems.put(aspect, crystal);
			}
		aspectStacks = aspectItems.stream().map(ItemStack::new).collect(Collectors.toList());
	}
	
	public static ItemStack getItemStackForAspect(Aspect aspect){
		int i = Aspects.getWithoutEmpty().indexOf(aspect);
		if(i < 0 || i > aspectStacks.size())
			return ItemStack.EMPTY;
		return aspectStacks.get(i);
	}
	
	/**
	 * Utility for getting an aspect by name. If there is no aspect with the given name,
	 * this returns null.
	 * <p>
	 * If `name` is null, returns null. This method is not case sensitive.
	 *
	 * @param name
	 * 		The name of the aspect.
	 * @return The aspect with that name, or null.
	 */
	@Nullable
	public static Aspect getAspectByName(@Nullable String name){
		if(name == null)
			return null;
		for(Aspect aspect : Aspects.getAll())
			if(aspect.name().equalsIgnoreCase(name))
				return aspect;
		return null;
	}
	
	public static boolean areAspectsConnected(Aspect a, Aspect b){
		if(a != null)
			if(b != null)
				return Aspects.COMBINATIONS.inverse().getOrDefault(a, Pair.of(null, null)).contains(b) || Aspects.COMBINATIONS.inverse().getOrDefault(b, Pair.of(null, null)).contains(a);
		return false;
	}

	public static ResourceLocation getAspectTextureLocation(Aspect aspect) {
		return ArcanaVariables.arcLoc("textures/aspect/" + aspect.name().toLowerCase() + ".png");
	}

	public static int getEmptyCell(IAspectHandler handler) {
		return handler.findIndexesFromAspectInHolders(Aspects.EMPTY)[0];
	}
	
	public static String getLocalizedAspectDisplayName(@Nonnull Aspect aspect) {
		return I18n.format("aspect." + aspect.name().toLowerCase());
	}

	public static void putAspect(CompoundNBT compound, String key, Aspect aspect){
		compound.putString(key, aspect.toResourceLocation().toString());
	}

	public static Aspect getAspect(CompoundNBT compound, String key){
		return Aspect.fromResourceLocation(new ResourceLocation(compound.getString(key)));
	}

	public static String aspectHandlerToJson(IAspectHandler handler) {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();
		return gson.toJson(handler.getHolders());
	}

	public static Aspect getAspectByDisplayName(String name) {
		if(name == null)
			return null;
		for(Aspect aspect : Aspects.getAll())
			if(I18n.format("aspect."+aspect.name()).equalsIgnoreCase(name))
				return aspect;
		return null;
	}

	public static List<AspectStack> squish(List<AspectStack> unSquished){
		return StreamUtils.partialReduce(unSquished, AspectStack::getAspect, (left, right) -> new AspectStack(left.getAspect(), left.getAmount() + right.getAmount()));
	}

	public static List<Aspect> castContaingAspects() {
		return Casts.castMap.values().stream().map(ICast::getSpellAspect).collect(Collectors.toList());
	}

	public static HashMap<Aspect, String> fociForgeTooltip = new HashMap<>();

	static{
		// Primal aspects
		fociForgeTooltip.put(Aspects.AIR, "Creates Cloud (Lingering effect)\nCan not target blocks.");
		fociForgeTooltip.put(Aspects.CHAOS, "Creates a mid ranged lightning bolt\nTargets the block / entity hit every few ticks");
		fociForgeTooltip.put(Aspects.EARTH, "Allows the player to select a block from far away.\nCan not target entities.");
		fociForgeTooltip.put(Aspects.FIRE, "Fires a projectiles with a long range.\nTargets the block / entity hit");
		fociForgeTooltip.put(Aspects.ORDER, "Targets yourself");
		fociForgeTooltip.put(Aspects.WATER, "Creates an AOE blast that targets any entity hit.\nCan not target blocks.");

		// The sins
		// I tried to make hints, yes I bad in naming things
		// TODO make hits better
		fociForgeTooltip.put(Aspects.ENVY,"Infects with spell targeted entities, targets nearby objects");
		fociForgeTooltip.put(Aspects.LUST,"Targets passive mob or pets");
		fociForgeTooltip.put(Aspects.SLOTH,"Makes a trap, spell bounces");
		fociForgeTooltip.put(Aspects.PRIDE,"Randomness and spread");
		fociForgeTooltip.put(Aspects.GREED,"Binds to hostile mobs, Targets underneath");
		fociForgeTooltip.put(Aspects.WRATH,"Generates movement, Inverts working or Spell stays on you");
		fociForgeTooltip.put(Aspects.GLUTTONY,"Accelerates more particles, Gives a lage area, Your soul goes to dropped items");
	}
}
