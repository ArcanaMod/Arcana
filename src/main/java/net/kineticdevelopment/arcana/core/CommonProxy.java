package net.kineticdevelopment.arcana.core;

import net.kineticdevelopment.arcana.client.Sounds;
import net.kineticdevelopment.arcana.common.handlers.WorldTickHandler;
import net.kineticdevelopment.arcana.common.init.EntityInit;
import net.kineticdevelopment.arcana.common.network.Connection;
import net.kineticdevelopment.arcana.common.objects.blocks.bases.LeavesBase;
import net.kineticdevelopment.arcana.common.objects.items.ItemWand;
import net.kineticdevelopment.arcana.common.objects.tile.ResearchTableTileEntity;
import net.kineticdevelopment.arcana.common.worldgen.OreGenerator;
import net.kineticdevelopment.arcana.core.research.EntrySection;
import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.kineticdevelopment.arcana.core.research.Requirement;
import net.kineticdevelopment.arcana.core.research.ResearchLoader;
import net.kineticdevelopment.arcana.core.research.impls.ResearcherCapability;
import net.kineticdevelopment.arcana.core.spells.SpellEffectHandler;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Common Proxy
 * 
 * @author Atlas
 */
@Mod.EventBusSubscriber
public class CommonProxy {
	public void registerItemRenderer(Item item, int meta, String id) {}

	public void preInit(FMLPreInitializationEvent event) {
		EntityInit.init();

		GameRegistry.registerWorldGenerator(OreGenerator.instance, 5);
		MinecraftForge.EVENT_BUS.register(OreGenerator.instance);

		// might want to group these together somehow...
		EntrySection.init();
		Requirement.init();
		ResearcherCapability.init();
		Puzzle.init();
	}

	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(WorldTickHandler.instance);

		Connection.init();
		ResearchLoader.load();
		
		Sounds.registerSounds();
		SpellEffectHandler.init();
	}

	public void postInit(FMLPostInitializationEvent event) {}

	public void registerWand(IForgeRegistry<Item> registry, ItemWand wand) {
		registry.register(wand);
	}
	
	public void setGraphicsLevel(LeavesBase parBlock, boolean parFancyEnabled) {}
	
	public void openResearchBookUI(ResourceLocation book){}
	public void openResearchTableUI(ResearchTableTileEntity te){}
}
