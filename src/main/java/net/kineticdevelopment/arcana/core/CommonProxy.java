package net.kineticdevelopment.arcana.core;

import net.kineticdevelopment.arcana.common.init.EntityInit;
import net.kineticdevelopment.arcana.common.objects.items.ItemWand;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class CommonProxy {
	public void registerItemRenderer(Item item, int meta, String id) {}

	public void preInit(FMLPreInitializationEvent event) {

		EntityInit.init();

	}

	public void init(FMLInitializationEvent event) {}

	public void postInit(FMLPostInitializationEvent event) {}

	public void registerWand(IForgeRegistry<Item> registry, ItemWand wand) {
		registry.register(wand);
	}

}
