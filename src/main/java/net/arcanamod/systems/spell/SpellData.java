package net.arcanamod.systems.spell;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.util.Pair;

public class SpellData {
    public Aspect firstModifier;
    public Aspect secondModifier;
    public Aspect sinModifier;

    public Pair<Aspect,Aspect> primaryCast;
    public Pair<Aspect,Aspect> plusCast;

    public SpellData(Aspect firstModifier, Aspect secondModifier, Aspect sinModifier,
                     Pair<Aspect,Aspect> primaryCast, Pair<Aspect,Aspect> plusCast){
        this.firstModifier = firstModifier;
        this.secondModifier = secondModifier;
        this.sinModifier = sinModifier;
        this.primaryCast = primaryCast;
        this.plusCast = plusCast;
    }
}
