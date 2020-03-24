package net.kineticdevelopment.arcana.client.particles;

import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public enum  EnumArcanaParticles {

    NORMAL_NODE("normal_node_particle", 990, true),

    ;


    private final String particleName;
    private final int particleID;
    private final boolean shouldIgnoreRange;
    private final int argumentCount;
    private static final Map<Integer, EnumArcanaParticles> PARTICLES = Maps.newHashMap();
    private static final Map<String, EnumArcanaParticles> BY_NAME = Maps.newHashMap();

    EnumArcanaParticles(String particleNameIn, int particleIDIn, boolean shouldIgnoreRangeIn, int argumentCountIn)
    {
        this.particleName = particleNameIn;
        this.particleID = particleIDIn;
        this.shouldIgnoreRange = shouldIgnoreRangeIn;
        this.argumentCount = argumentCountIn;
    }


    EnumArcanaParticles(String particleNameIn, int particleIDIn, boolean shouldIgnoreRangeIn)
    {
        this(particleNameIn, particleIDIn, shouldIgnoreRangeIn, 0);
    }

    public static Set<String> getParticleNames()
    {
        return BY_NAME.keySet();
    }

    public String getParticleName()
    {
        return this.particleName;
    }

    public int getParticleID()
    {
        return this.particleID;
    }

    public int getArgumentCount()
    {
        return this.argumentCount;
    }

    public boolean getShouldIgnoreRange()
    {
        return this.shouldIgnoreRange;
    }

    /**
     * Gets the relative EnumParticleTypes by id.
     */
    @Nullable
    public static EnumArcanaParticles getParticleFromId(int particleId)
    {
        return PARTICLES.get(Integer.valueOf(particleId));
    }

    @Nullable
    public static EnumArcanaParticles getByName(String nameIn)
    {
        return BY_NAME.get(nameIn);
    }

    static
    {
        for (EnumArcanaParticles enumparticletypes : values())
        {
            PARTICLES.put(Integer.valueOf(enumparticletypes.getParticleID()), enumparticletypes);
            BY_NAME.put(enumparticletypes.getParticleName(), enumparticletypes);
        }
    }


}
