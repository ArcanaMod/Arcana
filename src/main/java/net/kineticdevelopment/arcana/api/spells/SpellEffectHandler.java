package net.kineticdevelopment.arcana.api.spells;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles Spell Effects
 * 
 * @author Merijn
 */
public class SpellEffectHandler {

    private static List<ISpellEffect> effects = new ArrayList<>();


    @Nullable
    public static ISpellEffect getEffect(String effectType) {
        for(ISpellEffect effect : effects) {
            if(effect.getName().toUpperCase().equals(effectType.toUpperCase())) {
                return effect;
            }
        }
        return null;
    }

    public static void init() {
        effects.add(new SpellEffectEarth());
        effects.add(new SpellEffectFire());
    }

    public static List<ISpellEffect> getEffects() {
        return effects;
    }
}
