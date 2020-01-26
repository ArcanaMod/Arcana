package net.kineticdevelopment.arcana.core.wand;

/**
 * Enumeration for cap types
 * 
 * @author Merijn
 */
public enum CapType {

    ERROR(0,0, 0),
    IRON(2,  1, 1),
    GOLD(3,  2,2),
    THAUMIUM(6,  4,3),
    VOID(8,  4,4),
    COPPER(2,  1, 5),
    ELEMENTIUM(4,  3, 6),
    MANASTEEL(4, 1, 7),
    TERRASTEEL(7, 3, 8);


    @SuppressWarnings("unused")
	private int maxPower, maxEffects, id;

    CapType(int maxPower, int maxEffects, int id) {
        this.maxPower = maxPower;
        this.maxEffects = maxEffects;
        this.id = id;
    }

    public int getMaxPower() {
        return maxPower;
    }


    public int getMaxEffects() {
        return maxEffects;
    }
}
