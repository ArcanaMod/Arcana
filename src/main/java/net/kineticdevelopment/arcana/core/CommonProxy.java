package net.kineticdevelopment.arcana.core;

import net.kineticdevelopment.arcana.common.init.EntityInit;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public void registerItemRenderer(Item item, int meta, String id) {}

	public void preInit(FMLPreInitializationEvent event) {

		EntityInit.init();

	}

	public void init(FMLInitializationEvent event) {}

	public void postInit(FMLPostInitializationEvent event) {}

}
