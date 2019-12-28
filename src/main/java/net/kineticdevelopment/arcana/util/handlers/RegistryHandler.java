package net.kineticdevelopment.arcana.util.handlers;

import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.init.blockinit;
import net.kineticdevelopment.arcana.init.iteminit;
import net.kineticdevelopment.arcana.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

@EventBusSubscriber
public class RegistryHandler 
{
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(iteminit.ITEMS.toArray(new Item[0]));
		OreDictionary.registerOre("logWood", new ItemStack(blockinit.SILVERWOODLOG));
		OreDictionary.registerOre("logWood", new ItemStack(blockinit.GREATWOODLOG));
		OreDictionary.registerOre("logSilverwood", new ItemStack(blockinit.SILVERWOODLOG));
		OreDictionary.registerOre("logGreatwood", new ItemStack(blockinit.GREATWOODLOG));
		OreDictionary.registerOre("plankWood", new ItemStack(blockinit.GREATWOODPLANKS));
		OreDictionary.registerOre("plankGreatWood", new ItemStack(blockinit.GREATWOODPLANKS));
		OreDictionary.registerOre("plankWood", new ItemStack(blockinit.SILVERWOODPLANKS));
		OreDictionary.registerOre("plankSilverWood", new ItemStack(blockinit.SILVERWOODPLANKS));
		OreDictionary.registerOre("gemAmber", new ItemStack(iteminit.AMBER));
		OreDictionary.registerOre("Amber", new ItemStack(iteminit.AMBER));
		OreDictionary.registerOre("ingotQuicksilver", new ItemStack(iteminit.QUICKSILVER));
		OreDictionary.registerOre("Quicksilver", new ItemStack(iteminit.QUICKSILVER));
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(blockinit.BLOCKS.toArray(new Block[0]));
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		for(Item item:iteminit.ITEMS)
		{
			if(item instanceof IHasModel)
			{
				((IHasModel)item).registerModels();
			}
		}
		for(Block block:blockinit.BLOCKS)
		{
			if(block instanceof IHasModel)
			{
				((IHasModel)block).registerModels();
			}
		}
	}
}
