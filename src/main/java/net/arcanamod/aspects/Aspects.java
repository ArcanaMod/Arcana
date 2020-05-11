package net.arcanamod.aspects;

import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.AspectItem;
import net.arcanamod.Arcana;
import net.arcanamod.items.PhialItem;
import net.arcanamod.util.Pair;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.awt.*;
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

	public static final List<Item> phialItems = new ArrayList<>();
	public static List<ItemStack> phialStacks;
	
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
			PhialItem p = new PhialItem("phial_" + aspect.name().toLowerCase());
			ArcanaItems.ITEMS.register(aspect.name().toLowerCase()+"_phial", () -> p);
			phialItems.add(p);//.setCreativeTab(Arcana.TAB_ASPECTS_ARCANA));
		}
		aspectStacks = aspectItems.stream().map(ItemStack::new).collect(Collectors.toList());
		phialStacks = phialItems.stream().map(ItemStack::new).collect(Collectors.toList());
	}
	
	public static ItemStack getItemStackForAspect(Aspect aspect){
		return aspectStacks.get(Aspect.aspects.indexOf(aspect));
	}

	public static ItemStack getPhialItemStackForAspect(Aspect aspect){
		return phialStacks.get(Aspect.aspects.indexOf(aspect));
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

	/**
	 * Utility for getting an aspect color.
	 * <p>
	 * If `aspect` is null, returns null. This method is not case sensitive.
	 *
	 * @param aspect
	 * 		aspect has color of not.
	 * @return The color of an aspect.
	 */
	@Nullable
	public static Color getAspectColor(@Nullable Aspect aspect){
		if(aspect == null)
			return null;
		switch(aspect){
			default:
				return null;
			case AIR:
				return Color.decode("#FFFFFF");
			case ARMOUR:
				return Color.decode("#FFFFFF");
			case AURA:
				return Color.decode("#FFFFFF");
			case BEAST:
				return Color.decode("#FFFFFF");
			case CHAOS:
				return Color.decode("#FFFFFF");
			case CRAFTING:
				return Color.decode("#FFFFFF");
			case CRYSTAL:
				return Color.decode("#FFFFFF");
			case DARKNESS:
				return Color.decode("#FFFFFF");
			case DEATH:
				return Color.decode("#FFFFFF");
			case EARTH:
				return Color.decode("#FFFFFF");
			case ELDRITCH:
				return Color.decode("#FFFFFF");
			case ENDER:
				return Color.decode("#FFFFFF");
			case ENVY:
				return Color.decode("#FFFFFF");
			case EXCHANGE:
				return Color.decode("#FFFFFF");
			case FABRIC:
				return Color.decode("#FFFFFF");
			case FIRE:
				return Color.decode("#FFFFFF");
			case FLESH:
				return Color.decode("#FFFFFF");
			case FLIGHT:
				return Color.decode("#FFFFFF");
			case GLUTTONY:
				return Color.decode("#FFFFFF");
			case GREED:
				return Color.decode("#F8F811");
			case HARVEST:
				return Color.decode("#FFFFFF");
			case HUMAN:
				return Color.decode("#FFFFFF");
			case ICE:
				return Color.decode("#FFFFFF");
			case IMPRISON:
				return Color.decode("#FFFFFF");
			case JOURNEY:
				return Color.decode("#FFFFFF");
			case LIFE:
				return Color.decode("#FFFFFF");
			case LIGHT:
				return Color.decode("#FFFFFF");
			case LUST:
				return Color.decode("#FFFFFF");
			case MACHINE:
				return Color.decode("#FFFFFF");
			case MANA:
				return Color.decode("#FFFFFF");
			case METAL:
				return Color.decode("#FFFFFF");
			case MINING:
				return Color.decode("#FFFFFF");
			case MOVEMENT:
				return Color.decode("#FFFFFF");
			case NETHER:
				return Color.decode("#FFFFFF");
			case ORDER:
				return Color.decode("#FFFFFF");
			case PLANT:
				return Color.decode("#FFFFFF");
			case PRIDE:
				return Color.decode("#FFFFFF");
			case SEEDS:
				return Color.decode("#FFFFFF");
			case SENSES:
				return Color.decode("#FFFFFF");
			case SLIME:
				return Color.decode("#FFFFFF");
			case SLOTH:
				return Color.decode("#FFFFFF");
			case SPIRIT:
				return Color.decode("#FFFFFF");
			case STRENGTH:
				return Color.decode("#FFFFFF");
			case TAINT:
				return Color.decode("#FFFFFF");
			case TOOL:
				return Color.decode("#FFFFFF");
			case TREE:
				return Color.decode("#FFFFFF");
			case UNDEAD:
				return Color.decode("#FFFFFF");
			case VACUUM:
				return Color.decode("#FFFFFF");
			case VENOM:
				return Color.decode("#FFFFFF");
			case WATER:
				return Color.decode("#FFFFFF");
			case WEAPON:
				return Color.decode("#FFFFFF");
			case WIND:
				return Color.decode("#FFFFFF");
			case WRATH:
				return Color.decode("#FFFFFF");
			case OVERWORLD:
				return Color.decode("#FFFFFF");
			case ENERGY:
				return Color.decode("#FFFFFF");
			case MIND:
				return Color.decode("#FFFFFF");
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
