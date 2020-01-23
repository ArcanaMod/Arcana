package net.kineticdevelopment.arcana.api.spells;

import net.kineticdevelopment.arcana.api.aspects.Aspect;
import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Helper for foci
 * @author Merijn
 */
public class FociHelper {

    /**
     * Used to generate a focus item.
     * @param skin Skin variant
     * @param effects Array of {@link ISpellEffect} with the effects
     * @param power Integer of how strong the effect is
     * @param core Value of {@link net.kineticdevelopment.arcana.api.aspects.Aspect.AspectType}
     * @param name Name of the spell
     * @return ItemStack with the focus properties.
     */
    public static ItemStack generateFocus(int skin, ISpellEffect[] effects, int power, Aspect.AspectType core, String name) {

        ItemStack is = new ItemStack(ItemInit.FOCUS);

        NBTTagCompound tag = is.getOrCreateSubCompound("foci");

        StringBuilder sb = new StringBuilder();
        for (ISpellEffect effect : effects) {
            sb.append(effect.getName()).append(";");
        }

        tag.setString("effects", sb.toString());
        tag.setInteger("power", power);
        tag.setString("core", core.toString());
        tag.setString("name", name);
        NBTTagCompound newtag = new NBTTagCompound();
        newtag.setTag("foci", tag);
        newtag.setInteger("variant", skin);

        is.setStackDisplayName(name);
        is.setTagCompound(newtag);

        return is;
    }






}
