package net.kineticdevelopment.arcana.api.aspects;

import net.kineticdevelopment.arcana.api.exception.AspectNotFoundException;

import javax.annotation.Nullable;

public class Aspect {

    public enum AspectType {
        AIR,
        ARMOR,
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

    @Nullable
    public static AspectType getAspectByName(@Nullable String name) throws AspectNotFoundException {
        switch (name.toUpperCase()) {
            default: throw new AspectNotFoundException("Aspect "+name+" does not exist!");
            case "AIR": return AspectType.AIR;
            case "ARMOR": return AspectType.ARMOR;
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
