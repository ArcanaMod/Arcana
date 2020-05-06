package net.arcanamod.aspects;

import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.AspectItem;
import net.arcanamod.Arcana;
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
		for(Aspect aspect : Aspect.values()){
			AspectItem e = new AspectItem("aspect_" + aspect.name().toLowerCase());
			ArcanaItems.ITEMS.register("aspect_" + aspect.name().toLowerCase(), () -> e);
			aspectItems.add(e);//.setCreativeTab(Arcana.TAB_ASPECTS_ARCANA));
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
		switch(name.toUpperCase()){
			default:
				return null;
			case "AIR":
				return Aspect.AIR;
			case "ARMOR":
				return Aspect.ARMOUR;
			case "AURA":
				return Aspect.AURA;
			case "BEAST":
				return Aspect.BEAST;
			case "CHAOS":
				return Aspect.CHAOS;
			case "CRAFTING":
				return Aspect.CRAFTING;
			case "CRYSTAL":
				return Aspect.CRYSTAL;
			case "DARKNESS":
				return Aspect.DARKNESS;
			case "DEATH":
				return Aspect.DEATH;
			case "EARTH":
				return Aspect.EARTH;
			case "ELDRITCH":
				return Aspect.ELDRITCH;
			case "ENDER":
				return Aspect.ENDER;
			case "ENVY":
				return Aspect.ENVY;
			case "EXCHANGE":
				return Aspect.EXCHANGE;
			case "FABRIC":
				return Aspect.FABRIC;
			case "FIRE":
				return Aspect.FIRE;
			case "FLESH":
				return Aspect.FLESH;
			case "FLIGHT":
				return Aspect.FLIGHT;
			case "GLUTTONY":
				return Aspect.GLUTTONY;
			case "GREED":
				return Aspect.GREED;
			case "HARVEST":
				return Aspect.HARVEST;
			case "HUMAN":
				return Aspect.HUMAN;
			case "ICE":
				return Aspect.ICE;
			case "IMPRISON":
				return Aspect.IMPRISON;
			case "JOURNEY":
				return Aspect.JOURNEY;
			case "LIFE":
				return Aspect.LIFE;
			case "LIGHT":
				return Aspect.LIGHT;
			case "LUST":
				return Aspect.LUST;
			case "MACHINE":
				return Aspect.MACHINE;
			case "MANA":
				return Aspect.MANA;
			case "METAL":
				return Aspect.METAL;
			case "MINING":
				return Aspect.MINING;
			case "MOVEMENT":
				return Aspect.MOVEMENT;
			case "NETHER":
				return Aspect.NETHER;
			case "ORDER":
				return Aspect.ORDER;
			case "PLANT":
				return Aspect.PLANT;
			case "PRIDE":
				return Aspect.PRIDE;
			case "SEEDS":
				return Aspect.SEEDS;
			case "SENSES":
				return Aspect.SENSES;
			case "SLIME":
				return Aspect.SLIME;
			case "SLOTH":
				return Aspect.SLOTH;
			case "SPIRIT":
				return Aspect.SPIRIT;
			case "STRENGTH":
				return Aspect.STRENGTH;
			case "TAINT":
				return Aspect.TAINT;
			case "TOOL":
				return Aspect.TOOL;
			case "TREE":
				return Aspect.TREE;
			case "UNDEAD":
				return Aspect.UNDEAD;
			case "VACUUM":
				return Aspect.VACUUM;
			case "VENOM":
				return Aspect.VENOM;
			case "WATER":
				return Aspect.WATER;
			case "WEAPON":
				return Aspect.WEAPON;
			case "WIND":
				return Aspect.WIND;
			case "WRATH":
				return Aspect.WRATH;
			case "OVERWORLD":
				return Aspect.OVERWORLD;
			case "ENERGY":
				return Aspect.ENERGY;
			case "MIND":
				return Aspect.MIND;
		}
	}
	
	public static boolean areAspectsConnected(Aspect a, Aspect b){
		if(a != null)
			if(b != null){
				return Aspect.combinations.inverse().getOrDefault(a, Pair.of(null, null)).contains(b) || Aspect.combinations.inverse().getOrDefault(b, Pair.of(null, null)).contains(a);
			}
		return false;
	}
}
