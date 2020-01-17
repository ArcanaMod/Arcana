package net.kineticdevelopment.arcana.common.init;

import net.kineticdevelopment.arcana.api.spells.SpellEffectHandler;
import net.kineticdevelopment.arcana.common.entities.SpellEntity;
import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityInit {
    public static void init() {
        int id = 1;
        EntityRegistry.registerModEntity(new ResourceLocation(Main.MODID, "spellentity"), SpellEntity.class, "spellentity", id++, Main.instance, 0 , 3, true);

    }
}
