package net.arcanamod.systems.spell;

public class SpellNotBuiltError extends Exception {
    public SpellNotBuiltError(){
        System.exit(-1); // CRASH APPLICATION
    }
}
