package net.arcanamod;

import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.VisHandlerCapability;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.OreDictEntry;
import net.arcanamod.client.Sounds;
import net.arcanamod.commands.CommandFocus;
import net.arcanamod.commands.ResearchCommand;
import net.arcanamod.entities.ArcanaEntities;
import net.arcanamod.event.WorldTickHandler;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.research.EntrySection;
import net.arcanamod.research.Puzzle;
import net.arcanamod.research.Requirement;
import net.arcanamod.research.ResearchLoader;
import net.arcanamod.spells.SpellEffectHandler;
import net.arcanamod.worldgen.NodeGenerator;
import net.arcanamod.worldgen.OreGenerator;
import net.arcanamod.network.Connection;
import net.arcanamod.research.impls.ResearcherCapability;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

/**
 * Base Arcana Class
 *
 * @author Atlas
 */
@Mod(modid = Arcana.MODID, name = Arcana.NAME, version = Arcana.VERSION)
public class Arcana{
	public static final String MODID = "arcana";
	public static final String NAME = "Arcana";
	public static final String VERSION = "1.0";
	
	public static final Logger logger = LogManager.getLogger("Arcana");
	
	@Mod.Instance
	public static Arcana instance;
	
	@SidedProxy(clientSide = "net.arcanamod.ClientProxy", serverSide = "net.arcanamod.CommonProxy")
	public static CommonProxy proxy;
	
	/**
	 * Preintialization Event
	 */
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event){
		ArcanaEntities.init();
		
		GameRegistry.registerWorldGenerator(OreGenerator.instance, 5);
		MinecraftForge.EVENT_BUS.register(OreGenerator.instance);
		GameRegistry.registerWorldGenerator(NodeGenerator.instance, 20);
		MinecraftForge.EVENT_BUS.register(NodeGenerator.instance);
		
		// might want to group these together somehow...
		EntrySection.init();
		Requirement.init();
		ResearcherCapability.init();
		VisHandlerCapability.init();
		Puzzle.init();
		
		proxy.preInit(event);
	}
	
	/**
	 * Initialization Event
	 */
	@EventHandler
	public void onInit(FMLInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(WorldTickHandler.instance);
		
		Connection.init();
		ResearchLoader.load();
		NetworkRegistry.INSTANCE.registerGuiHandler(Arcana.instance, new ArcanaGuiHandler());
		
		Sounds.registerSounds();
		SpellEffectHandler.init();
		
		for(Block block : ArcanaBlocks.BLOCKS)
			if(block instanceof OreDictEntry)
				OreDictionary.registerOre(((OreDictEntry)block).getOreDictName(), block);
		
		FurnaceRecipes.instance().addSmeltingRecipeForBlock(ArcanaBlocks.AMBER_ORE, new ItemStack(ArcanaItems.AMBER), 0.2f);
		FurnaceRecipes.instance().addSmeltingRecipeForBlock(ArcanaBlocks.TAINTED_OAK_LOG, new ItemStack(ArcanaItems.TAINTED_WAND_CORE), 1);
		
		proxy.init(event);
	}
	
	/**
	 * Post Initialization Event
	 */
	@EventHandler
	public void onPostInit(FMLPostInitializationEvent event){
		proxy.postInit(event);
	}
	
	/**
	 * Main Arcana Creative Tab
	 */
	public static CreativeTabs TAB_ARCANA = (new CreativeTabs("tabArcana"){
		@Override
		@Nonnull
		public ItemStack getTabIconItem(){
			return new ItemStack(ArcanaItems.ARCANIUM_WAND_CORE);
		}
	});
	
	public static CreativeTabs TAB_TAINTARCANA = (new CreativeTabs("tabTaintArcana"){
		@Override
		@Nonnull
		public ItemStack getTabIconItem(){
			return new ItemStack(ArcanaBlocks.TAINTED_GRASS);
		}
	});
	
	public static CreativeTabs TAB_ASPECTS_ARCANA = (new CreativeTabs("tabAspectsArcana"){
		@Nonnull
		public ItemStack getTabIconItem(){
			return new ItemStack(Aspects.aspectItems.get(0));
		}
		
		@Nonnull
		public ItemStack getIconItemStack(){
			return proxy.getAspectItemStackForDisplay();
		}
	});
	
	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event){
		event.registerServerCommand(new CommandFocus());
		// event.registerServerCommand(new TaintLevelCommand());
		event.registerServerCommand(new ResearchCommand());
	}
	
	//Why is this here? This is very redundant
	
	/**
	 * Retrieves NBT Tag
	 */
	public static NBTTagCompound getNBT(ItemStack itemStack){
		if(!itemStack.hasTagCompound()){
			itemStack.setTagCompound(new NBTTagCompound());
		}
		
		return itemStack.getTagCompound();
	}
}