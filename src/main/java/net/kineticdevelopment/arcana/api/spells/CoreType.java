package net.kineticdevelopment.arcana.api.spells;

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
