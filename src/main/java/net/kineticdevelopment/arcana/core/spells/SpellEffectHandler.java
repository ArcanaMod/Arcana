package net.kineticdevelopment.arcana.core.spells;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.kineticdevelopment.arcana.core.spells.effects.ISpellEffect;
import net.kineticdevelopment.arcana.core.spells.effects.SpellEffectEarth;
import net.kineticdevelopment.arcana.core.spells.effects.SpellEffectFire;

/**
 * Handles Spell Effects
 * 
 * @author Merijn
 */
public class SpellEffectHandler {

    private static List<ISpellEffect> effects = new ArrayList<>();

    /**
     * Get a {@link ISpellEffect} based on the effects name
     * @param name Name of the effect
     * @return {@link ISpellEffect} instance with the given name. If it doesnt exists returns null
     */
    @Nullable
    public static ISpellEffect getEffect(String name) {
        for(ISpellEffect effect : effects) {
            if(effect.getName().toUpperCase().equals(name.toUpperCase())) {
                return effect;
            }
        }
        return null;
    }

    public static void init() {
        effects.add(new SpellEffectEarth());
        effects.add(new SpellEffectFire());
    }

    /**
     * Getter of a List with all the registered effects
     * @return
     */
    public static List<ISpellEffect> getEffects() {
        return effects;
    }
}
