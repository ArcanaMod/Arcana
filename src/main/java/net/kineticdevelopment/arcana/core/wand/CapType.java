package net.kineticdevelopment.arcana.core.wand;

/**
 * Enumeration for cap types
 *
 * @author Merijn
 */
// TODO: please stop using enums for these, and hardcoding references -- use interfaces or classes instead. makes things a chore for addon makers & improving the mod. thanks!
public enum CapType {

    ERROR(0,0, 0),
    IRON(2,  1, 1),
    GOLD(3,  2,2),
    THAUMIUM(6,  4,3),
    VOID(8,  4,4),
    COPPER(2,  1, 5),
    SILVER(3, 2, 6),
    ELEMENTIUM(4,  3, 7),
    MANASTEEL(4, 1, 8),
    TERRASTEEL(7, 3, 9);


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
