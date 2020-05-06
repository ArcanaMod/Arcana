package net.arcanamod.event;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Register objects here
 *
 * @author Atlas, Merijn
 */
@EventBusSubscriber(modid = Arcana.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RegistryHandler{
	
	/**
	 * Register Items here
	 *
	 * @param event
	 */
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onItemRegister(RegistryEvent.Register<Item> event){
		Aspects.register();
		IForgeRegistry<Item> registry = event.getRegistry();
		ArcanaBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
			Item.Properties properties = new Item.Properties();
			if(block instanceof GroupedBlock)
				properties = properties.group(((GroupedBlock)block).getGroup());
			BlockItem blockItem = new BlockItem(block, properties);
			blockItem.setRegistryName(block.getRegistryName());
			registry.register(blockItem);
		});
		//event.getRegistry().registerAll(ArcanaItems.ITEMS.toArray(new Item[0]));
		/*for(ItemWand wand : ItemWand.WANDS)
			Arcana.proxy.registerWand(event.getRegistry(), wand);*/
	}
	
	/**
	 * Register Blocks here
	 *
	 * @param event
	 */
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event){
		//event.getRegistry().registerAll(ArcanaBlocks.BLOCKS.toArray(new Block[0]));
		/*GameRegistry.registerTileEntity(NodeTileEntity.class, new ResourceLocation(Arcana.MODID, "tile_normalnode"));
		GameRegistry.registerTileEntity(ResearchTableTileEntity.class, ResearchTableTileEntity.ID);*/
	}
	
	/**
	 * Register Models Here
	 *
	 * @param event
	 */
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event){
		/*for(Item item : ArcanaItems.ITEMS){
			if(item instanceof IHasModel){
				((IHasModel)item).registerModels();
			}
		}
		for(Block block : ArcanaBlocks.BLOCKS){
			if(block instanceof IHasModel){
				((IHasModel)block).registerModels();
			}
		}*/
	}
}