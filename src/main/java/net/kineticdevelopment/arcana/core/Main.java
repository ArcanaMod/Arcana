package net.kineticdevelopment.arcana.core;

import net.kineticdevelopment.arcana.common.commands.CommandFocus;
import net.kineticdevelopment.arcana.common.commands.ReloadResearchCommand;
import net.kineticdevelopment.arcana.common.commands.TaintLevelCommand;
import net.kineticdevelopment.arcana.common.init.BlockInit;
import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.core.research.EntrySection;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Base Arcana Class
 * @author Atlas
 */
@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {
	public static final String MODID = "arcana";
	public static final String NAME = "Arcana";
	public static final String VERSION = "1.0";
	
	public static final Logger logger = LogManager.getLogger("Arcana");

	@Mod.Instance
	public static Main instance;


	@SidedProxy(clientSide="net.kineticdevelopment.arcana.core.ClientProxy", serverSide="net.kineticdevelopment.arcana.core.CommonProxy")
    public static CommonProxy proxy;


	/**
	 * Preintialization Event
	 * @param event
	 */
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}
	
	/**
	 * Initialization Event
	 * @param event
	 */
	@EventHandler
	public void onInit(FMLInitializationEvent event) {
		proxy.init(event);
	}
	
	/**
	 * Post Initialization Event
	 * @param event
	 */
	@EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
	
	/**
	 * Main Arcana Creative Tab
	 */
	public static CreativeTabs TAB_ARCANA = (new CreativeTabs("tabArcana") {
        @Override
        public ItemStack getTabIconItem() {
			return new ItemStack(ItemInit.ARCANIUM_WAND_CORE);
        }
    });

	public static CreativeTabs TAB_TAINTARCANA = (new CreativeTabs("tabTaintArcana") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(BlockInit.TAINTED_GRASS);
		}
	});


	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandFocus());
		event.registerServerCommand(new TaintLevelCommand());
		event.registerServerCommand(new ReloadResearchCommand());
	}

	//Why is this here? This is very redundant
	/**
	 * Retrieves NBT Tag
	 * @param itemStack
	 * @return
	 */
	public static NBTTagCompound getNBT(ItemStack itemStack) {
		if (!itemStack.hasTagCompound()) {
			itemStack.setTagCompound(new NBTTagCompound());
		}

		return itemStack.getTagCompound();
	}
}