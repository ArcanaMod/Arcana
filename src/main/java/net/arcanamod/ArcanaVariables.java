package net.arcanamod;

import net.minecraft.util.ResourceLocation;

public class ArcanaVariables {
    public static ResourceLocation arcLoc(String path){
        return new ResourceLocation("arcana", path);
    }

    /**
     * Enable this if you are using junit tests
     */
    public static final boolean test = false;

    private static int pNextAspectId = 0;

    @Deprecated
    public static int nextAspectId() {
        return pNextAspectId++;
    }
}
