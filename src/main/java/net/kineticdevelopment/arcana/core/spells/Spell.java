package net.kineticdevelopment.arcana.core.spells;

import net.kineticdevelopment.arcana.common.objects.entities.SpellEntity;
import net.kineticdevelopment.arcana.core.aspects.Aspect;
import net.kineticdevelopment.arcana.core.spells.effects.ISpellEffect;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Spell object class.
 * 
 * @author Merijn
 */
public class Spell {

    private ISpellEffect[] effects;
    private Aspect.AspectType core;
    private String name;
    private int power;

    /**
     * Default constructor
     * @param effects Array of {@link ISpellEffect}
     * @param core Value of {@link net.kineticdevelopment.arcana.core.aspects.Aspect.AspectType}
     * @param name Name of the spell
     * @param power Determines the strength of the spell
     */

    public Spell(ISpellEffect[] effects, Aspect.AspectType core, String name, int power) {
        this.effects = effects;
        this.core = core;
        this.name = name;
        this.power = power;
    }

    /**
     * Getter for the effects of the spell
     * @return Array of {@link ISpellEffect}
     */

    public ISpellEffect[] getEffects() {
        return effects;
    }

    /**
     * Getter of the spell core
     * @return Value of {@link net.kineticdevelopment.arcana.core.aspects.Aspect.AspectType}
     */
    public Aspect.AspectType getCore() {
        return core;
    }

    /**
     * Getter of the spell name
     * @return Name of the spell
     */
    public String getName() {
        return name;
    }

    /**
     * Getter of the power value
     * @return Power of the spell
     */
    public int getPower() {
        return power;
    }

    /**
     * Casts the spell
     * @param player Caster of the spell
     */
    public void cast(EntityPlayer player) {
        switch(core) {
            case EARTH:
                for (ISpellEffect effect : effects) {
                    if (effect == null) {
                        continue;
                    }
                    if (player.getEntityWorld().getBlockState(player.getPosition()) != Blocks.AIR.getDefaultState()) {
                        effect.getEffect(player.getPosition(), player.getEntityWorld(), power);
                        effect.getEffect(player, power);
                    } else {
                        effect.getEffect(player, power);
                    }
                    //effect.getEffect(player.getPosition(), player.getEntityWorld(), power);
                }
                break;
            case AIR:
                SpellEntity entity = new SpellEntity(player.getEntityWorld(), player, this);
                entity.setPosition(player.posX, player.getEntityBoundingBox().minY + (double) player.getEyeHeight(), player.posZ);
                entity.setNoGravity(true);
                float rotationYawIn = player.rotationPitch;
                float rotationPitchIn = player.rotationPitch;
                float pitchOffset = 2F;
                float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
                float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
                float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
                entity.shoot(f, f1, f2, 5F, 0.0F);
                System.out.println("spawnedSpellentity");
                player.getEntityWorld().spawnEntity(entity);
                break;
            case CHAOS:
                List<EntityLivingBase> nearbyEntities = player.getEntityWorld().getEntitiesWithinAABB(EntityLiving.class, player.getEntityBoundingBox().expand(power, power, power));
                for (EntityLivingBase ent : nearbyEntities) {
                    if (ent != player) {
                        for (ISpellEffect effect : effects) {
                            effect.getEffect(ent, power);
                        }
                    }
                }
                break;
            case WATER:
                float yaw = player.rotationYawHead - 12F;
                for (int i = 0; i < 5; i++) {
                    SpellEntity entity1 = new SpellEntity(player.getEntityWorld(), player, this);
                    entity1.setWorld(player.getEntityWorld());
                    entity1.setNoGravity(true);
                    float wrotationYawIn = yaw;
                    float wrotationPitchIn = player.rotationPitch;
                    float wpitchOffset = 2F;
                    float wf = -MathHelper.sin(wrotationYawIn * 0.017453292F) * MathHelper.cos(wrotationPitchIn * 0.017453292F);
                    float wf1 = -MathHelper.sin((wrotationPitchIn + wpitchOffset) * 0.017453292F);
                    float wf2 = MathHelper.cos(wrotationYawIn * 0.017453292F) * MathHelper.cos(wrotationPitchIn * 0.017453292F);
                    entity1.shoot(wf, wf1, wf2, 5F, 0.0F);
                    yaw = yaw + 6F;

                    player.getEntityWorld().spawnEntity(entity1);
                }
                break;
            case ORDER:
                List<EntityLivingBase> nearbyEntities1 = player.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().expand(power, power, power));
                for (EntityLivingBase ent : nearbyEntities1) {
                    if (ent != player) {
                        for (ISpellEffect effect : effects) {
                            effect.getEffect(ent, power);
                        }
                        break;
                    }
                }
                break;
        }
    }


    /**
     * Creates an spell object based of NBT
     * @param spell NBTTagCompound of the spell
     * @return {@link Spell} object
     */
    public static Spell fromNBT(NBTTagCompound spell) {
        List<ISpellEffect> effects = new ArrayList<>();
        for (String effect : spell.getString("effects").split(";")) {
            ISpellEffect effectObj = SpellEffectHandler.getEffect(effect);
            if(effectObj == null) {
                throw new NullPointerException("Spell Effect cannot be null!");
            }
            effects.add(effectObj);
        }
        Aspect.AspectType core = Aspect.AspectType.valueOf(spell.getString("core").toUpperCase());
        int power = spell.getInteger("power");
        String name = spell.getString("name");

        return new Spell(effects.toArray(new ISpellEffect[0]), core, name, power);
    }

}
