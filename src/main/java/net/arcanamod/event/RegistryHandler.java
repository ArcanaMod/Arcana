package net.arcanamod.event;

import net.arcanamod.blocks.tiles.NodeTileEntity;
import net.arcanamod.items.ItemWand;
import net.arcanamod.util.IHasModel;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.blocks.tiles.ResearchTableTileEntity;
import net.arcanamod.aspects.Aspects;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Register objects here
 *
 * @author Atlas, Merijn
 */
@EventBusSubscriber
public class RegistryHandler{
	
	/**
	 * Register Items here
	 *
	 * @param event
	 */
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event){
		Aspects.register();
		event.getRegistry().registerAll(ArcanaItems.ITEMS.toArray(new Item[0]));
		for(ItemWand wand : ItemWand.WANDS){
			Arcana.proxy.registerWand(event.getRegistry(), wand);
		}
	}
	
	/**
	 * Register Blocks here
	 *
	 * @param event
	 */
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event){
		event.getRegistry().registerAll(ArcanaBlocks.BLOCKS.toArray(new Block[0]));
		GameRegistry.registerTileEntity(NodeTileEntity.class, new ResourceLocation(Arcana.MODID, "tile_normalnode"));
		GameRegistry.registerTileEntity(ResearchTableTileEntity.class, ResearchTableTileEntity.ID);
	}
	
	/**
	 * Register Models Here
	 *
	 * @param event
	 */
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event){
		for(Item item : ArcanaItems.ITEMS){
			if(item instanceof IHasModel){
				((IHasModel)item).registerModels();
			}
		}
		for(Block block : ArcanaBlocks.BLOCKS){
			if(block instanceof IHasModel){
				((IHasModel)block).registerModels();
			}
		}
	}
}