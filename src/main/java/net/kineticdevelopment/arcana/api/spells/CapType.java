package net.kineticdevelopment.arcana.api.spells;

public enum CapType {

    ERROR(0,0),
    IRON(2,  1),
    GOLD(3,  2),
    THAUMIUM(6,  4),
    VOID(8,  4),
    COPPER(2,  1),
    ELEMENTIUM(4,  3),
    MANASTEEL(4, 1),
    TERRASTEEL(7, 3);


    private int maxPower, maxEffects;

    CapType(int maxPower, int maxEffects) {
        this.maxPower = maxPower;
        this.maxEffects = maxEffects;
    }

    public int getMaxPower() {
        return maxPower;
    }


    public int getMaxEffects() {
        return maxEffects;
    }
}
