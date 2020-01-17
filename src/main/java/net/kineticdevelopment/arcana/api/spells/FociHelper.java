package net.kineticdevelopment.arcana.api.spells;

import net.kineticdevelopment.arcana.api.aspects.Aspect;
import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FociHelper {

    public static ItemStack generateFoci(int skin, ISpellEffect[] effects, int power, Aspect.AspectType core, String name) {

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
        NBTTagCompound newtag = is.getTagCompound();
        newtag.setTag("foci", tag);

        is.setStackDisplayName(name);
        is.setTagCompound(newtag);

        return is;
    }






}
