package net.arcanamod.entities;

import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.Arcana;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Initialize Entities here
 *
 * @author Merijn
 * @see ArcanaBlocks
 * @see ArcanaItems
 */
public class ArcanaEntities{
	public static void init(){
		int id = 1;
		EntityRegistry.registerModEntity(new ResourceLocation(Arcana.MODID, "spellentity"), SpellEntity.class, "spellentity", id++, Arcana.instance, 0, 3, true);
	}
}
