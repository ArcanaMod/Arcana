package net.arcanamod.aspects;

import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.AspectItem;
import net.arcanamod.util.Pair;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains enum and util for aspects
 *
 * @author Atlas
 */
public class Aspects{
	
	public static final List<Item> aspectItems = new ArrayList<>();
	public static final Aspect[] primalAspects = new Aspect[]{Aspect.AIR, Aspect.CHAOS, Aspect.EARTH, Aspect.FIRE, Aspect.ORDER, Aspect.WATER};
	public static List<ItemStack> aspectStacks;
	
	// TODO: DON'T USE AN ENUM, use an ItemBase-like constructor to allow addons to add aspects.
	// Update research system to match.
	
	public static void register(){
		// Automatically register all aspects' items
		// TODO: this might break with addons, not finding the correct resources. maybe.
		// Addons should be able to create an assets/arcana/... directory and declare their own model & textures, I think.
		for(Aspect aspect : Aspect.values())
			if(aspect != Aspect.EMPTY){
				AspectItem e = new AspectItem("aspect_" + aspect.name().toLowerCase());
				ArcanaItems.ITEMS.register("aspect_" + aspect.name().toLowerCase(), () -> e);
				aspectItems.add(e);
			}
		aspectStacks = aspectItems.stream().map(ItemStack::new).collect(Collectors.toList());
	}
	
	public static ItemStack getItemStackForAspect(Aspect aspect){
		return aspectStacks.get(Aspect.aspects.indexOf(aspect));
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
		for(Aspect aspect : Aspect.values())
			if(aspect.name().equalsIgnoreCase(name))
				return aspect;
		return null;
	}
	
	public static boolean areAspectsConnected(Aspect a, Aspect b){
		if(a != null)
			if(b != null)
				return Aspect.combinations.inverse().getOrDefault(a, Pair.of(null, null)).contains(b) || Aspect.combinations.inverse().getOrDefault(b, Pair.of(null, null)).contains(a);
		return false;
	}

	public static int getEmptyCell(IAspectHandler handler) {
		return handler.findIndexFromAspectInHolders(Aspect.EMPTY);
	}
}
