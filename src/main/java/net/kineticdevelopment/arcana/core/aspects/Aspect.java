package net.kineticdevelopment.arcana.core.aspects;

import net.kineticdevelopment.arcana.common.items.AspectItem;
import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.item.Item;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains enum and utilities for aspects
 * @author Atlas
 */
public class Aspect {
    
    public static final List<Item> aspectItems = new ArrayList<>();
    public static final AspectType[] primalAspects = new AspectType[]{AspectType.AIR, AspectType.CHAOS, AspectType.EARTH, AspectType.FIRE, AspectType.ORDER, AspectType.WATER};
    
    // TODO: DON'T USE AN ENUM, use an ItemBase-like constructor to allow addons to add aspects.
    // Update research system to match.
	/**
	 * Enumeration containing all aspect types
	 *
	 * @author Atlas
	 */
    public enum AspectType {
        AIR,
        ARMOUR,
        AURA,
        BEAST,
        CHAOS,
        CRAFTING,
        CRYSTAL,
        DARKNESS,
        DEATH,
        EARTH,
        ELDRITCH,
        ENDER,
        ENERGY,
        ENVY,
        EXCHANGE,
        FABRIC,
        FIRE,
        FLESH,
        FLIGHT,
        GLUTTONY,
        GREED,
        HARVEST,
        HUMAN,
        ICE,
        IMPRISON,
        JOURNEY,
        LIFE,
        LIGHT,
        LUST,
        MACHINE,
        MANA,
        METAL,
        MINING,
        MIND,
        MOVEMENT,
        NETHER,
        ORDER,
        OVERWORLD,
        PLANT,
        PRIDE,
        SEEDS,
        SENSES,
        SLIME,
        SLOTH,
        SPIRIT,
        STRENGTH,
        TAINT,
        TOOL,
        TREE,
        UNDEAD,
        VACUUM,
        VENOM,
        WATER,
        WEAPON,
        WIND,
        WRATH
    }

    public static void register(){
        // Automatically register all aspects' items
        // TODO: this might break with addons, not finding the correct resources. maybe.
        // Addons should be able to create an assets/arcana/... directory and declare their own model & textures, I think.
        for(AspectType aspect : AspectType.values())
            aspectItems.add(new AspectItem("aspect_" + aspect.name().toLowerCase(), aspect.name().toLowerCase()).setCreativeTab(Main.TAB_ASPECTS_ARCANA));
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
    public static AspectType getAspectByName(@Nullable String name){
        if(name == null)
            return null;
        switch(name.toUpperCase()){
            default: return null;
            case "AIR": return AspectType.AIR;
            case "ARMOR": return AspectType.ARMOUR;
            case "AURA": return AspectType.AURA;
            case "BEAST": return AspectType.BEAST;
            case "CHAOS": return AspectType.CHAOS;
            case "CRAFTING": return AspectType.CRAFTING;
            case "CRYSTAL": return AspectType.CRYSTAL;
            case "DARKNESS": return AspectType.DARKNESS;
            case "DEATH": return AspectType.DEATH;
            case "EARTH": return AspectType.EARTH;
            case "ELDRITCH": return AspectType.ELDRITCH;
            case "ENDER": return AspectType.ENDER;
            case "ENVY": return AspectType.ENVY;
            case "EXCHANGE": return AspectType.EXCHANGE;
            case "FABRIC": return AspectType.FABRIC;
            case "FIRE": return AspectType.FIRE;
            case "FLESH": return AspectType.FLESH;
            case "FLIGHT": return AspectType.FLIGHT;
            case "GLUTTONY": return AspectType.GLUTTONY;
            case "GREED": return AspectType.GREED;
            case "HARVEST": return AspectType.HARVEST;
            case "HUMAN": return AspectType.HUMAN;
            case "ICE": return AspectType.ICE;
            case "IMPRISON": return AspectType.IMPRISON;
            case "JOURNEY": return AspectType.JOURNEY;
            case "LIFE": return AspectType.LIFE;
            case "LIGHT": return AspectType.LIGHT;
            case "LUST": return AspectType.LUST;
            case "MACHINE": return AspectType.MACHINE;
            case "MANA": return AspectType.MANA;
            case "METAL": return AspectType.METAL;
            case "MINING": return AspectType.MINING;
            case "MOVEMENT": return AspectType.MOVEMENT;
            case "NETHER": return AspectType.NETHER;
            case "ORDER": return AspectType.ORDER;
            case "PLANT": return AspectType.PLANT;
            case "PRIDE": return AspectType.PRIDE;
            case "SEEDS": return AspectType.SEEDS;
            case "SENSES": return AspectType.SENSES;
            case "SLIME": return AspectType.SLIME;
            case "SLOTH": return AspectType.SLOTH;
            case "SPIRIT": return AspectType.SPIRIT;
            case "STRENGTH": return AspectType.STRENGTH;
            case "TAINT": return AspectType.TAINT;
            case "TOOL": return AspectType.TOOL;
            case "TREE": return AspectType.TREE;
            case "UNDEAD": return AspectType.UNDEAD;
            case "VACUUM": return AspectType.VACUUM;
            case "VENOM": return AspectType.VENOM;
            case "WATER": return AspectType.WATER;
            case "WEAPON": return AspectType.WEAPON;
            case "WIND": return AspectType.WIND;
            case "WRATH": return AspectType.WRATH;
            case "OVERWORLD": return AspectType.OVERWORLD;
            case "ENERGY": return AspectType.ENERGY;
            case "MIND": return AspectType.MIND;
        }
    }
}
