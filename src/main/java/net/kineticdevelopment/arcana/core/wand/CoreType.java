package net.kineticdevelopment.arcana.core.wand;

/**
 * Enumeration for Core Types
 *
 * @author Merijn
 */
// TODO: please stop using enums for these, and hardcoding references -- use interfaces or classes instead. makes things a chore for addon makers & improving the mod. thanks!
public enum CoreType {
    ERROR(0),
    WOOD(25),
    TAINTED(35),
    DAIR(30),
    HAWTHORN(30),
    SILVERWOOD(40),
    GREATWOOD(35),
    ARCANIUM(50);


    private int maxVis;

    CoreType(int maxVis) {
        this.maxVis = maxVis;
    }

    public int getMaxVis() {
        return maxVis;
    }
}
