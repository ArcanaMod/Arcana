package net.kineticdevelopment.arcana.common.handlers;

import net.kineticdevelopment.arcana.common.blocks.OreDictEntry;
import net.kineticdevelopment.arcana.common.init.BlockInit;
import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.common.items.ItemWand;
import net.kineticdevelopment.arcana.common.objects.tile.NodeTileEntity;
import net.kineticdevelopment.arcana.common.objects.tile.ResearchTableTileEntity;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.aspects.Aspects;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Register objects here
 * 
 * @author Atlas, Merijn
 */
@EventBusSubscriber
public class RegistryHandler {
	
	/**
	 * Register Items here
	 * @param event
	 */
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		Aspects.register();
		event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
		for(ItemWand wand : ItemWand.WANDS) {
			Main.proxy.registerWand(event.getRegistry(), wand);
		}
	}
	
	/**
	 * Register Blocks here
	 * @param event
	 */
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(BlockInit.BLOCKS.toArray(new Block[0]));
		GameRegistry.registerTileEntity(NodeTileEntity.class, new ResourceLocation(Main.MODID, "tile_normalnode"));
		GameRegistry.registerTileEntity(ResearchTableTileEntity.class, ResearchTableTileEntity.ID);
	}
	
	/**
	 * Register Models Here
	 * @param event
	 */
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for (Item item : ItemInit.ITEMS) {
			if (item instanceof IHasModel) {
				((IHasModel) item).registerModels();
			}
		}
		for (Block block : BlockInit.BLOCKS) {
			if (block instanceof IHasModel) {
				((IHasModel) block).registerModels();
			}
		}
	}
}