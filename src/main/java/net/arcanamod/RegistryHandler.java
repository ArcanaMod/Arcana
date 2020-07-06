package net.arcanamod;

import net.arcanamod.aspects.AspectManager;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
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
		AspectManager.register();
		IForgeRegistry<Item> registry = event.getRegistry();
		ArcanaBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
			Item.Properties properties = new Item.Properties();
			if(block instanceof GroupedBlock){
				GroupedBlock grouped = (GroupedBlock)block;
				ItemGroup group = grouped.getGroup();
				if(group != null)
					properties = properties.group(group);
			}else
				properties = properties.group(Arcana.ITEMS);
			BlockItem blockItem = new BlockItem(block, properties);
			blockItem.setRegistryName(block.getRegistryName());
			registry.register(blockItem);
		});
	}
}