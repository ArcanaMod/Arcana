package net.arcanamod;

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
	}
}