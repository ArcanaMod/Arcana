package net.kineticdevelopment.arcana.core;

import net.kineticdevelopment.arcana.ArcanaGuiHandler;
import net.kineticdevelopment.arcana.client.Sounds;
import net.kineticdevelopment.arcana.common.blocks.OreDictEntry;
import net.kineticdevelopment.arcana.common.handlers.WorldTickHandler;
import net.kineticdevelopment.arcana.common.init.BlockInit;
import net.kineticdevelopment.arcana.common.init.EntityInit;
import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.common.network.Connection;
import net.kineticdevelopment.arcana.common.objects.blocks.bases.LeavesBase;
import net.kineticdevelopment.arcana.common.items.ItemWand;
import net.kineticdevelopment.arcana.common.worldgen.NodeGenerator;
import net.kineticdevelopment.arcana.common.worldgen.OreGenerator;
import net.kineticdevelopment.arcana.core.aspects.Aspects;
import net.kineticdevelopment.arcana.core.aspects.AspectHandlerCapability;
import net.kineticdevelopment.arcana.core.research.EntrySection;
import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.kineticdevelopment.arcana.core.research.Requirement;
import net.kineticdevelopment.arcana.core.research.ResearchLoader;
import net.kineticdevelopment.arcana.core.research.impls.ResearcherCapability;
import net.kineticdevelopment.arcana.core.spells.SpellEffectHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.stream.Collectors;

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
		GameRegistry.registerWorldGenerator(NodeGenerator.instance, 20);
		MinecraftForge.EVENT_BUS.register(NodeGenerator.instance);

		// might want to group these together somehow...
		EntrySection.init();
		Requirement.init();
		ResearcherCapability.init();
		AspectHandlerCapability.init();
		Puzzle.init();
	}

	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(WorldTickHandler.instance);

		Connection.init();
		ResearchLoader.load();
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new ArcanaGuiHandler());
		
		Sounds.registerSounds();
		SpellEffectHandler.init();
		
		for(Block block : BlockInit.BLOCKS)
			if(block instanceof OreDictEntry)
				OreDictionary.registerOre(((OreDictEntry)block).getOreDictName(), block);
		
		FurnaceRecipes.instance().addSmeltingRecipeForBlock(BlockInit.AMBER_ORE, new ItemStack(ItemInit.AMBER), 0.2f);
		FurnaceRecipes.instance().addSmeltingRecipeForBlock(BlockInit.TAINTED_OAK_LOG, new ItemStack(ItemInit.TAINTED_WAND_CORE), 1);
	}

	public void postInit(FMLPostInitializationEvent event){
	}

	public void registerWand(IForgeRegistry<Item> registry, ItemWand wand) {
		registry.register(wand);
	}
	
	public void setGraphicsLevel(LeavesBase parBlock, boolean parFancyEnabled) {}
	
	public void openResearchBookUI(ResourceLocation book){}

	public ItemStack getAspectItemStackForDisplay(){
		return Aspects.aspectStacks.get(0);
	}
}
